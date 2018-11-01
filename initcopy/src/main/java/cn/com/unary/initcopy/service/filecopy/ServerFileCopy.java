package cn.com.unary.initcopy.service.filecopy;

import cn.com.unary.initcopy.InitCopyContext;
import cn.com.unary.initcopy.common.AbstractLoggable;
import cn.com.unary.initcopy.common.ExecExceptionsHandler;
import cn.com.unary.initcopy.dao.FileManager;
import cn.com.unary.initcopy.entity.Constants;
import cn.com.unary.initcopy.exception.InfoPersistenceException;
import cn.com.unary.initcopy.exception.TaskFailException;
import cn.com.unary.initcopy.grpc.entity.ClientInitReq;
import cn.com.unary.initcopy.grpc.entity.ServerInitResp;
import cn.com.unary.initcopy.grpc.entity.SyncProcess;
import cn.com.unary.initcopy.service.filecopy.filepacker.SyncAllPacker;
import cn.com.unary.initcopy.service.filecopy.fileresolver.Resolver;
import cn.com.unary.initcopy.service.filecopy.fileresolver.RsyncResolver;
import cn.com.unary.initcopy.service.filecopy.fileresolver.SyncAllResolver;
import cn.com.unary.initcopy.service.filecopy.init.ServerFileCopyInit;
import cn.com.unary.initcopy.service.transmit.DataHandlerAdapter;
import lombok.Setter;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static cn.com.unary.initcopy.InitCopyContext.RESOURCE_TIME_OUT;

