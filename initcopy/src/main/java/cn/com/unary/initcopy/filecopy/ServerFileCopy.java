package cn.com.unary.initcopy.filecopy;

import cn.com.unary.initcopy.InitCopyContext;
import cn.com.unary.initcopy.common.AbstractLoggable;
import cn.com.unary.initcopy.common.ExecExceptionsHandler;
import cn.com.unary.initcopy.entity.Constants;
import cn.com.unary.initcopy.exception.InfoPersistenceException;
import cn.com.unary.initcopy.exception.TaskFailException;
import cn.com.unary.initcopy.filecopy.filepacker.SyncAllPacker;
import cn.com.unary.initcopy.filecopy.fileresolver.Resolver;
import cn.com.unary.initcopy.filecopy.fileresolver.RsyncResolver;
import cn.com.unary.initcopy.filecopy.fileresolver.SyncAllResolver;
import cn.com.unary.initcopy.filecopy.init.ServerFileCopyInit;
import cn.com.unary.initcopy.grpc.entity.ClientInitReq;
import cn.com.unary.initcopy.grpc.entity.ServerInitResp;
import lombok.Setter;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 目标端的文件复制模块
 * 线程安全
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Component("ServerFileCopy")
@Scope("singleton")
public class ServerFileCopy extends AbstractLoggable implements ApplicationContextAware, Closeable {

    private final Object lock;
    @Autowired
    private ServerFileCopyInit init;
    private volatile boolean close;
    @Setter
    private ApplicationContext applicationContext;
    private ThreadPoolExecutor fileCopyExec;

    /**
     * 已放入线程池中执行的任务
     */
    private Map<String, CopyTask> execTaskMap;
    /**
     * 任务列表
     */
    private Map<String, CopyTask> taskMap;

