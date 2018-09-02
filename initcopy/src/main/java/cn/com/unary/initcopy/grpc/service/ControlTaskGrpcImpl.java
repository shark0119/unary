package cn.com.unary.initcopy.grpc.service;

import cn.com.unary.initcopy.grpc.ControlTaskGrpc;
import cn.com.unary.initcopy.grpc.entity.DeleteTask;
import cn.com.unary.initcopy.grpc.entity.ExecResult;
import cn.com.unary.initcopy.grpc.entity.ModifyTask;
import cn.com.unary.initcopy.grpc.entity.ServerInitResp;
import cn.com.unary.initcopy.grpc.linker.ControlTaskGrpcLinker;
import io.grpc.stub.StreamObserver;

/**
 * 任务控制 GRPC 服务实现类
 *
 * @author Shark.Yin
 * @since 1.0
 */
public class ControlTaskGrpcImpl extends ControlTaskGrpc.ControlTaskImplBase {
    private ControlTaskGrpcLinker linker;

    /**
     *
     * @param linker 任务控制 GRPC 服务与业务代码的连接器
     */
    public ControlTaskGrpcImpl(ControlTaskGrpcLinker linker) {
        this.linker = linker;
    }

    @Override
    public void init(cn.com.unary.initcopy.grpc.entity.ClientInitReq request,
                     StreamObserver<ServerInitResp> responseObserver) {
        responseObserver.onNext(linker.init(request));
        responseObserver.onCompleted();
    }

    @Override
    public void delete(DeleteTask request, StreamObserver<ExecResult> responseObserver) {
        responseObserver.onNext(linker.delete(request));
        responseObserver.onCompleted();
    }

    @Override
    public void modify(ModifyTask request, StreamObserver<ExecResult> responseObserver) {
        responseObserver.onNext(linker.modify(request));
        responseObserver.onCompleted();
    }
}