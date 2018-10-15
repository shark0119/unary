package cn.com.unary.initcopy.service.taskupdater;

import api.UnaryTransferClient;
import cn.com.unary.initcopy.InitCopyContext;
import cn.com.unary.initcopy.common.AbstractLoggable;
import cn.com.unary.initcopy.dao.FileManager;
import cn.com.unary.initcopy.entity.Constants;
import cn.com.unary.initcopy.entity.SyncTaskDO;
import cn.com.unary.initcopy.exception.TaskFailException;
import cn.com.unary.initcopy.grpc.entity.DeleteTask;
import cn.com.unary.initcopy.grpc.entity.ExecResult;
import cn.com.unary.initcopy.grpc.entity.ModifyTask;
import cn.com.unary.initcopy.grpc.entity.QueryTask;
import cn.com.unary.initcopy.grpc.entity.TaskState;
import cn.com.unary.initcopy.grpc.service.ControlTaskGrpcClient;
import cn.com.unary.initcopy.service.filecopy.ClientFileCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

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
        SyncTaskDO taskDO = fm.queryTask(task.getTaskId());
        // @Cleanup
        ControlTaskGrpcClient client = new ControlTaskGrpcClient(taskDO.getIp(), context.getInnerGrpcPort());
        final ExecResult result = client.invokeGrpcDelete(task);
        // 删除源端任务相关信息
        if (result.getHealthy()) {
            fm.deleteFileInfoByTaskId(task.getTaskId());
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
        SyncTaskDO taskDO = fm.queryTask(task.getTaskId());
        // @Cleanup
        ControlTaskGrpcClient client = new ControlTaskGrpcClient(taskDO.getIp(), context.getInnerGrpcPort());
        ExecResult result = client.invokeGrpcModify(task);
        if (!result.getHealthy()) {
            throw new TaskFailException(String.format("Server update task fail. %s", result.getMsg()));
        }
    }

    public TaskState query(QueryTask task) throws TaskFailException {
        SyncTaskDO taskDO = fm.queryTask(task.getTaskId());
        if (taskDO == null) {
            throw new TaskFailException(String.format("Don't have information about task id:%s.", task.getTaskId()));
        }
        // @Cleanup
        ControlTaskGrpcClient client = new ControlTaskGrpcClient(taskDO.getIp(), taskDO.getGrpcPort());
        TaskState resp = client.invokeGrpcQuery(task);
        return resp;
    }

}
