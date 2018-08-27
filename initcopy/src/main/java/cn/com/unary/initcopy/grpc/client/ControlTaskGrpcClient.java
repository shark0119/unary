package cn.com.unary.initcopy.grpc.client;

import cn.com.unary.initcopy.grpc.ControlTaskGrpc;
import cn.com.unary.initcopy.grpc.entity.ClientInitReq;
import cn.com.unary.initcopy.grpc.entity.DeleteTask;
import cn.com.unary.initcopy.grpc.entity.ExecResult;
import cn.com.unary.initcopy.grpc.entity.ModifyTask;
import cn.com.unary.initcopy.grpc.entity.ServerInitResp;
import cn.com.unary.initcopy.grpc.linker.ControlTaskGrpcLinker;
import cn.com.unary.initcopy.utils.AbstractLogable;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * 源端和目标端之间，任务控制信息GRPC接口的调用方(客户端)
 *
 * @author Shark.Yin
 * @since 1.0
 */
public class ControlTaskGrpcClient extends AbstractLogable {


    private ControlTaskGrpc.ControlTaskBlockingStub blockingStub;
    private ControlTaskGrpc.ControlTaskFutureStub futureStub;
    private ControlTaskGrpcLinker linker;

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
     * @param req 初始化请求
     * @return 初始化响应
     */
    public ServerInitResp invokeGrpcInit(ClientInitReq req) {
        /*final ListenableFuture<ServerInitResp> sir =  futureStub.init(req);
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
        }, null);*/
        logger.debug("Send file data to the target to confirm.");
        return linker.init(req);
        // return blockingStub.init(req);
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

    public void setLinker(ControlTaskGrpcLinker linker) {
        this.linker = linker;
    }
}
