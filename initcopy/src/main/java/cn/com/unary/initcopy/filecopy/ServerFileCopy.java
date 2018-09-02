package cn.com.unary.initcopy.filecopy;

import cn.com.unary.initcopy.entity.ClientInitReqDO;
import cn.com.unary.initcopy.entity.Constants;
import cn.com.unary.initcopy.entity.ServerInitRespDO;
import cn.com.unary.initcopy.exception.TaskException;
import cn.com.unary.initcopy.filecopy.filepacker.SyncAllPacker;
import cn.com.unary.initcopy.filecopy.fileresolver.Resolver;
import cn.com.unary.initcopy.filecopy.fileresolver.RsyncResolver;
import cn.com.unary.initcopy.filecopy.fileresolver.SyncAllResolver;
import cn.com.unary.initcopy.filecopy.init.ServerFileCopyInit;
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
 * 会处理所有下层抛出的异常
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

    /**
     * 已放入线程池中执行的任务
     */
    private Map<Integer, CopyTask> execTaskMap = new HashMap<>();
    /**
     * 任务列表
      */
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
     * @throws TaskException 任务初始化失败异常
     */
    public ServerInitRespDO startInit(ClientInitReqDO req) throws TaskException {
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
            ServerInitRespDO resp = new ServerInitRespDO();
            resp.setMsg(e.getMessage());
            resp.setReady(false);
            return resp;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    protected class CopyTask implements Runnable {
        private Resolver syncAllResolver;
        private Resolver syncDiffResolver;
        private final List<byte[]> packs;
        private int taskId;
        private final Object lock;
        private boolean pause;
        private AtomicInteger wait = new AtomicInteger(0);
        private AtomicInteger notify = new AtomicInteger(0);

        private CopyTask(int taskId, String targetDir) {
            this.taskId = taskId;
            this.syncAllResolver = context.getBean("SyncAllResolver", SyncAllResolver.class);
            this.syncDiffResolver = context.getBean("RsyncResolver", RsyncResolver.class);
            syncAllResolver.setBackupPath(targetDir).setTaskId(taskId);
            syncDiffResolver.setBackupPath(targetDir).setTaskId(taskId);
            packs = new ArrayList<>();
            lock = new Object();
            pause = false;
        }

        /**
         * 根据解析器种类来选择不同的解析器。
         */
        @Override
        public void run() {
            Thread.currentThread().setName(Thread.currentThread().getName()+taskId);
            long time = System.nanoTime();
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
                                logger.info(wait.getAndIncrement() +"'s wait when pack null.");
                                pause = true;
                                lock.wait();
                            }
                            continue;
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
                                    logger.info(wait.getAndIncrement() +"'s wait when packs' empty.");
                                    pause = true;
                                    lock.wait();
                                }
                            }
                        } catch (InterruptedException e) {
                            throw new IllegalStateException(e);
                        }
                    }
                }
            }
            logger.info("Task resolver done. Use " + (System.nanoTime() - time));
        }

        private boolean resolve(byte[] pack) {
            if (Constants.PackerType.SYNC_ALL_JAVA.getValue() == pack[SyncAllPacker.HEAD_LENGTH-1]) {
                return syncAllResolver.process(pack);
            } else {
                return syncDiffResolver.process(pack);
            }
        }

        private void addPack(byte[] pack) {
            Objects.requireNonNull(pack);
            synchronized (packs) {
                this.packs.add(pack);
                synchronized (lock) {
                    if (pause) {
                        logger.info(notify.getAndIncrement() +"'s notify when pack arrive.");
                        lock.notify();
                        pause = false;
                    }
                }
            }
        }
    }
}
