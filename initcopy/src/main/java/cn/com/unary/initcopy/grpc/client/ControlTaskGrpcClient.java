package cn.com.unary.initcopy.grpc.client;

import cn.com.unary.initcopy.grpc.ControlTaskGrpc;
import cn.com.unary.initcopy.grpc.entity.ClientInitReq;
import cn.com.unary.initcopy.grpc.entity.DeleteTask;
import cn.com.unary.initcopy.grpc.entity.ExecResult;
import cn.com.unary.initcopy.grpc.entity.ModifyTask;
import cn.com.unary.initcopy.grpc.entity.ServerInitResp;
import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;

import java.util.concurrent.ExecutionException;

/**
 * 源端和目标端之间，任务控制信息GRPC接口的调用方(客户端)
 *
 * @author Shark.Yin
 * @since 1.0
 */
public class ControlTaskGrpcClient {

    private ControlTaskGrpc.ControlTaskBlockingStub blockingStub;
    private ControlTaskGrpc.ControlTaskFutureStub futureStub;

    /**
     * 配置 GRPC 服务的相关信息
     *
     * @param host GRPC 服务地址
     * @param port GRPC 服务监听的端口
     */
    public ControlTaskGrpcClient(String host, int port) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build();
        blockingStub = ControlTaskGrpc.newBlockingStub(channel);
        futureStub = ControlTaskGrpc.newFutureStub(channel);
    }

    /**
     * 调用 {@link cn.com.unary.initcopy.grpc.ControlTaskGrpc#METHODID_INIT}
     *
     * @param clientInitReq 初始化请求
     * @return 初始化响应
     */
    public ServerInitResp invokeGrpcInit(ClientInitReq clientInitReq) {
        final ListenableFuture<ServerInitResp> sir =  futureStub.init(clientInitReq);
        sir.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    sir.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }, null);
        return blockingStub.init(clientInitReq);
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

}
