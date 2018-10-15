package cn.com.unary.initcopy.grpc.service;

import cn.com.unary.initcopy.common.AbstractLoggable;
import cn.com.unary.initcopy.grpc.ControlTaskGrpc;
import cn.com.unary.initcopy.grpc.entity.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.io.Closeable;

/**
 * 源端和目标端之间，任务控制信息GRPC接口的调用方(客户端)
 *
 * @author Shark.Yin
 * @since 1.0
 */
public class ControlTaskGrpcClient extends AbstractLoggable implements Closeable {

    private ControlTaskGrpc.ControlTaskBlockingStub blockingStub;
    private ManagedChannel channel;

    /**
     * 配置 GRPC 服务的相关信息
     *
     * @param host GRPC 服务地址
     * @param port GRPC 服务监听的端口
     */
    public ControlTaskGrpcClient(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build();
        blockingStub = ControlTaskGrpc.newBlockingStub(channel);
    }

    /**
     * 调用 {@link cn.com.unary.initcopy.grpc.ControlTaskGrpc#METHODID_INIT}
     *
     * @param req 初始化请求
     * @return 初始化响应
     */
    public ServerInitResp invokeGrpcInit(ClientInitReq req) {
        return blockingStub.init(req);
    }

    /**
     * 调用 {@link cn.com.unary.initcopy.grpc.ControlTaskGrpc#METHODID_DELETE}
     *
     * @param deleteTask 删除任务的相关参数
     * @return 执行结果
     */
    public ExecResult invokeGrpcDelete(DeleteTask deleteTask) {
        return blockingStub.delete(deleteTask);
    }

    /**
     * 调用 {@link cn.com.unary.initcopy.grpc.ControlTaskGrpc#METHODID_MODIFY}
     *
     * @param modifyTask 修改任务的相关参数
     * @return 执行结果
     */
    public ExecResult invokeGrpcModify(ModifyTask modifyTask) {
        return blockingStub.modify(modifyTask);
    }

    public TaskState invokeGrpcQuery(QueryTask queryTask) {
        return blockingStub.query(queryTask);
    }

    @Override
    public void close() {
        channel.shutdown();
    }
}
