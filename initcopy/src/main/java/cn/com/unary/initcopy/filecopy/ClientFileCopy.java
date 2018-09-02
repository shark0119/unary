package cn.com.unary.initcopy.filecopy;

import api.UnaryTClient;
import cn.com.unary.initcopy.dao.FileManager;
import cn.com.unary.initcopy.entity.Constants;
import cn.com.unary.initcopy.entity.FileInfoDO;
import cn.com.unary.initcopy.exception.TaskException;
import cn.com.unary.initcopy.filecopy.filepacker.Packer;
import cn.com.unary.initcopy.filecopy.filepacker.SyncDiffPacker;
import cn.com.unary.initcopy.filecopy.init.ClientFileCopyInit;
import cn.com.unary.initcopy.grpc.constant.ModifyType;
import cn.com.unary.initcopy.grpc.entity.DiffFileInfo;
import cn.com.unary.initcopy.grpc.entity.SyncTask;
import cn.com.unary.initcopy.common.AbstractLogable;
import cn.com.unary.initcopy.utils.BeanConvertUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * 文件复制模块，负责与外部通讯。将内部三个模块与外部组合起来。
 * 线程安全，会将下层的所有可拦截异常全部捕获。
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Component("ClientFileCopy")
@Scope("singleton")
public class ClientFileCopy extends AbstractLogable implements ApplicationContextAware {

    @Autowired
    protected ClientFileCopyInit clientFileCopyInit;
    @Autowired
    @Qualifier("clientFM")
    protected FileManager fm;
    @Autowired
    @Qualifier("clientExecutor")
    protected ExecutorService exec;
    private ApplicationContext applicationContext;

    /**
     * 对任务进行删除、暂停、唤醒等操作
     * @param taskId 任务Id
     * @param modifyType 更新操作
     * @throws TaskException 任务异常
     */
    public void updateTask (int taskId, Constants.UpdateType modifyType) throws TaskException {
    }
    /**
     * 源端接收到添加任务指令后，开始初始化；
     * 初始化结束以后开始文件打包；线程安全
     *
     * @param syncTask 同步任务相关配置信息
     * @throws TaskException 任务添加失败，可能是 IO异常，可能是初始化异常，也可能是打包异常
     */
    public void addTask(SyncTask syncTask) throws TaskException {
        try {
            logger.debug("Start File Copy Init.");
            UnaryTClient unaryTClient = applicationContext.getBean(UnaryTClient.class);
            List<DiffFileInfo> diffFileInfos = new ArrayList<>();
            List<String> syncFileIds = clientFileCopyInit.startInit(unaryTClient, syncTask, diffFileInfos);
            logger.debug("Got " + diffFileInfos.size() + " diff file info and "
                    + syncFileIds.size() + " file ids.");

            logger.debug("Remove invalid file info. Complete file info from base file info.");
            List<FileInfoDO> list1 = fm.queryByTaskId(syncTask.getTaskId());
            Map<String, String> map = new HashMap<>(100);
            for (String id : syncFileIds) {
                map.put(id, id);
            }
            for (FileInfoDO fi : list1) {
                if (map.containsKey(fi.getId())) {
                    fm.save(BeanConvertUtil.readFromBaseFileInfo(fi));
                } else {
                    fm.delete(fi.getId());
                }
            }

            logger.debug("Try Start A Sync Task. Pack And Send File.");
            // 根据不同的同步方式来进行使用对应的打包策略
            switch (syncTask.getSyncType()) {
                // 差异复制
                case SYNC_DIFF:
                    startDiffSync(unaryTClient, syncTask.getTaskId(), syncFileIds, diffFileInfos);
                    break;
                case SYNC_ALL:
                default:
                    startAllSync(unaryTClient, syncTask.getTaskId(), syncFileIds);
                    break;
            }
        } catch (IOException e) {
            throw new TaskException(e);
        }
    }

    /**
     * 开启一个差异复制的任务
     *
     * @param unaryTClient  传输模块
     * @param taskId        任务Id作为当前线程名
     * @param syncFileIds   待同步的文件Id集合
     * @param diffFileInfos 差异文件数据集合
     */
    private void startDiffSync(UnaryTClient unaryTClient, int taskId,
                               final List<String> syncFileIds,
                               final List<DiffFileInfo> diffFileInfos) {
        final SyncDiffPacker syncDiffPacker =
                applicationContext.getBean("RsyncPacker", SyncDiffPacker.class).setTaskId(taskId);
        syncDiffPacker.setFileDiffInfos(diffFileInfos).setTransfer(unaryTClient);
        exec.execute(new PackTask(taskId, syncDiffPacker, syncFileIds));
    }

    /**
     * 开启一个全复制的任务
     *
     * @param unaryTClient 传输模块
     * @param taskId       任务Id作为当前线程名
     * @param syncFileIds  待同步的文件ID
     */
    private void startAllSync(UnaryTClient unaryTClient, int taskId, final List<String> syncFileIds) {
        final Packer syncAllPacker =
                applicationContext.getBean("SyncAllPacker", Packer.class).setTaskId(taskId);
        syncAllPacker.setTransfer(unaryTClient);
        exec.execute(new PackTask(taskId, syncAllPacker, syncFileIds));
    }

    private void recordPackerError(Exception e) {
        logger.error("Exception happened .", e);
        throw new IllegalStateException(e);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    protected class PackTask implements Runnable {

        private final List<String> syncFileIds;
        private int taskId;
        private Packer packer;

        public PackTask(int taskId, Packer packer, List<String> syncFileIds){
            this.taskId = taskId;
            this.packer = packer;
            this.syncFileIds = syncFileIds;
        }

        @Override
        public void run() {
            Thread.currentThread().setName(Thread.currentThread().getName()+taskId);
            try {
                packer.start(syncFileIds);
            } catch (IOException e) {
                ClientFileCopy.this.recordPackerError(e);
            } catch (IllegalStateException ise) {
                ClientFileCopy.this.recordPackerError(ise);
            }
        }
    }
}
