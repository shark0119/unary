package cn.com.unary.initcopy.grpc.service;

import cn.com.unary.initcopy.common.utils.CommonUtils;
import cn.com.unary.initcopy.common.utils.ValidateUtils;
import cn.com.unary.initcopy.grpc.ControlTaskGrpc;
import cn.com.unary.initcopy.grpc.entity.*;
import cn.com.unary.initcopy.service.filecopy.ServerFileCopy;
import cn.com.unary.initcopy.service.taskupdater.ServerTaskUpdater;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static cn.com.unary.initcopy.common.Msg.*;
/**
 * 任务控制 GRPC 服务实现类
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Component("ControlTaskGrpcService")
@Scope("singleton")
public class ControlTaskGrpcService extends ControlTaskGrpc.ControlTaskImplBase {


    @Autowired
    private ServerFileCopy fileCopy;
    @Autowired
    private ServerTaskUpdater taskUpdater;

    @Override
    public void init(cn.com.unary.initcopy.grpc.entity.ClientInitReq request,
                     StreamObserver<ServerInitResp> responseObserver) {
        CommonUtils.logGrpcEntity(logger, request);
        ServerInitResp resp = this.initLinker(request);
        CommonUtils.logGrpcEntity(logger, resp);
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }

    @Override
    public void delete(DeleteTask request, StreamObserver<ExecResult> responseObserver) {
        CommonUtils.logGrpcEntity(logger, request);
        ExecResult resp = this.deleteLinker(request);
        CommonUtils.logGrpcEntity(logger, resp);
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }

    @Override
    public void resume(ResumeTask request, StreamObserver<SyncProcess> responseObserver) {
        CommonUtils.logGrpcEntity(logger, request);
        SyncProcess resp = this.resumeLinker(request);
        CommonUtils.logGrpcEntity(logger, resp);
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }

    @Override
    public void query(QueryTask request, StreamObserver<TaskState> responseObserver) {
        CommonUtils.logGrpcEntity(logger, request);
        TaskState taskState = this.queryLinker(request);
        CommonUtils.logGrpcEntity(logger, taskState);
        responseObserver.onNext(taskState);
        responseObserver.onCompleted();
    }

    /***
     * 下面的方法为连接具体业务的代码。
     * ***/

    private TaskState queryLinker(QueryTask request) {
        TaskState.Builder builder = TaskState.newBuilder();
        ExecResult.Builder resultBuilder = ExecResult.newBuilder();
        if (ValidateUtils.isEmpty(request.getTaskId())) {
            builder.setExecResult(resultBuilder.setHealthy(false).setMsg(MSG_TASK_ID_NULL));
        } else {
            resultBuilder.setTaskId(request.getTaskId());
            try {
                builder = taskUpdater.query(request.getTaskId()).toBuilder();
                resultBuilder.setHealthy(true).setMsg(MSG_TASK_SUCCESS);
            } catch (Exception e) {
                logger.error(MSG_TASK_FAIL, e);
                resultBuilder.setHealthy(false).setMsg(e.getMessage() == null ? "" : e.getMessage());
            }
        }
        return builder.setExecResult(resultBuilder).build();
    }

    private ServerInitResp initLinker(ClientInitReq req) {
        ServerInitResp.Builder builder = ServerInitResp.newBuilder();
        ExecResult.Builder resultBuilder = ExecResult.newBuilder();
        if (req.getBaseFileInfosCount() <= 0) {
            resultBuilder.setHealthy(false).setMsg("Illegal request. No files.");
        } else if (ValidateUtils.isEmpty(req.getTaskId())) {
            resultBuilder.setHealthy(false).setMsg(MSG_TASK_ID_NULL);
        } else {
            resultBuilder.setTaskId(req.getTaskId());
            try {
                builder = fileCopy.startInit(req).toBuilder();
                resultBuilder.setHealthy(true).setMsg(MSG_TASK_SUCCESS);
            } catch (Exception e) {
                logger.error(MSG_TASK_FAIL, e);
                resultBuilder.setHealthy(false).setMsg(e.getMessage() == null ? "" : e.getMessage());
            }
        }
        return builder.setExecResult(resultBuilder).build();
    }

    private ExecResult deleteLinker(DeleteTask task) {
        if (ValidateUtils.isEmpty(task.getTaskId())) {
            throw new IllegalArgumentException(MSG_TASK_ID_NULL);
        }
        ExecResult.Builder builder = ExecResult.newBuilder();
        try {
            taskUpdater.delete(task);
            builder.setHealthy(true).setMsg(MSG_TASK_SUCCESS);
        } catch (Exception e) {
            logger.error(MSG_TASK_FAIL, e);
            builder.setHealthy(false).setMsg(e.getMessage() == null ? "" : e.getMessage());
        }
        return builder.build();
    }

    private SyncProcess resumeLinker(ResumeTask task) {
        Objects.requireNonNull(task);
        if (ValidateUtils.isEmpty(task.getTaskId())) {
            throw new IllegalArgumentException(MSG_TASK_ID_NULL);
        }
        SyncProcess.Builder builder = SyncProcess.newBuilder();
        ExecResult.Builder resultBuilder = ExecResult.newBuilder();
        try {
            taskUpdater.resume(task.getTaskId());
            resultBuilder.setHealthy(true).setMsg(MSG_TASK_SUCCESS);
        } catch (Exception e) {
            logger.error(MSG_TASK_FAIL, e);
            resultBuilder.setHealthy(false).setMsg(e.getMessage() == null ? "" : e.getMessage());
        }
        return builder.setExecResult(resultBuilder).build();
    }

}