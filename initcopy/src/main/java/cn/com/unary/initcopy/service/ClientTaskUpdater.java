package cn.com.unary.initcopy.service;

import api.UnaryTClient;
import cn.com.unary.initcopy.InitCopyContext;
import cn.com.unary.initcopy.common.AbstractLoggable;
import cn.com.unary.initcopy.dao.FileManager;
import cn.com.unary.initcopy.entity.*;
import cn.com.unary.initcopy.exception.TaskFailException;
import cn.com.unary.initcopy.filecopy.ClientFileCopy;
import cn.com.unary.initcopy.grpc.client.ControlTaskGrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 客户端的任务修改器
 * 线程安全
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Component("ClientTaskUpdater")
@Scope("singleton")
public class ClientTaskUpdater extends AbstractLoggable {

    @Autowired
    @Qualifier("clientFM")
    private FileManager fm;
    @Autowired
    private ClientFileCopy fileCopy;
    @Autowired
    private InitCopyContext context;

    public ExecResultDO delete(DeleteTaskDO task) throws TaskFailException {
        fileCopy.updateTask(task.getTaskId(), Constants.UpdateType.DELETE);
        SyncTargetDO targetInfo = fm.queryTask(task.getTaskId()).getTargetInfo();
        ControlTaskGrpcClient client = new ControlTaskGrpcClient(targetInfo.getIp(), context.getInnerGrpcPort());
        return client.invokeGrpcDelete(task);
    }

    public ExecResultDO modify(ModifyTaskDO task) throws TaskFailException {
        Constants.UpdateType updateType;
        switch (task.getModifyType()) {
            case SPEED_LIMIT:
                UnaryTClient client = fileCopy.getTransferMap().get(task.getTaskId());
                if (client == null) {
                    throw new TaskFailException("task doesn't exist");
                }
                client.setSpeedLimit(task.getSpeedLimit());
                return new ExecResultDO(true, 0, "");
            case START:
                updateType = Constants.UpdateType.RESUME;
                break;
            case PAUSE:
                // treat as default option
            default:
                updateType = Constants.UpdateType.PAUSE;
                break;
        }
        fileCopy.updateTask(task.getTaskId(), updateType);
        SyncTargetDO targetInfo = fm.queryTask(task.getTaskId()).getTargetInfo();
        ControlTaskGrpcClient client = new ControlTaskGrpcClient(targetInfo.getIp(), context.getInnerGrpcPort());
        return client.invokeGrpcModify(task);
    }

    public TaskStateDO query(int taskId) throws TaskFailException {
        List<FileInfoDO> fis = fm.queryByTaskId(taskId);
        SyncTaskDO taskDO = fm.queryTask(taskId);
        long syncedFileNum = 0L, syncedFileSize = 0L;
        String syncingFileName = null;
        for (FileInfoDO infoDO : fis) {
            switch (infoDO.getStateEnum()) {
                case WAIT:
                    break;
                case SYNCED:
                    syncedFileNum++;
                    syncedFileSize += infoDO.getFileSize();
                    break;
                case SYNCING:
                default:
                    if (syncingFileName != null) {
                        throw new TaskFailException("error state, more than one file in syncing state.");
                    }
                    syncingFileName = infoDO.getFullName();
                    break;
            }
        }
        ProgressInfoDO progress = new ProgressInfoDO();
        progress.setStage((int) (fis.size() / syncedFileNum));
        progress.setSyncedFileNum(syncedFileNum);
        progress.setSyncingFileName(syncingFileName);
        progress.setSyncedFileSize(syncedFileSize);
        progress.setTotalFileNum(fis.size());
        progress.setTotalFileSize(taskDO.getTotalSize());
        progress.setTotalFileNum(fis.size());
        ExecResultDO execResultDO = new ExecResultDO(true, 0, "");
        return new TaskStateDO(taskId, execResultDO, progress);
    }
}
