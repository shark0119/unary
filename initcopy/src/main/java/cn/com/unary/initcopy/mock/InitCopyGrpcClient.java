package cn.com.unary.initcopy.mock;

import cn.com.unary.initcopy.common.AbstractLoggable;
import cn.com.unary.initcopy.grpc.InitCopyGrpc;
import cn.com.unary.initcopy.grpc.entity.DeleteTask;
import cn.com.unary.initcopy.grpc.entity.ExecResult;
import cn.com.unary.initcopy.grpc.entity.QueryTask;
import cn.com.unary.initcopy.grpc.entity.SyncTask;
import cn.com.unary.initcopy.grpc.entity.TaskState;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.io.Closeable;

/**
 * 用于模拟调用初始化复制的 GRPC 服务
 *
 * @author Shark.Yin
 * @since 1.0
 */
public class InitCopyGrpcClient extends AbstractLoggable implements Closeable {

    private InitCopyGrpc.InitCopyBlockingStub blockingStub;
    private ManagedChannel channel;

    /**
     * 配置 GRPC 服务的相关信息
     *
     * @param host GRPC 服务地址
     * @param gp GRPC 服务监听的端口
     */
    public InitCopyGrpcClient(String host, int gp) {
        channel = ManagedChannelBuilder
                .forAddress(host, gp).usePlaintext(true).build();
        blockingStub = InitCopyGrpc.newBlockingStub(channel);
    }

    public ExecResult add(SyncTask task) {
        return blockingStub.add(task);
    }

    public TaskState query(QueryTask task) {
        return blockingStub.query(task);
    }

    public ExecResult delete(DeleteTask task) {
        return blockingStub.delete(task);
    }

    @Override
    public void close() {
        channel.shutdown();
    }
}
