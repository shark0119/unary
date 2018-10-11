package cn.com.unary.initcopy.grpc.service;

import cn.com.unary.initcopy.common.utils.CommonUtils;
import cn.com.unary.initcopy.common.utils.ValidateUtils;
import cn.com.unary.initcopy.filecopy.ServerFileCopy;
import cn.com.unary.initcopy.grpc.ControlTaskGrpc;
import cn.com.unary.initcopy.grpc.entity.ClientInitReq;
import cn.com.unary.initcopy.grpc.entity.DeleteTask;
import cn.com.unary.initcopy.grpc.entity.ExecResult;
import cn.com.unary.initcopy.grpc.entity.ModifyTask;
import cn.com.unary.initcopy.grpc.entity.ServerInitResp;
import cn.com.unary.initcopy.service.ServerTaskUpdater;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 任务控制 GRPC 服务实现类
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Component("ControlTaskGrpcImpl")
@Scope("singleton")
public class ControlTaskGrpcImpl extends ControlTaskGrpc.ControlTaskImplBase {

    private static final String TASK_SUCCESS_MSG = "Task success!";

    @Autowired
    private ServerFileCopy fileCopy;
    @Autowired
    private ServerTaskUpdater taskUpdater;

    @Override
    public void init(cn.com.unary.initcopy.grpc.entity.ClientInitReq request,
                     StreamObserver<ServerInitResp> responseObserver) {
        logger.info("ClientInitReq:" + CommonUtils.formatGrpcEntity(request));
        ServerInitResp resp = this.initLinker(request);
        logger.info("ServerInitResp:" + CommonUtils.formatGrpcEntity(resp));
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }

    @Override
    public void delete(DeleteTask request, StreamObserver<ExecResult> responseObserver) {
        logger.info("DeleteTask:" + CommonUtils.formatGrpcEntity(request));
        ExecResult result = this.deleteLinker(request);
        logger.info("ExecResult:" + CommonUtils.formatGrpcEntity(result));
        responseObserver.onNext(result);
        responseObserver.onCompleted();
    }

    @Override
    public void modify(ModifyTask request, StreamObserver<ExecResult> responseObserver) {
        logger.info("DeleteTask:" + CommonUtils.formatGrpcEntity(request));
        ExecResult result = this.modifyLinker(request);
        logger.info("ExecResult:" + CommonUtils.formatGrpcEntity(result));
        responseObserver.onNext(result);
        responseObserver.onCompleted();
    }

    /**
     * 调用 {@link cn.com.unary.initcopy.grpc.ControlTaskGrpc#METHODID_INIT}
     *
     * @param req 初始化请求
     * @return 初始化响应
     */
    private ServerInitResp initLinker(ClientInitReq req) {
        ServerInitResp.Builder builder = ServerInitResp.newBuilder().setTaskId(req.getTaskId());
        if (req.getBaseFileInfosCount() <= 0) {
            builder.setMsg("Illegal request. No files.").setReady(false);
        } else if (ValidateUtils.isEmpty(req.getTaskId())) {
            builder.setReady(false).setMsg("Illegal request. Task Id Null");
        } else {
            try {
                return fileCopy.startInit(req);
            } catch (Exception e) {
                logger.error("init fail", e);
                builder.setReady(false).setMsg(e.getMessage());
            }
        }
        return builder.build();
    }

    /**
     * 调用 {@link cn.com.unary.initcopy.grpc.ControlTaskGrpc#METHODID_DELETE}
     *
     * @param task 删除任务的相关参数
     * @return 执行结果
     */
    private ExecResult deleteLinker(DeleteTask task) {
        if (ValidateUtils.isEmpty(task.getTaskId())) {
            throw new IllegalArgumentException("task id can't be null.");
        }
        ExecResult.Builder builder = ExecResult.newBuilder();
        try {
            taskUpdater.delete(task);
            builder.setHealthy(true).setMsg(TASK_SUCCESS_MSG);
        } catch (Exception e) {
            logger.error("delete fail", e);
            builder.setHealthy(false).setMsg(e.getMessage());
        }
        return builder.build();
    }

    /**
     * 调用 {@link cn.com.unary.initcopy.grpc.ControlTaskGrpc#METHODID_MODIFY}
     *
     * @param task 修改任务的相关参数
     * @return 执行结果
     */
    private ExecResult modifyLinker(ModifyTask task) {
        Objects.requireNonNull(task);
        if (ValidateUtils.isEmpty(task.getTaskId())) {
            throw new IllegalArgumentException("task id can't be null.");
        }
        Objects.requireNonNull(task.getModifyType());
        ExecResult.Builder builder = ExecResult.newBuilder();
        try {
            taskUpdater.modify(task);
            builder.setHealthy(true).setMsg(TASK_SUCCESS_MSG);
        } catch (Exception e) {
            logger.error("modify fail", e);
            builder.setHealthy(false).setMsg(e.getMessage());
        }
        return builder.build();
    }

}