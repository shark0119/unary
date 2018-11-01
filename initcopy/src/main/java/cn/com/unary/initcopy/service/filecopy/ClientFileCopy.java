package cn.com.unary.initcopy.service.filecopy;

import cn.com.unary.initcopy.InitCopyContext;
import cn.com.unary.initcopy.common.AbstractLoggable;
import cn.com.unary.initcopy.common.ExecExceptionsHandler;
import cn.com.unary.initcopy.common.utils.BeanExactUtil;
import cn.com.unary.initcopy.dao.FileManager;
import cn.com.unary.initcopy.entity.FileInfoDO;
import cn.com.unary.initcopy.exception.InfoPersistenceException;
import cn.com.unary.initcopy.exception.TaskFailException;
import cn.com.unary.initcopy.grpc.constant.ModifyType;
import cn.com.unary.initcopy.grpc.entity.DiffFileInfo;
import cn.com.unary.initcopy.grpc.entity.SyncTask;
import cn.com.unary.initcopy.service.filecopy.filepacker.Packer;
import cn.com.unary.initcopy.service.filecopy.filepacker.SyncDiffPacker;
import cn.com.unary.initcopy.service.filecopy.init.ClientFileCopyInit;
import cn.com.unary.initcopy.service.transmit.TransmitClientAdapter;
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
import java.util.Set;
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
    private final ThreadPoolExecutor exec;
    @Setter
    private ApplicationContext applicationContext;
    @Getter
    private Map<String, TransmitClientAdapter> transferMap;
    private final Map<String, PackTask> execTaskMap;

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
    public void updateTask(String taskId, ModifyType modifyType) throws IOException {
        switch (modifyType) {
            case PAUSE:
                synchronized (lock) {
                    execTaskMap.get(taskId).close();
                }
                break;
            case RESUME:
                TransmitClientAdapter transmitClient = applicationContext.getBean(TransmitClientAdapter.class);
                startAllSync(transmitClient, taskId);
                break;
            case SPEED_LIMIT:
                // TransmitClientAdapter client = fileCopy.getTransferMap().get(task.getTaskId())
                // TODO speed limit
                return;
            default:
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
    public void addTask(SyncTask task) throws TaskFailException {
        try {
            fm.saveTask(BeanExactUtil.takeFromGrpc(task, null));
            synchronized (lock) {
                if (close) {
                    throw new TaskFailException("file copy already shutdown");
                }
            }
            logger.debug("Start File Copy Init.");
            TransmitClientAdapter transmitClient = applicationContext.getBean(TransmitClientAdapter.class);
            synchronized (lock) {
                transferMap.put(task.getTaskId(), transmitClient);
            }
            List<DiffFileInfo> diffFileInfoList = new ArrayList<>();
            Set<String> syncFileIds = init.startInit(task, diffFileInfoList);
            logger.debug(String.format("Got %d diff file info and %d file ids.",
                    diffFileInfoList.size(), syncFileIds.size()));
            List<FileInfoDO> list1 = fm.queryByTaskId(task.getTaskId());
            FileInfoDO infoDO;
            for (FileInfoDO fi : list1) {
                if (syncFileIds.contains(fi.getFileId())) {
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
                    startDiffSync(transmitClient, task.getTaskId(), diffFileInfoList);
                    break;
                case SYNC_ALL:
                default:
                    startAllSync(transmitClient, task.getTaskId());
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
     * @param diffFileInfoList       差异文件数据集合
     */
    private void startDiffSync(TransmitClientAdapter unaryTransferClient, String taskId,
                               final List<DiffFileInfo> diffFileInfoList) {
        final SyncDiffPacker syncDiffPacker =
                applicationContext.getBean("RsyncPacker", SyncDiffPacker.class);
        syncDiffPacker.setFileDiffInfos(diffFileInfoList);
        final PackTask packTask = new PackTask(taskId, syncDiffPacker, unaryTransferClient);
        synchronized (lock) {
            execTaskMap.put(taskId, packTask);
        }
        exec.execute(packTask);
    }

    /**
     * 开启一个全复制的任务
     *
     * @param client 传输模块
     * @param taskId              任务Id作为当前线程名
     */
    private void startAllSync(TransmitClientAdapter client, String taskId) {
        final Packer syncAllPacker =
                applicationContext.getBean("SyncAllPacker", Packer.class);
        final PackTask packTask = new PackTask(taskId, syncAllPacker, client);
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
            for (String key : execTaskMap.keySet()) {
                execTaskMap.get(key).close();
            }
            // 关闭相应的传输模块
            for (String key : transferMap.keySet()) {
                transferMap.get(key).close();
            }
            execTaskMap.clear();
            transferMap.clear();
            close = true;
        }
    }

    class PackTask implements Runnable, Closeable {

        private final String taskId;
        private final Packer packer;
        private final TransmitClientAdapter transfer;

        private PackTask(String taskId, Packer packer, TransmitClientAdapter transfer) {
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
                    logger.error("packer shutdown error, taskId:" + taskId, e);
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
