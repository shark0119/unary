package cn.com.unary.initcopy.service.filecopy;

import cn.com.unary.initcopy.InitCopyContext;
import cn.com.unary.initcopy.common.AbstractLoggable;
import cn.com.unary.initcopy.common.ExecExceptionsHandler;
import cn.com.unary.initcopy.common.utils.BeanExactUtil;
import cn.com.unary.initcopy.common.utils.ValidateUtils;
import cn.com.unary.initcopy.dao.FileManager;
import cn.com.unary.initcopy.entity.FileInfoDO;
import cn.com.unary.initcopy.exception.InfoPersistenceException;
import cn.com.unary.initcopy.exception.TaskFailException;
import cn.com.unary.initcopy.grpc.entity.DiffFileInfo;
import cn.com.unary.initcopy.grpc.entity.ExecResult;
import cn.com.unary.initcopy.grpc.entity.SyncProcess;
import cn.com.unary.initcopy.grpc.entity.SyncTask;
import cn.com.unary.initcopy.service.filecopy.filepacker.Packer;
import cn.com.unary.initcopy.service.filecopy.filepacker.SyncDiffPacker;
import cn.com.unary.initcopy.service.filecopy.init.ClientFileCopyInit;
import cn.com.unary.initcopy.service.transmit.TransmitClientAdapter;
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

