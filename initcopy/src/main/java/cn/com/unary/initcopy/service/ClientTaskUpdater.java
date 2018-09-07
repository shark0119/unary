package cn.com.unary.initcopy.service;

import api.UnaryTransferClient;
import cn.com.unary.initcopy.InitCopyContext;
import cn.com.unary.initcopy.common.AbstractLoggable;
import cn.com.unary.initcopy.dao.FileManager;
import cn.com.unary.initcopy.entity.*;
import cn.com.unary.initcopy.exception.TaskFailException;
import cn.com.unary.initcopy.filecopy.ClientFileCopy;
import cn.com.unary.initcopy.grpc.client.ControlTaskGrpcClient;
import cn.com.unary.initcopy.grpc.constant.ModifyType;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
        // 停止源端同步任务
        try {
            fileCopy.updateTask(task.getTaskId(), Constants.UpdateType.PAUSE);
        } catch (IOException e) {
            throw new TaskFailException(e);
        }
        // 通知目标端删除任务
        SyncTargetDO targetInfo = fm.queryTask(task.getTaskId()).getTargetInfo();
        ControlTaskGrpcClient client = new ControlTaskGrpcClient(targetInfo.getIp(), context.getInnerGrpcPort());
        final ExecResultDO execResultDO = client.invokeGrpcDelete(task);
        // 删除源端任务相关信息
        if (execResultDO.isHealthy()) {
            fm.deleteTask(task.getTaskId());
        }
        return execResultDO;
    }

    public ExecResultDO modify(ModifyTaskDO task) throws TaskFailException {
        Constants.UpdateType updateType;
        switch (task.getModifyType()) {
            case SPEED_LIMIT:
                @NonNull UnaryTransferClient client = fileCopy.getTransferMap().get(task.getTaskId());
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
        try {
            fileCopy.updateTask(task.getTaskId(), updateType);
        } catch (IOException e) {
            throw new TaskFailException(e);
        }
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
        progress.setProgress(0);
        progress.setTotalFileNum(fis.size());
        progress.setSyncedFileNum(syncedFileNum);
        progress.setSyncedFileSize(syncedFileSize);
        progress.setSyncingFileName(syncingFileName);
        progress.setTotalFileSize(taskDO.getTotalSize());
        progress.setStage((int) (fis.size() / syncedFileNum));
        ExecResultDO execResultDO = new ExecResultDO(true, 0, "");
        return new TaskStateDO(taskId, execResultDO, progress);
    }
}
