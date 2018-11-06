package cn.com.unary.initcopy.grpc.service;

import cn.com.unary.initcopy.common.Msg;
import cn.com.unary.initcopy.common.utils.CommonUtils;
import cn.com.unary.initcopy.common.utils.ValidateUtils;
import cn.com.unary.initcopy.grpc.InitCopyGrpc;
import cn.com.unary.initcopy.grpc.entity.DeleteTask;
import cn.com.unary.initcopy.grpc.entity.ExecResult;
import cn.com.unary.initcopy.grpc.entity.ModifyTask;
import cn.com.unary.initcopy.grpc.entity.QueryTask;
import cn.com.unary.initcopy.grpc.entity.SyncTask;
import cn.com.unary.initcopy.grpc.entity.TaskState;
import cn.com.unary.initcopy.service.filecopy.ClientFileCopy;
import cn.com.unary.initcopy.service.taskupdater.ClientTaskUpdater;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 初始化复制 GRPC 服务实现类
 * <p>
 * 1. 转发任务到具体业务逻辑
 * 2. 参数合法校验
 * 3. 会处理下层抛出的所有异常。
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Component("InitCopyGrpcImpl")
@Scope("singleton")
public class InitCopyGrpcService extends InitCopyGrpc.InitCopyImplBase {

    @Autowired
    private ClientFileCopy fileCopy;
    @Autowired
    private ClientTaskUpdater updater;

    @Override
    public void add(SyncTask request, StreamObserver<ExecResult> responseObserver) {
        CommonUtils.logGrpcEntity(logger, request);
        ExecResult result = this.addLinker(request);
        CommonUtils.logGrpcEntity(logger, result);
        responseObserver.onNext(result);
        responseObserver.onCompleted();
    }

    @Override
    public void query(QueryTask request, StreamObserver<TaskState> responseObserver) {
        CommonUtils.logGrpcEntity(logger, request);
        TaskState result = this.queryLinker(request);
        CommonUtils.logGrpcEntity(logger, result);
        responseObserver.onNext(result);
        responseObserver.onCompleted();
    }

    @Override
    public void delete(DeleteTask request, StreamObserver<ExecResult> responseObserver) {
        CommonUtils.logGrpcEntity(logger, request);
        ExecResult result = this.deleteLinker(request);
        CommonUtils.logGrpcEntity(logger, result);
        responseObserver.onNext(result);
        responseObserver.onCompleted();
    }

    @Override
    public void modify(ModifyTask request, StreamObserver<ExecResult> responseObserver) {
        CommonUtils.logGrpcEntity(logger, request);
        ExecResult result = this.modifyLinker(request);
        CommonUtils.logGrpcEntity(logger, result);
        responseObserver.onNext(result);
        responseObserver.onCompleted();
    }

    private ExecResult addLinker(SyncTask task) {
        ExecResult.Builder builder = ExecResult.newBuilder();
        final int filesCount = task.getFilesCount();
        final int targetDirsCount = task.getTargetDirsCount();
        if (filesCount <= 0) {
            builder.setHealthy(false).setMsg("backup file can't be null.");
        } else if (targetDirsCount <= 0) {
            builder.setHealthy(false).setMsg("target dirs can't be null.");
        } else if (targetDirsCount != 1 && filesCount != targetDirsCount) {
            builder.setHealthy(false).setMsg(
                    String.format("bad backup dir map. fileCount:%d, targetDirsCount:%d",
                            filesCount, targetDirsCount));
        } else {
            if (ValidateUtils.isEmpty(task.getTaskId())) {
                task = task.toBuilder()
                        .setTaskId(UUID.randomUUID().toString())
                        .build();
            }
            builder.setTaskId(task.getTaskId());
            try {
                fileCopy.addTask(task);
                builder.setHealthy(true);
            } catch (Exception e) {
                logger.error(Msg.MSG_TASK_FAIL, e);
                builder.setHealthy(false);
                builder.setMsg(e.getMessage() == null ? "" : e.getMessage());
            }
        }
        return builder.build();
    }

    private TaskState queryLinker(QueryTask task) {
        TaskState.Builder stateBuilder = TaskState.newBuilder();
        ExecResult.Builder resultBuilder = ExecResult.newBuilder();
        if (ValidateUtils.isEmpty(task.getTaskId())) {
            resultBuilder.setMsg(Msg.MSG_TASK_ID_NULL).setHealthy(false);
        } else {
            resultBuilder.setTaskId(task.getTaskId());
            try {
                stateBuilder = updater.query(task).toBuilder();
                resultBuilder.setMsg(Msg.MSG_TASK_SUCCESS).setHealthy(true);
            } catch (Exception e) {
                logger.error(Msg.MSG_TASK_FAIL, e);
                resultBuilder.setMsg(e.getMessage() == null ? "" : e.getMessage()).setHealthy(false);
            }
        }
        return stateBuilder.setExecResult(resultBuilder).build();
    }

    private ExecResult deleteLinker(DeleteTask task) {
        ExecResult.Builder resultBuilder = ExecResult.newBuilder();
        if (ValidateUtils.isEmpty(task.getTaskId())) {
            resultBuilder.setMsg(Msg.MSG_TASK_ID_NULL).setHealthy(false);
        } else {
            resultBuilder.setTaskId(task.getTaskId());
            try {
                updater.delete(task);
                resultBuilder.setHealthy(true).setMsg(Msg.MSG_TASK_SUCCESS);
            } catch (Exception e) {
                logger.error(Msg.MSG_TASK_FAIL, e);
                resultBuilder.setMsg(e.getMessage() == null ? "" : e.getMessage()).setHealthy(false);
            }
        }
        return resultBuilder.build();
    }

    private ExecResult modifyLinker(ModifyTask task) {
        ExecResult.Builder resultBuilder = ExecResult.newBuilder();
        if (ValidateUtils.isEmpty(task.getTaskId())) {
            resultBuilder.setMsg(Msg.MSG_TASK_ID_NULL).setHealthy(false);
        } else if (task.getModifyType() == null) {
            resultBuilder.setMsg("ModifyType can't be null").setHealthy(false);
        } else {
            resultBuilder.setTaskId(task.getTaskId());
            try {
                resultBuilder = updater.modify(task).toBuilder();
            } catch (Exception e) {
                logger.error(Msg.MSG_TASK_FAIL, e);
                resultBuilder.setMsg(e.getMessage() == null ? "" : e.getMessage()).setHealthy(false);
            }
        }
        return resultBuilder.build();
    }

}