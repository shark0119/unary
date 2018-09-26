package cn.com.unary.initcopy.mock;

import cn.com.unary.initcopy.common.AbstractLoggable;
import cn.com.unary.initcopy.grpc.InitCopyGrpc;
import cn.com.unary.initcopy.grpc.entity.ExecResult;
import cn.com.unary.initcopy.grpc.entity.SyncTask;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class InitCopyGrpcClient extends AbstractLoggable {

    private InitCopyGrpc.InitCopyBlockingStub blockingStub;

    /**
     * 配置 GRPC 服务的相关信息
     *
     * @param host GRPC 服务地址
     * @param port GRPC 服务监听的端口
     */
    public InitCopyGrpcClient(String host, int gp) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(host, gp).usePlaintext(true).build();
        blockingStub = InitCopyGrpc.newBlockingStub(channel);
    }

    public ExecResult add(SyncTask task) {
        return blockingStub.add(task);
    }
}