import static cn.com.unary.initcopy.entity.Constants.THREAD_STATE;

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
    /**
     * 所有正在执行和挂起的任务均存在此集合
     */
    private final Map<String, PackTask> execTaskMap;
    private final ThreadPoolExecutor exec;
    private volatile boolean close;
    @Autowired
    protected ClientFileCopyInit init;
    @Autowired
    @Qualifier("clientFM")
    private FileManager fm;
    @Setter
    private ApplicationContext applicationContext;
    private Map<String, TransmitClientAdapter> transferMap;

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

    public void speedLimit(String taskId, int speed) {
        // TransmitClientAdapter clientAdapter = transferMap.get(taskId)
        // TODO speed limit
    }

    public ExecResult resume(SyncProcess process) {
        PackTask packTask;
        ExecResult.Builder resultBuilder = ExecResult.newBuilder();
        synchronized (lock) {
            packTask = execTaskMap.get(process.getExecResult().getTaskId());
        }
        if (packTask.isActive()) {
            resultBuilder.setMsg(String.format("PackTask:%s is running, state:%s."
                    , packTask.toString(), packTask.threadState.toString()));
        } else {
            packTask.threadState = THREAD_STATE.READY;
            if (ValidateUtils.isEmpty(process.getFileId())) {
                resultBuilder = process.getExecResult().toBuilder();
                resultBuilder.setMsg("Server task resume failed. " + resultBuilder.getMsg());
            } else {
                fm.updatePreviousFilesToUnSync(process.getExecResult().getTaskId(), process.getFileId());
                packTask.serverSyncProcess = process;
                resultBuilder.setMsg(String.format("Task %s resume success.", packTask.taskId));
            }
            exec.execute(packTask);
        }
        return resultBuilder.setHealthy(true).build();
    }

    public void pause(String taskId) throws IOException {
        synchronized (lock) {
            final PackTask packTask = execTaskMap.get(taskId);
            if (packTask.isActive()) {
                packTask.pause();
            } else {
                logger.info(String.format("UnSupport operation to task:%s. State:%s."
                        , taskId, packTask.threadState.toString()));
            }
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
                    throw new TaskFailException("file copy already pause");
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
     * @param diffFileInfoList    差异文件数据集合
     */
    private void startDiffSync(TransmitClientAdapter unaryTransferClient, String taskId,
                               final List<DiffFileInfo> diffFileInfoList) {
        final SyncDiffPacker syncDiffPacker =
                applicationContext.getBean("RsyncPacker", SyncDiffPacker.class);
        syncDiffPacker.setFileDiffInfos(diffFileInfoList);
        final PackTask packTask = new PackTask(taskId, syncDiffPacker, unaryTransferClient);
        synchronized (lock) {
            PackTask oldTask = execTaskMap.put(taskId, packTask);
            if (oldTask != null) {
                logger.warn(String.format("Task %s be replaced", oldTask.toString()));
            }
            exec.execute(packTask);
        }
    }

    /**
     * 开启一个全复制的任务
     *
     * @param client 传输模块
     * @param taskId 任务Id作为当前线程名
     */
    private void startAllSync(TransmitClientAdapter client, String taskId) {
        final Packer syncAllPacker =
                applicationContext.getBean("SyncAllPacker", Packer.class);
        final PackTask packTask = new PackTask(taskId, syncAllPacker, client);
        synchronized (lock) {
            PackTask oldTask = execTaskMap.put(taskId, packTask);
            if (oldTask != null) {
                logger.warn(String.format("Task %s be replaced", oldTask.toString()));
            }
            exec.execute(packTask);
        }
    }

    @PreDestroy
    @Override
    public void close() throws IOException {
        synchronized (lock) {
            // 关闭已执行的任务
            for (String key : execTaskMap.keySet()) {
                execTaskMap.get(key).pause();
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

    class PackTask implements Runnable {

        private final String taskId;
        private final Packer packer;
        private final TransmitClientAdapter transfer;
        private volatile THREAD_STATE threadState;
        @Setter
        private SyncProcess serverSyncProcess;

        private PackTask(String taskId, Packer packer, TransmitClientAdapter transfer) {
            this.taskId = taskId;
            this.packer = packer;
            this.transfer = transfer;
            threadState = THREAD_STATE.READY;
        }

        @Override
        public void run() {
            Thread.currentThread().setName(Thread.currentThread().getName() + taskId);
            try {
                transfer.start(BeanExactUtil.takeTransmitParamFromTaskDO(fm.queryTask(taskId)));
                threadState = THREAD_STATE.RUNNING;
                packer.init(taskId, serverSyncProcess);
                serverSyncProcess = null;
                byte[] pack;
                while ((pack = packer.pack()) != null) {
                    logger.debug(String.format("Pass %d bytes to transfer.", pack.length));
                    this.transfer.sendData(pack);
                    if (!isActive()) {
                        break;
                    }
                }
            } catch (IOException | InfoPersistenceException e) {
                logger.error("Packer init error .", e);
                throw new IllegalStateException(e);
            } finally {
                if (threadState.equals(THREAD_STATE.SHUTDOWN)) {
                    logger.info(String.format("Task:%s suspend.", taskId));
                    try {
                        // 留作备用。当客户端的被停止，而服务端的任务还在运行时，按照客户端的复制进度来启动任务
                        serverSyncProcess = packer.close();
                    } catch (IOException e) {
                        logger.error("Packer close error.", e);
                    }
                } else {
                    logger.info(String.format("Task:%s shutdown, remove from task map.", taskId));
                    // 如果不是被暂停的任务，从任务 Map 中移除并关闭该任务
                    synchronized (ClientFileCopy.this.lock) {
                        execTaskMap.remove(taskId);
                        try {
                            this.close();
                        } catch (IOException e) {
                            logger.error("PackTask close error.", e);
                        }
                    }
                }
            }
        }

        public void pause() {
            if (!isActive()) {
                return;
            }
            threadState = THREAD_STATE.SHUTDOWN;
        }

        /**
         * 强制关闭，可能会导致 {@link Packer#pack()} 的异常退出
         *
         * @throws IOException IO 异常
         */
        public void close() throws IOException {
            if (THREAD_STATE.DEAD.equals(threadState)) {
                return;
            }
            packer.close();
            threadState = THREAD_STATE.DEAD;
        }

        boolean isActive() {
            return THREAD_STATE.RUNNING.equals(threadState)
                    || THREAD_STATE.READY.equals(threadState)
                    || THREAD_STATE.WAITING.equals(threadState);
        }

        @Override
        public String toString() {
            return "PackTask{" +
                    "taskId='" + taskId + '\'' +
                    ", threadState=" + threadState +
                    '}';
        }
    }

}
