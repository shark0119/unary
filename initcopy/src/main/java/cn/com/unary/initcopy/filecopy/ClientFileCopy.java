package cn.com.unary.initcopy.filecopy;

import api.UnaryTransferClient;
import cn.com.unary.initcopy.InitCopyContext;
import cn.com.unary.initcopy.common.AbstractLoggable;
import cn.com.unary.initcopy.common.ExecExceptionsHandler;
import cn.com.unary.initcopy.common.utils.BeanExactUtil;
import cn.com.unary.initcopy.dao.FileManager;
import cn.com.unary.initcopy.entity.Constants;
import cn.com.unary.initcopy.entity.FileInfoDO;
import cn.com.unary.initcopy.exception.InfoPersistenceException;
import cn.com.unary.initcopy.exception.TaskFailException;
import cn.com.unary.initcopy.filecopy.filepacker.Packer;
import cn.com.unary.initcopy.filecopy.filepacker.SyncDiffPacker;
import cn.com.unary.initcopy.filecopy.init.ClientFileCopyInit;
import cn.com.unary.initcopy.grpc.entity.DiffFileInfo;
import cn.com.unary.initcopy.grpc.entity.SyncTask;
import lombok.Getter;
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 文件复制模块，负责与外部通讯。将内部三个模块与外部组合起来。
 * 线程安全
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Component("ClientFileCopy")
@Scope("singleton")
public class ClientFileCopy extends AbstractLoggable implements ApplicationContextAware, Closeable {

    private final Object lock;
    @Autowired
    protected ClientFileCopyInit init;
    @Autowired
    @Qualifier("clientFM")
    private FileManager fm;
    private boolean close;
    private ThreadPoolExecutor exec;
    @Setter
    private ApplicationContext applicationContext;
    @Getter
    private Map<Integer, UnaryTransferClient> transferMap;
    private Map<Integer, PackTask> execTaskMap;

