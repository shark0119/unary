package cn.com.unary.initcopy.grpc.service;

import cn.com.unary.initcopy.grpc.InitCopyGrpc;
import cn.com.unary.initcopy.grpc.entity.DeleteTask;
import cn.com.unary.initcopy.grpc.entity.ExecResult;
import cn.com.unary.initcopy.grpc.entity.ModifyTask;
import cn.com.unary.initcopy.grpc.entity.QueryTask;
import cn.com.unary.initcopy.grpc.entity.SyncTask;
import cn.com.unary.initcopy.grpc.entity.TaskState;
import cn.com.unary.initcopy.grpc.linker.InitCopyGrpcLinker;
import io.grpc.stub.StreamObserver;

/**
 * 初始化复制 GRPC 服务实现类
 *
 * @author Shark.Yin
 * @since 1.0
 */
public class InitCopyGrpcImpl extends InitCopyGrpc.InitCopyImplBase {

    private InitCopyGrpcLinker linker;

    /**
     *
     * @param linker 与业务逻辑代码的链接器
     */
    public InitCopyGrpcImpl(InitCopyGrpcLinker linker) {
        this.linker = linker;
    }

    @Override
    public void add(SyncTask request, StreamObserver<ExecResult> responseObserver) {
        logger.info("task:" + request);
        final ExecResult result = linker.add(request);
        logger.info("result:" + result);
        responseObserver.onNext(result);
        responseObserver.onCompleted();
    }

    @Override
    public void query(QueryTask request, StreamObserver<TaskState> responseObserver) {
        logger.info("task:" + request);
        final TaskState result = linker.query(request);
        logger.info("result:" + result);
        responseObserver.onNext(result);
        responseObserver.onCompleted();
    }

    @Override
    public void delete(DeleteTask request, StreamObserver<ExecResult> responseObserver) {
        logger.info("task:" + request);
        ExecResult result = linker.delete(request);
        logger.info("result:" + result);
        responseObserver.onNext(result);
        responseObserver.onCompleted();
    }

    @Override
    public void modify(ModifyTask request, StreamObserver<ExecResult> responseObserver) {
        logger.info("task:" + request);
        ExecResult result = linker.modify(request);
        logger.info("result:" + result);
        responseObserver.onNext(result);
        responseObserver.onCompleted();
    }
}