    public ServerFileCopy() {
        close = false;
        lock = new Object();
        taskMap = new HashMap<>(InitCopyContext.TASK_NUMBER);
        execTaskMap = new HashMap<>(InitCopyContext.TASK_NUMBER);
        ThreadFactory executorThreadFactory = new BasicThreadFactory.Builder()
                .namingPattern("server-%d-task-")
                .uncaughtExceptionHandler(new ExecExceptionsHandler(this))
                .build();
        fileCopyExec = new ThreadPoolExecutor(2,
                30, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(200),
                executorThreadFactory,
                new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * 先根据包信息来确定属于哪个任务。
     *
     * @param data 数据包
     * @throws TaskFailException 当前任务停止时，抛出异常
     */
    public void resolverPack(byte[] data) throws TaskFailException {
        String taskId = takeTaskId(data);
        synchronized (lock) {
            if (close) {
                throw new TaskFailException("FileCopy already shutdown.");
            }
            if (execTaskMap.containsKey(taskId)) {
                execTaskMap.get(taskId).addPack(data);
            } else {
                if (taskMap.containsKey(taskId)) {
                    final CopyTask copyTask = taskMap.get(taskId);
                    copyTask.addPack(data);
                    fileCopyExec.execute(copyTask);
                    execTaskMap.put(taskId, copyTask);
                } else {
                    logger.error("Task " + taskId + " not found;");
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
        CopyTask task = new CopyTask(req.getTaskId(), req.getBackUpPath());
        synchronized (lock) {
            if (close) {
                throw new TaskFailException("FileCopy already shutdown.");
            }
            taskMap.put(req.getTaskId(), task);
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
            for (String i : taskMap.keySet()) {
                taskMap.get(i).close();
            }
            close = true;
        }
    }

    /**
     * 停止当前任务并从任务 map 中移除
     *
     * @param taskId 任务 Id
     * @throws IOException
     */
    public void deleteTask(String taskId) throws IOException {
        synchronized (lock) {
            if (!taskMap.containsKey(taskId)) {
                throw new IllegalStateException("Task not found with id:" + taskId);
            }
        }
        this.updateTask(taskId, Constants.UpdateType.PAUSE);
        synchronized (lock) {
            taskMap.remove(taskId);
        }
    }

    /**
     * 暂停或者唤醒某个任务
     *
     * @param taskId     任务Id
     * @param updateType 更新操作类型
     * @throws IOException IO 异常
     */
    public void updateTask(String taskId, Constants.UpdateType updateType) throws IOException {
        switch (updateType) {
            case RESUME:
                synchronized (lock) {
                    if (execTaskMap.containsKey(taskId)) {
                        return;
                    }
                    if (taskMap.containsKey(taskId)) {
                        final CopyTask copyTask = taskMap.get(taskId);
                        fileCopyExec.execute(copyTask);
                        execTaskMap.put(taskId, copyTask);
                    } else {
                        throw new IOException("Task " + taskId + " not found;");
                    }
                }
                break;
            case PAUSE:
            default:
                synchronized (lock) {
                    execTaskMap.get(taskId).close();
                }
                break;
        }
    }

    protected class CopyTask implements Runnable, Closeable {
        private final List<byte[]> packs;
        private final Object lock;
        private Resolver syncAllResolver, syncDiffResolver;
        private String taskId;
        /**
         * pause 表示当前拷贝任务处于挂起状态，等待源端传包
         * shutdown 表示当前任务已被关闭
         */
        private boolean pause, shutdown;
        private long execTime, tempTime;
        /**
         * 调试代码
         */
        private AtomicInteger wait = new AtomicInteger(0);
        private AtomicInteger notify = new AtomicInteger(0);

        /**
         * @param taskId     任务Id
         * @param backUpPath 如果源目录与目标目录是多对一关系，则不为空，否则设为空
         */
        private CopyTask(String taskId, String backUpPath) {
            this.taskId = taskId;
            this.syncAllResolver = applicationContext.getBean("SyncAllResolver", SyncAllResolver.class);
            this.syncDiffResolver = applicationContext.getBean("RsyncResolver", RsyncResolver.class);
            // TODO
            syncAllResolver.setTaskId(taskId).setBackUpPath(backUpPath);
            syncDiffResolver.setTaskId(taskId).setBackUpPath(backUpPath);
            packs = new ArrayList<>(30);
            lock = new Object();
            pause = false;
            shutdown = false;
        }

        /**
         * 根据解析器种类来选择不同的解析器。
         */
        @Override
        public void run() {
            Thread.currentThread().setName(Thread.currentThread().getName() + taskId);
            tempTime = System.nanoTime();
            byte[] pack;
            while (true) {
                synchronized (lock) {
                    if (!packs.isEmpty()) {
                        pack = packs.get(0);
                        packs.remove(0);
                    } else {
                        execTime += (System.nanoTime() - tempTime);
                        logger.info(wait.incrementAndGet() + "'s wait when pack null.");
                        pause = true;
                        try {
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
                // 当前任务已完成解析
                if (resolve(pack)) {
                    synchronized (lock) {
                        if (!packs.isEmpty()) {
                            throw new IllegalStateException("Program error. Task Done but "
                                    + "Pack size: " + packs.size());
                        }
                    }
                    break;
                }
            }
            this.close();
        }

        private boolean resolve(byte[] pack) {
            try {
                if (Constants.PackerType.SYNC_ALL_JAVA.getValue() == pack[SyncAllPacker.HEAD_LENGTH - 1]) {
                    return syncAllResolver.process(pack);
                } else {
                    return syncDiffResolver.process(pack);
                }
            } catch (IOException e) {
                throw new IllegalStateException("ERROR 0x01 : IO error.", e);
            } catch (InfoPersistenceException e) {
                throw new IllegalStateException("ERROR 0x02 : Persistence error.", e);
            }
        }

        private void addPack(byte[] pack) {
            Objects.requireNonNull(pack);
            synchronized (lock) {
                if (shutdown) {
                    return;
                }
                this.packs.add(pack);
                if (pause) {
                    logger.info(notify.incrementAndGet() + "'s notify when pack arrive.");
                    lock.notify();
                    pause = false;
                    tempTime = System.nanoTime();
                }
            }
        }

        /**
         * 会将尝试停止当前任务，并从任务执行 Map中移除该任务
         */
        @Override
        public void close() {
            synchronized (lock) {
                if (shutdown) {
                    return;
                }
                packs.clear();
                shutdown = true;
            }
            synchronized (ServerFileCopy.this.lock) {
                ServerFileCopy.this.execTaskMap.remove(this.taskId);
            }
            logger.info("Task resolver done. Use " + execTime + ".");
        }
    }

    private String takeTaskId(byte[] data) {
        return new String(data, 0, InitCopyContext.UUID_LEN, InitCopyContext.CHARSET);
    }
}