    public ClientFileCopy() {
        ThreadFactory executorThreadFactory = new BasicThreadFactory.Builder()
                .namingPattern("client-%d-packer-")
                .uncaughtExceptionHandler(new ExecExceptionsHandler(this))
                .build();
        exec = new ThreadPoolExecutor(InitCopyContext.TASK_NUMBER,
                200, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024),
                executorThreadFactory,
                new ThreadPoolExecutor.AbortPolicy());
        close = false;
        lock = new Object();
        transferMap = new HashMap<>(InitCopyContext.TASK_NUMBER);
        execTaskMap = new HashMap<>(InitCopyContext.TASK_NUMBER);
    }

    /**
     * 对任务进行 暂停、唤醒 操作
     *
     * @param taskId     任务Id
     * @param modifyType 更新操作
     * @throws IOException 任务关闭异常
     */
    public void updateTask(int taskId, Constants.UpdateType modifyType) throws IOException {
        switch (modifyType) {
            case PAUSE:
                synchronized (lock) {
                    execTaskMap.get(taskId).close();
                }
                break;
            case RESUME:
            default:
                SyncTask task = fm.queryTask(taskId);
                UnaryTransferClient unaryTransferClient = applicationContext.getBean(UnaryTransferClient.class);
                unaryTransferClient.setCompressType(task.getCompressType());
                unaryTransferClient.setEncryptType(task.getEncryptType());
                unaryTransferClient.setSpeedLimit(task.getSpeedLimit());
                startAllSync(unaryTransferClient, taskId);
                break;
        }
    }

    /**
     * 源端接收到添加任务指令后，开始初始化；
     * 初始化结束以后开始文件打包；线程安全
     *
     * @param task 同步任务相关配置信息
     * @throws TaskFailException 任务添加失败，可能是 IO异常，可能是初始化异常，也可能是打包异常
     */
    public void addTask(cn.com.unary.initcopy.grpc.entity.SyncTask task) throws TaskFailException {
        try {
            synchronized (lock) {
                if (close) {
                    throw new TaskFailException("file copy already shutdown");
                }
            }
            logger.debug("Start File Copy Init.");
            UnaryTransferClient unaryTransferClient = applicationContext.getBean(UnaryTransferClient.class);
            synchronized (lock) {
                transferMap.put(task.getTaskId(), unaryTransferClient);
            }
            List<DiffFileInfo> diffFileInfos = new ArrayList<>();
            List<String> syncFileIds = init.startInit(unaryTransferClient, task, diffFileInfos);
            logger.debug("Got " + diffFileInfos.size() + " diff file info and " + syncFileIds.size() + " file ids.");
            logger.debug("Remove invalid file info. Complete file info from base file info.");
            List<FileInfoDO> list1 = fm.queryByTaskId(task.getTaskId());
            Map<String, String> map = new HashMap<>(100);
            for (String id : syncFileIds) {
                map.put(id, "");
            }
            FileInfoDO infoDO;
            for (FileInfoDO fi : list1) {
                if (map.containsKey(fi.getFileId())) {
                    infoDO = BeanExactUtil.readFromBaseFileInfo(fi);
                    infoDO.setTaskId(task.getTaskId());
                    fm.save(infoDO);
                } else {
                    fm.deleteByIds(fi.getFileId());
                }
            }
            logger.debug("Try Start A Sync Task. Pack And Send File.");
            // 根据不同的同步方式来进行使用对应的打包策略
            switch (task.getSyncType()) {
                // 差异复制
                case SYNC_DIFF:
                    startDiffSync(unaryTransferClient, task.getTaskId(), diffFileInfos);
                    break;
                case SYNC_ALL:
                default:
                    startAllSync(unaryTransferClient, task.getTaskId());
                    break;
            }
        } catch (IOException | InfoPersistenceException e) {
            throw new TaskFailException(e);
        } finally {
            synchronized (lock) {
                transferMap.remove(task.getTaskId());
            }
        }
    }

    /**
     * 开启一个差异复制的任务
     *
     * @param unaryTransferClient 传输模块
     * @param taskId              任务Id作为当前线程名
     * @param diffFileInfos       差异文件数据集合
     */
    private void startDiffSync(UnaryTransferClient unaryTransferClient, int taskId,
                               final List<DiffFileInfo> diffFileInfos) {
        final SyncDiffPacker syncDiffPacker =
                applicationContext.getBean("RsyncPacker", SyncDiffPacker.class);
        syncDiffPacker.setFileDiffInfos(diffFileInfos);
        final PackTask packTask = new PackTask(taskId, syncDiffPacker, unaryTransferClient);
        synchronized (lock) {
            execTaskMap.put(taskId, packTask);
        }
        exec.execute(packTask);
    }

    /**
     * 开启一个全复制的任务
     *
     * @param unaryTransferClient 传输模块
     * @param taskId              任务Id作为当前线程名
     */
    private void startAllSync(UnaryTransferClient unaryTransferClient, int taskId) {
        final Packer syncAllPacker =
                applicationContext.getBean("SyncAllPacker", Packer.class);
        final PackTask packTask = new PackTask(taskId, syncAllPacker, unaryTransferClient);
        synchronized (lock) {
            execTaskMap.put(taskId, packTask);
        }
        exec.execute(packTask);
    }

    @PreDestroy
    @Override
    public void close() throws IOException {
        synchronized (lock) {
            // 关闭已执行的任务
            for (Integer key : execTaskMap.keySet()) {
                execTaskMap.get(key).close();
            }
            // 关闭相应的传输模块
            for (Integer key : transferMap.keySet()) {
                transferMap.get(key).stopClient();
            }
            execTaskMap.clear();
            transferMap.clear();
            close = true;
        }
    }

    protected class PackTask implements Runnable, Closeable {

        private int taskId;
        private Packer packer;
        private UnaryTransferClient transfer;

        private PackTask(int taskId, Packer packer, UnaryTransferClient transfer) {
            this.taskId = taskId;
            this.packer = packer;
            this.transfer = transfer;
        }

        @Override
        public void run() {
            Thread.currentThread().setName(Thread.currentThread().getName() + taskId);
            try {
                packer.start(taskId, transfer);
            } catch (IOException | InfoPersistenceException e) {
                logger.error("Exception happened .", e);
                throw new IllegalStateException(e);
            } finally {
                try {
                    this.close();
                } catch (IOException e) {
                    logger.error("packer close error, taskId:" + taskId, e);
                }
            }
        }

        @Override
        public void close() throws IOException {
            synchronized (ClientFileCopy.this.lock) {
                execTaskMap.remove(taskId);
            }
            packer.close();
        }
    }
}