/**
 * 目标端的文件复制模块
 * 线程安全
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Component("ServerFileCopy")
@Scope("singleton")
public class ServerFileCopy extends AbstractLoggable implements DataHandlerAdapter, ApplicationContextAware, Closeable {

    private final ThreadPoolExecutor fileCopyExec;
    private final Object lock;
    /**
     * 存放待执行的任务列表
     */
    private final Map<String, CopyTask> taskMap;
    /**
     * 已放入线程池中执行的任务
     */
    private final Map<String, CopyTask> execTaskMap;
    @Autowired
    private ServerFileCopyInit init;
    @Autowired
    @Qualifier("clientFM")
    private FileManager fm;
    private volatile boolean close;
    @Setter
    private ApplicationContext applicationContext;

    public ServerFileCopy() {
        close = false;
        lock = new Object();
        taskMap = new HashMap<>(InitCopyContext.TASK_NUMBER);
        execTaskMap = new HashMap<>(InitCopyContext.TASK_NUMBER);
        ThreadFactory executorThreadFactory = new BasicThreadFactory.Builder()
                .namingPattern("server-%d-task-")
                .uncaughtExceptionHandler(new ExecExceptionsHandler(this))
                .build();
        fileCopyExec = new ThreadPoolExecutor(InitCopyContext.SERVER_CORE_POOL_SIZE,
                InitCopyContext.SERVER_MAX_POOL_SIZE, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(200),
                executorThreadFactory,
                new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * 先根据包信息来确定属于哪个任务。
     * 如任务已被停止，则直接返回
     *
     * @param data 数据包
     */
    @Override
    public void handle(byte[] data) {
        if (close) {
            logger.error("FileCopy already shutdown.");
            return;
        }
        String taskId = new String(data, 0, InitCopyContext.UUID_LEN, InitCopyContext.CHARSET);
        synchronized (lock) {
            if (close) {
                logger.error("FileCopy already shutdown.");
                return;
            }
            if (execTaskMap.containsKey(taskId)) {
                execTaskMap.get(taskId).addPack(data);
            } else {
                if (taskMap.containsKey(taskId)) {
                    final CopyTask copyTask = taskMap.remove(taskId);
                    copyTask.addPack(data);
                    fileCopyExec.execute(copyTask);
                    execTaskMap.put(taskId, copyTask);
                } else {
                    logger.warn(String.format("Task not found:%s, ignore a pack.", taskId));
                }
            }
        }
    }

    /**
     * 收到初始化请求后，新建任务并放入任务列表中。
     *
     * @param req 初始化请求
     * @return 初始化响应
     * @throws TaskFailException 任务初始化失败异常
     */
    public ServerInitResp startInit(ClientInitReq req) throws TaskFailException {
        final CopyTask task = new CopyTask(req.getTaskId(), req.getBackUpPath());
        synchronized (lock) {
            if (close) {
                throw new TaskFailException("FileCopy already shutdown.");
            }
            final CopyTask oldTask = taskMap.put(req.getTaskId(), task);
            if (null != oldTask) {
                logger.warn(String.format("Task %s was replaced by %s", oldTask.toString(), task.toString()));
            }
        }
        try {
            return init.startInit(req);
        } catch (InfoPersistenceException e) {
            // 初始化失败后，移除当前任务
            synchronized (lock) {
                taskMap.remove(req.getTaskId());
            }
            throw new TaskFailException("init task failed.", e);
        }
    }

    @PreDestroy
    @Override
    public void close() {
        synchronized (lock) {
            fileCopyExec.shutdownNow();
            taskMap.clear();
            for (Map.Entry<String, CopyTask> entry : execTaskMap.entrySet()) {
                entry.getValue().shutdown();
                fm.deleteFileInfoByTaskId(entry.getValue().taskId);
            }
            execTaskMap.clear();
            close = true;
        }
    }

    /**
     * 停止当前任务并从任务 map 中移除
     *
     * @param taskId 任务 Id
     * @throws IOException Io 异常
     */
    public void deleteTask(String taskId) throws IOException {
        synchronized (lock) {
            CopyTask task = execTaskMap.remove(taskId);
            if (task == null) {
                task = taskMap.remove(taskId);
                if (task == null) {
                    throw new IllegalStateException(String.format("Task not found:%s", taskId));
                }
            }
            logger.info(String.format("Task delete:%s", task.toString()));
        }
    }

    /**
     * 检查在线程池中执行的任务列表，如果超时未接受到数据，
     * 默认为客户端断开连接，则暂停该任务并放入待执行任务列表中
     */
    public void clean() {
        synchronized (lock) {
            Iterator<Map.Entry<String, CopyTask>> iterator = execTaskMap.entrySet().iterator();
            Map.Entry<String, CopyTask> entry;
            long currentTime = System.currentTimeMillis();
            while (iterator.hasNext()) {
                entry = iterator.next();
                if (currentTime - entry.getValue().timeStamp > RESOURCE_TIME_OUT) {
                    iterator.remove();
                    entry.getValue().pause = true;
                    taskMap.put(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    public void resume(String taskId) {
    }

    class CopyTask implements Runnable {
        private final List<byte[]> packs;
        private final Object lock;
        private final String taskId, backUpPath;
        private final AtomicInteger tempWait = new AtomicInteger(0);
        private final AtomicInteger tempNotify = new AtomicInteger(0);
        private Resolver resolver;
        /**
         * wait 表示当前拷贝任务处于挂起状态，等待源端传包
         * shutdown 表示当前任务已被关闭
         * pause 表示资源长时间未使用，被关闭
         */
        private volatile boolean wait, shutdown, pause;
        /**
         * 资源被使用的最近时间点
         */
        private long timeStamp;
        /**
         * 用于任务计时
         */
        private long waitTime, tempTime, startTime;
        private SyncProcess syncProcessDO;

        /**
         * @param taskId     任务Id
         * @param backUpPath 如果源目录与目标目录是多对一关系，则不为空，否则设为 null
         */
        private CopyTask(String taskId, String backUpPath) {
            wait = false;
            shutdown = false;
            lock = new Object();
            this.taskId = taskId;
            this.backUpPath = backUpPath;
            packs = new ArrayList<>(30);
            startTime = System.currentTimeMillis();
        }

        /**
         * 根据解析器种类来选择不同的解析器。
         */
        @Override
        public void run() {
            pause = false;
            Thread.currentThread().setName(Thread.currentThread().getName() + taskId);
            byte[] pack;
            while (true) {
                synchronized (lock) {
                    if (!packs.isEmpty()) {
                        pack = packs.get(0);
                        packs.remove(0);
                    } else {
                        if (pause) {
                            logger.info(String.format("CopyTask:%s, paused.", taskId));
                            break;
                        }
                        logger.info(tempWait.incrementAndGet() + "'s wait when pack null.");
                        wait = true;
                        try {
                            tempTime = System.currentTimeMillis();
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new IllegalStateException(e);
                        }
                        if (shutdown) {
                            break;
                        } else {
                            continue;
                        }

                    }
                }
                resolve(pack);
            }
            this.shutdown();
        }

        private void resolve(byte[] pack) {
            try {
                if (fm.taskFinished(taskId)) {
                    throw new IllegalStateException("Task already finished.");
                }
                if (resolver == null) {
                    if (Constants.PackerType.SYNC_ALL_JAVA.getValue() == pack[SyncAllPacker.HEAD_LENGTH - 1]) {
                        resolver = applicationContext.getBean(SyncAllResolver.class);
                    } else {
                        resolver = applicationContext.getBean(RsyncResolver.class);
                    }
                    resolver.init(taskId, backUpPath);
                }
                resolver.process(pack);
                if (fm.taskFinished(taskId)) {
                    resolver.close();
                }
            } catch (IOException e) {
                throw new IllegalStateException("ERROR 0x01 : IO error.", e);
            } catch (InfoPersistenceException e) {
                throw new IllegalStateException("ERROR 0x02 : Persistence error.", e);
            }
        }

        private void addPack(byte[] pack) {
            synchronized (lock) {
                if (shutdown) {
                    return;
                }
                timeStamp = System.currentTimeMillis();
                this.packs.add(pack);
                if (wait) {
                    logger.info(tempNotify.incrementAndGet() + "'s notify when pack arrive.");
                    waitTime += System.currentTimeMillis() - tempTime;
                    lock.notify();
                    wait = false;
                }
            }
        }

        /**
         * 会将尝试停止当前任务
         * 1. 从任务执行 Map中移除该任务
         */
        void shutdown() {
            if (shutdown) {
                return;
            }
            synchronized (lock) {
                if (shutdown) {
                    return;
                }
                packs.clear();
                shutdown = true;
            }
            logger.info(String.format("Task resolver done. WaitTime: %d. Total cost:%d.",
                    waitTime, System.currentTimeMillis() - startTime));
        }
    }
}
