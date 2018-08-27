package cn.com.unary.initcopy.filecopy;

import cn.com.unary.initcopy.dao.FileManager;
import cn.com.unary.initcopy.entity.Constants;
import cn.com.unary.initcopy.filecopy.fileresolver.Resolver;
import cn.com.unary.initcopy.filecopy.fileresolver.RsyncResolver;
import cn.com.unary.initcopy.filecopy.fileresolver.SyncAllResolver;
import cn.com.unary.initcopy.filecopy.init.ServerFileCopyInit;
import cn.com.unary.initcopy.grpc.entity.ClientInitReq;
import cn.com.unary.initcopy.grpc.entity.ServerInitResp;
import cn.com.unary.initcopy.utils.AbstractLogable;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
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

    private Map<Integer, CopyTask> execTaskMap = new HashMap<>();
    private Map<Integer, CopyTask> taskMap = new HashMap<>();
    private ReentrantLock lock = new ReentrantLock();

    /**
     * 先根据包信息来确定属于哪个任务。
     *
     * @param data 数据包
     */
    public void resolverPack(byte[] data) {
        int taskId = CommonUtils.byteArrayToInt(data);
        lock.lock();
        if (execTaskMap.containsKey(taskId)) {
            taskMap.get(taskId).addPack(data);
        } else {
            if (taskMap.containsKey(taskId)) {
                fileCopyExec.execute(new Thread(taskMap.get(taskId), ""+taskId));
                execTaskMap.put(taskId, taskMap.get(taskId));
            } else {
                logger.error("Task " + taskId + " not found;");
            }
        }
        lock.unlock();
    }

    public ServerInitResp startInit(ClientInitReq req) {
        try {
            CopyTask task = new CopyTask(req.getTaskId(), req.getTargetDir());
            lock.lock();
            taskMap.put(req.getTaskId(), task);
            lock.unlock();
            return init.startInit(req);
        } catch (Exception e) {
            ServerInitResp.Builder builder = ServerInitResp.newBuilder();
            builder.setMsg(e.getMessage());
            builder.setReady(false);
            logger.error("Grpc Server Internal Error.", e);
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
        private Integer index = 0;
        private Boolean pause = false;

        public CopyTask(int taskId, String targetDir) {
            this.taskId = taskId;
            this.syncAllResolver = context.getBean("SyncAllResolver", SyncAllResolver.class);
            this.syncDiffResolver = context.getBean("RsyncResolver", RsyncResolver.class);
            syncAllResolver.setBackupPath(targetDir);
            syncDiffResolver.setBackupPath(targetDir);
            packs = new ArrayList<>();
        }

        public int getTaskId() {
            return this.taskId;
        }

        /**
         * 根据解析器种类来选择不同的解析器。
         */
        @Override
        public void run() {
            byte[] pack = null;
            while (true) {
                synchronized (index) {
                    if (index < packs.size()) {
                        pack = packs.get(index);
                        index++;
                    }
                }
                if (pack == null) {
                    try {
                        if (index >= packs.size()) {
                            logger.debug("Index: " + index + ", pack size: " + packs.size());
                        } else {
                            logger.error("Index: " + index + ", pack size: " + packs.size());
                            throw new IllegalStateException("Program error.");
                        }
                        synchronized (pause) {
                            pause.wait();
                        }
                    } catch (InterruptedException e) {
                        throw new IllegalStateException(e);
                    }
                }
                if (resolve(pack)) {
                    if (index >= packs.size()) {
                        logger.debug("Index: " + index + ", pack size: " + packs.size());
                    } else {
                        logger.error("Index: " + index + ", pack size: " + packs.size());
                        throw new IllegalStateException("Program error.");
                    }
                    break;
                } else {
                    if (index >= packs.size()) {
                        try {
                            logger.debug("Index: " + index + ", pack size: " + packs.size());
                            synchronized (pause) {
                                pause.wait();
                            }
                        } catch (InterruptedException e) {
                            throw new IllegalStateException(e);
                        }
                    }
                }
            }
        }

        private boolean resolve(byte[] pack) {
            if (Constants.PackType.SYNC_ALL_JAVA.equals(pack[8])) {
                return syncAllResolver.process(pack);
            } else {
                return syncDiffResolver.process(pack);
            }
        }

        public void addPack(byte[] pack) {
            synchronized (index) {
                this.packs.add(pack);

                synchronized (pause) {
                    pause.notify();
                }
            }
        }
    }
}
