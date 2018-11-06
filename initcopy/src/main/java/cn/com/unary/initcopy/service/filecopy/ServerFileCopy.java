package cn.com.unary.initcopy.service.filecopy;

import cn.com.unary.initcopy.InitCopyContext;
import cn.com.unary.initcopy.common.AbstractLoggable;
import cn.com.unary.initcopy.common.ExecExceptionsHandler;
import cn.com.unary.initcopy.dao.FileManager;
import cn.com.unary.initcopy.entity.Constants;
import cn.com.unary.initcopy.exception.InfoPersistenceException;
import cn.com.unary.initcopy.exception.TaskFailException;
import cn.com.unary.initcopy.grpc.entity.ClientInitReq;
import cn.com.unary.initcopy.grpc.entity.ExecResult;
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
import static cn.com.unary.initcopy.entity.Constants.THREAD_STATE;

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
     * 存放待执行的任务列表，包括新建的任务与挂起的任务
     */
    private final Map<String, CopyTask> taskMap;
    /**
     * 已放入线程池中执行的任务
     */
    private final Map<String, CopyTask> execTaskMap;
    @Autowired
    @Qualifier("serverFM")
    private FileManager fm;
    @Autowired
    private ServerFileCopyInit init;
    @Setter
    private ApplicationContext applicationContext;
    private volatile boolean close;

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
     * 如已被关闭，则直接返回
     *
     * @param data 数据包
     */
    @Override
    public void handle(byte[] data) {
        if (close) {
            logger.warn("FileCopy already pause.");
            return;
        }
        String taskId = new String(data, 0, InitCopyContext.UUID_LEN, InitCopyContext.CHARSET);
        synchronized (lock) {
            if (close) {
                logger.warn("FileCopy already pause.");
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
        synchronized (lock) {
            if (close) {
                throw new TaskFailException("FileCopy already pause.");
            }
        }
        final CopyTask task = new CopyTask(req.getTaskId(), req.getBackUpPath());
        ServerInitResp resp;
        try {
            resp = init.startInit(req);
        } catch (InfoPersistenceException e) {
            throw new TaskFailException("init task failed.", e);
        }
        synchronized (lock) {
            final CopyTask oldTask = taskMap.put(req.getTaskId(), task);
            if (null != oldTask) {
                logger.warn(String.format("Task %s was replaced by %s", oldTask.toString(), task.toString()));
            }
        }
        return resp;
    }

    @PreDestroy
    @Override
    public void close() {
        synchronized (lock) {
            fileCopyExec.shutdownNow();
            taskMap.clear();
            for (Map.Entry<String, CopyTask> entry : execTaskMap.entrySet()) {
                entry.getValue().shutdownNow();
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
            task.shutdownNow();
            logger.info(String.format("Task delete:%s", task.toString()));
        }
    }

    /**
     * 检查在线程池中执行的任务列表，如果超时未接受到数据，
     * 默认为客户端断开连接，则暂停该任务
     */
    public void clean() {
        synchronized (lock) {
            Iterator<Map.Entry<String, CopyTask>> iterator = execTaskMap.entrySet().iterator();
            Map.Entry<String, CopyTask> entry;
            long currentTime = System.currentTimeMillis();
            while (iterator.hasNext()) {
                entry = iterator.next();
                if (currentTime - entry.getValue().timeStamp > RESOURCE_TIME_OUT) {
                    entry.getValue().shutdown();
                }
            }
        }
    }

    /**
     * 将任务从休眠状态中唤醒
     *
     * @param taskId 任务 Id
     * @return 任务的复制进度，当任务正在执行时或者任务尚未开始时，返回 NULL
     */
    public SyncProcess resume(String taskId) {
        synchronized (lock) {
            CopyTask task = taskMap.get(taskId);
            ExecResult.Builder resultBuilder = ExecResult.newBuilder().setTaskId(taskId);
            SyncProcess.Builder processBuilder = SyncProcess.newBuilder();
            if (task == null) {
                if (execTaskMap.containsKey(taskId)) {
                    resultBuilder
                            .setMsg(String.format("Task %s is running on server", taskId))
                            .setHealthy(true);
                } else {
                    resultBuilder
                            .setMsg(String.format("Task %s not found in server. May have been completed. ", taskId))
                            .setHealthy(false);
                }
            } else {
                task.threadState = THREAD_STATE.READY;
                if (task.syncProcess == null) {
                    resultBuilder
                            .setMsg(String.format("Task %s is ready on server. No syncProcess found.", taskId))
                            .setHealthy(true);
                } else {
                    resultBuilder
                            .setMsg(String.format("Task %s resume success on server.", taskId))
                            .setHealthy(true);
                    processBuilder = task.syncProcess.toBuilder();
                }
            }
            return processBuilder.setExecResult(resultBuilder).build();
        }
    }

    class CopyTask implements Runnable {
        private final List<byte[]> packs;
        private final Object lock;
        private Resolver resolver;
        private SyncProcess syncProcess;
        private final String taskId, backUpPath;
        private final AtomicInteger tempWait = new AtomicInteger(0);
        private final AtomicInteger tempNotify = new AtomicInteger(0);
        private volatile THREAD_STATE threadState;
        /**
         * 资源被使用的最近时间点
         */
        private long timeStamp;
        /**
         * 用于任务计时
         */
        private long waitTime, tempTime, startTime;

        /**
         * @param taskId     任务Id
         * @param backUpPath 如果源目录与目标目录是多对一关系，则不为空，否则设为 null
         */
        private CopyTask(String taskId, String backUpPath) {
            lock = new Object();
            this.taskId = taskId;
            this.backUpPath = backUpPath;
            threadState = THREAD_STATE.READY;
            packs = new ArrayList<>(30);
            startTime = System.currentTimeMillis();
        }

        /**
         * 根据解析器种类来选择不同的解析器。
         */
        @Override
        public void run() {
            threadState = THREAD_STATE.RUNNING;
            try {
                if (resolver != null) {
                    resolver.resume();
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
            Thread.currentThread().setName(Thread.currentThread().getName() + taskId);
            byte[] pack;
            boolean taskFinished = false;
            while (true) {
                synchronized (lock) {
                    if (!packs.isEmpty()) {
                        pack = packs.get(0);
                        packs.remove(0);
                    } else {
                        if (THREAD_STATE.SHUTDOWN.equals(threadState)) {
                            logger.info(String.format("CopyTask:%s, paused.", taskId));
                            break;
                        }
                        try {
                            logger.info(String.format("%d's wait when pack null, state：%s."
                                    , tempWait.incrementAndGet(), threadState));
                            tempTime = System.currentTimeMillis();
                            threadState = THREAD_STATE.WAITING;
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new IllegalStateException(e);
                        }
                        continue;
                    }
                }
                if (resolve(pack)) {
                    logger.info(String.format("Task resolver done. WaitTime: %d. Total cost:%d.",
                            waitTime, System.currentTimeMillis() - startTime));
                    taskFinished = true;
                    break;
                }
            }
            this.shutdown();
            try {
                syncProcess = resolver.pause();
            } catch (IOException e) {
                syncProcess = null;
                logger.warn(String.format("Task %s. Resolver pause failed.", taskId), e);
            }
            synchronized (ServerFileCopy.this.lock) {
                // 对于 threadState 和 TASK_MAP 同时访问时，必须获取到 ServerFileCopy.this.lock
                threadState = THREAD_STATE.DEAD;
                if (taskFinished) {
                    ServerFileCopy.this.execTaskMap.remove(taskId);
                    logger.info(String.format("Task %s finished, remove from map.", taskId));
                } else {
                    ServerFileCopy.this.taskMap.put(taskId,
                            ServerFileCopy.this.execTaskMap.remove(taskId));
                }
            }
        }

        /**
         * 处理数据包
         *
         * @param pack 数据包
         * @return 任务结束返回 True
         */
        private boolean resolve(byte[] pack) {
            try {
                if (fm.taskFinished(taskId)) {
                    throw new IllegalStateException("Task already finished.");
                }
                // init resolver when first pack arrive
                if (resolver == null) {
                    if (Constants.PackerType.SYNC_ALL_JAVA.getValue() == pack[SyncAllPacker.HEAD_LENGTH - 1]) {
                        resolver = applicationContext.getBean(SyncAllResolver.class);
                    } else {
                        resolver = applicationContext.getBean(RsyncResolver.class);
                    }
                    resolver.init(taskId, backUpPath);
                }
                resolver.process(pack);
                return fm.taskFinished(taskId);
            } catch (IOException e) {
                throw new IllegalStateException("ERROR 0x01 : IO error.", e);
            } catch (InfoPersistenceException e) {
                throw new IllegalStateException("ERROR 0x02 : Persistence error.", e);
            }
        }

        private void addPack(byte[] pack) {
            synchronized (lock) {
                if (!isActive()) {
                    return;
                }
                timeStamp = System.currentTimeMillis();
                this.packs.add(pack);
                if (THREAD_STATE.WAITING.equals(threadState)) {
                    logger.info(tempNotify.incrementAndGet() + "'s notify when pack arrive.");
                    waitTime += System.currentTimeMillis() - tempTime;
                    threadState = THREAD_STATE.RUNNING;
                    lock.notify();
                }
            }
        }

        /**
         * 会将尝试停止当前任务
         *
         * @param lazy 当参数为 true 会将当前数据包队列处理完后再关闭。否则，清空当前数据队列，立即关闭。
         * */
        private void shutdown(boolean lazy) {
            if (!isActive()) {
                return;
            }
            if (!isActive()) {
                return;
            }
            // 如果当前线程正在等待数据包
            if (THREAD_STATE.WAITING.equals(threadState)) {
                threadState = THREAD_STATE.SHUTDOWN;
                logger.info(tempNotify.incrementAndGet() + "'s notify when pack shutdown.");
                waitTime += System.currentTimeMillis() - tempTime;
                synchronized (lock) {
                    lock.notify();
                }
            }
            threadState = THREAD_STATE.SHUTDOWN;
            // 直接清空即可，无需获取锁
            if (!lazy) {
                packs.clear();
            }
        }

        void shutdown() {
            this.shutdown(true);
        }

        void shutdownNow() {
            this.shutdown(false);
        }

        boolean isActive() {
            return THREAD_STATE.READY.equals(threadState)
                    || THREAD_STATE.RUNNING.equals(threadState)
                    || THREAD_STATE.WAITING.equals(threadState);
        }
    }

}
