package cn.com.unary.initcopy.service.taskupdater;

import cn.com.unary.initcopy.common.AbstractLoggable;
import cn.com.unary.initcopy.dao.FileManager;
import cn.com.unary.initcopy.entity.SyncTaskDO;
import cn.com.unary.initcopy.exception.TaskFailException;
import cn.com.unary.initcopy.grpc.entity.*;
import cn.com.unary.initcopy.grpc.service.ControlTaskGrpcClientPool;
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

    public void delete(DeleteTask task) throws TaskFailException {
        // 停止源端同步任务
        try {
            fileCopy.pause(task.getTaskId());
        } catch (IOException e) {
            throw new TaskFailException(e);
        }
        // 通知目标端删除任务
        SyncTaskDO taskDO = fm.queryTask(task.getTaskId());
        if (taskDO == null) {
            throw new TaskFailException(String.format("Task not found:%s", task.getTaskId()));
        }
        ControlTaskGrpcClientPool.ControlTaskGrpcClient client =
                ControlTaskGrpcClientPool.getClient(taskDO.getIp(), taskDO.getGrpcPort());
        final ExecResult result = client.invokeGrpcDelete(task);
        // 删除源端任务相关信息
        if (result.getHealthy()) {
            fm.deleteFileInfoByTaskId(task.getTaskId());
        } else {
            throw new TaskFailException(String.format("Server update task fail. %s", result.getMsg()));
        }
    }

    public ExecResult modify(ModifyTask task) throws TaskFailException {
        SyncTaskDO taskDO = fm.queryTask(task.getTaskId());
        if (taskDO == null) {
            throw new TaskFailException(String.format("Task not found:%s", task.getTaskId()));
        }
        SyncProcess process;
        ExecResult.Builder resultBuilder = ExecResult.newBuilder().setHealthy(true);
        switch (task.getModifyType()) {
            case RESUME:
                ControlTaskGrpcClientPool.ControlTaskGrpcClient client =
                        ControlTaskGrpcClientPool.getClient(taskDO.getIp(), taskDO.getGrpcPort());
                ResumeTask resumeTask = ResumeTask.newBuilder().setTaskId(task.getTaskId()).build();
                process = client.invokeGrpcResume(resumeTask);
                if (!process.getExecResult().getHealthy()) {
                    logger.info(String.format("Server task resume fail. %s", process.getExecResult().getMsg()));
                    resultBuilder = process.getExecResult().toBuilder();
                } else {
                    resultBuilder = fileCopy.resume(process).toBuilder();
                }
                break;
            case PAUSE:
                try {
                    fileCopy.pause(task.getTaskId());
                    resultBuilder.setMsg(String.format("Task %s pause success.", task.getTaskId()));
                } catch (IOException e) {
                    throw new TaskFailException(String.format("Task %s pause failed.", task.getTaskId()));
                }
                break;
            case SPEED_LIMIT:
                fileCopy.speedLimit(task.getTaskId(), task.getSpeedLimit());
                resultBuilder.setMsg(String.format("Task %s speed limit success.", task.getTaskId()));
                break;
            default:
                break;
        }
        return resultBuilder.build();
    }

    public TaskState query(QueryTask task) throws TaskFailException {
        SyncTaskDO taskDO = fm.queryTask(task.getTaskId());
        if (taskDO == null) {
            throw new TaskFailException(String.format("Don't have information about task id:%s.", task.getTaskId()));
        }
        ControlTaskGrpcClientPool.ControlTaskGrpcClient client = ControlTaskGrpcClientPool.getClient(taskDO.getIp(), taskDO.getGrpcPort());
        return client.invokeGrpcQuery(task);
    }

}
