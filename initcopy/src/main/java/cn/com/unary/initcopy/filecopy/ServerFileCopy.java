package cn.com.unary.initcopy.filecopy;

import cn.com.unary.initcopy.entity.Constants;
import cn.com.unary.initcopy.filecopy.fileresolver.Resolver;
import cn.com.unary.initcopy.filecopy.fileresolver.RsyncResolver;
import cn.com.unary.initcopy.filecopy.fileresolver.SyncAllResolver;
import cn.com.unary.initcopy.filecopy.init.ServerFileCopyInit;
import cn.com.unary.initcopy.grpc.entity.ClientInitReq;
import cn.com.unary.initcopy.grpc.entity.ServerInitResp;
import cn.com.unary.initcopy.common.AbstractLogable;
import cn.com.unary.initcopy.utils.CommonUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 目标端的文件复制模块
 * 线程安全
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Component("ServerFileCopy")
@Scope("singleton")
public class ServerFileCopy extends AbstractLogable implements ApplicationContextAware {

    private ApplicationContext context;
    @Autowired
    @Qualifier("serverExecutor")
    private ExecutorService fileCopyExec;
    @Autowired
    private ServerFileCopyInit init;

    // 已放入线程池中执行的任务
    private Map<Integer, CopyTask> execTaskMap = new HashMap<>();
    // 任务列表
    private Map<Integer, CopyTask> taskMap = new HashMap<>();
    private ReentrantLock lock = new ReentrantLock();

    /**
     * 先根据包信息来确定属于哪个任务。
     *
     * @param data 数据包
     */
    public void resolverPack(byte[] data) {
        int taskId = CommonUtils.byteArrayToInt(data, 0);
        lock.lock();
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
        lock.unlock();
    }

    /**
     * 收到初始化请求后，新建任务并放入任务列表中。
     *
     * @param req 初始化请求
     * @return 初始化响应
     */
    public ServerInitResp startInit(ClientInitReq req) {
        try {
            CopyTask task = new CopyTask(req.getTaskId(), req.getTargetDir());
            lock.lock();
            taskMap.put(req.getTaskId(), task);
            lock.unlock();
            return init.startInit(req);
        } catch (Exception e) {
            logger.error("Grpc Server Internal Error.", e);
            // 初始化失败后，移除当前任务
            lock.lock();
            taskMap.remove(req.getTaskId());
            lock.unlock();
            ServerInitResp.Builder builder = ServerInitResp.newBuilder();
            builder.setMsg(e.getMessage());
            builder.setReady(false);
            return builder.build();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    protected class CopyTask implements Runnable {
        private Resolver syncAllResolver;
        private Resolver syncDiffResolver;
        private List<byte[]> packs;
        private int taskId;
        private Object lock;
        private boolean pause;
        private AtomicInteger wait;
        private AtomicInteger notify;

        public CopyTask(int taskId, String targetDir) {
            this.taskId = taskId;
            this.syncAllResolver = context.getBean("SyncAllResolver", SyncAllResolver.class);
            this.syncDiffResolver = context.getBean("RsyncResolver", RsyncResolver.class);
            syncAllResolver.setBackupPath(targetDir).setTaskId(taskId);
            syncDiffResolver.setBackupPath(targetDir).setTaskId(taskId);
            packs = new ArrayList<>();
            lock = new Object();
            pause = false;
            wait = new AtomicInteger(0);
            notify = new AtomicInteger(0);
        }

        /**
         * 根据解析器种类来选择不同的解析器。
         */
        @Override
        public void run() {
            Thread.currentThread().setName(Thread.currentThread().getName()+taskId);
            byte[] pack = null;
            while (true) {
                synchronized (packs) {
                    if (!packs.isEmpty()) {
                        pack = packs.get(0);
                        packs.remove(0);
                    }
                }
                if (pack == null) {
                    try {
                        if (!packs.isEmpty()) {
                            throw new IllegalStateException("Program error."
                                    +"Pack size: " + packs.size());
                        }
                        synchronized (lock) {
                            if (!pause) {
                                logger.debug(wait.incrementAndGet() + "'s wait when pack's null !!!");
                                lock.wait();
                                pause = true;
                            }
                        }
                    } catch (InterruptedException e) {
                        throw new IllegalStateException(e);
                    }
                }
                // 当前任务已完成解析
                if (resolve(pack)) {
                    if (!packs.isEmpty()) {
                        throw new IllegalStateException("Program error. Task Done but "
                                +"Pack size: " + packs.size());
                    }
                    break;
                } else {
                    // 等待后续的包接收
                    if (packs.isEmpty()) {
                        try {
                            synchronized (lock) {
                                if (!pause) {
                                    logger.debug(wait.incrementAndGet() + "'s wait on packs' empty!!!");
                                    lock.wait();
                                    pause = true;
                                }
                            }
                        } catch (InterruptedException e) {
                            throw new IllegalStateException(e);
                        }
                    }
                }
            }
        }

        private boolean resolve(byte[] pack) {
            if (Constants.PackType.SYNC_ALL_JAVA.getValue() == pack[8]) {
                return syncAllResolver.process(pack);
            } else {
                return syncDiffResolver.process(pack);
            }
        }

        public void addPack(byte[] pack) {
            Objects.requireNonNull(pack);
            synchronized (packs) {
                this.packs.add(pack);
                synchronized (lock) {
                    if (pause) {
                        logger.debug(notify.incrementAndGet() + "'s notify!!!");
                        lock.notify();
                        pause = false;
                    }
                }
            }
        }
    }
}
