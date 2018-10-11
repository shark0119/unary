package cn.com.unary.initcopy.service;

import api.UnaryTransferClient;
import cn.com.unary.initcopy.InitCopyContext;
import cn.com.unary.initcopy.common.AbstractLoggable;
import cn.com.unary.initcopy.common.utils.ValidateUtils;
import cn.com.unary.initcopy.dao.FileManager;
import cn.com.unary.initcopy.entity.Constants;
import cn.com.unary.initcopy.entity.FileInfoDO;
import cn.com.unary.initcopy.exception.TaskFailException;
import cn.com.unary.initcopy.filecopy.ClientFileCopy;
import cn.com.unary.initcopy.grpc.client.ControlTaskGrpcClient;
import cn.com.unary.initcopy.grpc.entity.*;
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

    public void delete(DeleteTask task) throws TaskFailException {
        // 停止源端同步任务
        try {
            fileCopy.updateTask(task.getTaskId(), Constants.UpdateType.PAUSE);
        } catch (IOException e) {
            throw new TaskFailException(e);
        }
        // 通知目标端删除任务
        SyncTarget targetInfo = fm.queryTask(task.getTaskId()).getTargetInfo();
        ControlTaskGrpcClient client = new ControlTaskGrpcClient(targetInfo.getIp(), context.getInnerGrpcPort());
        final ExecResult result = client.invokeGrpcDelete(task);
        // 删除源端任务相关信息
        if (result.getHealthy()) {
            fm.deleteTask(task.getTaskId());
        } else {
            throw new TaskFailException(String.format("Server update task fail. %s", result.getMsg()));
        }
    }

    public void modify(ModifyTask task) throws TaskFailException {
        Constants.UpdateType updateType;
        switch (task.getModifyType()) {
            case SPEED_LIMIT:
                UnaryTransferClient client = fileCopy.getTransferMap().get(task.getTaskId());
                client.setSpeedLimit(task.getSpeedLimit());
                return;
            case START:
                updateType = Constants.UpdateType.RESUME;
                break;
            case PAUSE:
            default:
                updateType = Constants.UpdateType.PAUSE;
                break;
        }
        try {
            fileCopy.updateTask(task.getTaskId(), updateType);
        } catch (IOException e) {
            throw new TaskFailException("Client update task fail.", e);
        }
        SyncTarget targetInfo = fm.queryTask(task.getTaskId()).getTargetInfo();
        ControlTaskGrpcClient client = new ControlTaskGrpcClient(targetInfo.getIp(), context.getInnerGrpcPort());
        ExecResult result = client.invokeGrpcModify(task);
        if (!result.getHealthy()) {
            throw new TaskFailException(String.format("Server update task fail. %s", result.getMsg()));
        }
    }

    public TaskState query(String taskId) throws TaskFailException {
        List<FileInfoDO> fis = fm.queryByTaskId(taskId);
        SyncTask task = fm.queryTask(taskId);
        long syncedFileNum = 0L, syncedFileSize = 0L;
        String syncingFileName = "";
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
                    if (!ValidateUtils.isEmpty(syncingFileName)) {
                        throw new TaskFailException("error state, more than one file in syncing state.");
                    }
                    syncingFileName = infoDO.getFullName();
                    break;
            }
        }
        ProgressInfo.Builder progress = ProgressInfo.newBuilder();
        progress.setProgress(0);
        progress.setTotalFileNum(fis.size());
        progress.setSyncedFileNum(syncedFileNum);
        progress.setSyncedFileSize(syncedFileSize);
        progress.setSyncingFileName(syncingFileName);
        progress.setTotalFileSize(0L);
        if (syncedFileNum != 0) {
            progress.setStage((int) (fis.size() / syncedFileNum));
        } else {
            progress.setStage(0);
        }
        ExecResult.Builder result = ExecResult.newBuilder();
        result.setHealthy(true).setCode(0).setMsg("");
        TaskState.Builder builder = TaskState.newBuilder();
        // TODO
        progress.setSyncedFileNum(fis.size());
        progress.setSyncedFileSize(1000L);
        progress.setTotalFileSize(1000L);
        progress.setProgress(100);

        builder.setExecResult(result).setProgressInfo(progress);
        return builder.build();
    }

}
