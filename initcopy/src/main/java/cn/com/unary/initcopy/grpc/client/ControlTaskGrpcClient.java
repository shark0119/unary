package cn.com.unary.initcopy.grpc.client;

import cn.com.unary.initcopy.common.AbstractLoggable;
import cn.com.unary.initcopy.common.BeanConverter;
import cn.com.unary.initcopy.entity.DeleteTaskDO;
import cn.com.unary.initcopy.entity.ExecResultDO;
import cn.com.unary.initcopy.entity.ModifyTaskDO;
import cn.com.unary.initcopy.grpc.ControlTaskGrpc;
import cn.com.unary.initcopy.grpc.entity.ClientInitReq;
import cn.com.unary.initcopy.grpc.entity.DeleteTask;
import cn.com.unary.initcopy.grpc.entity.ExecResult;
import cn.com.unary.initcopy.grpc.entity.ModifyTask;
import cn.com.unary.initcopy.grpc.entity.ServerInitResp;
import cn.com.unary.initcopy.grpc.linker.ControlTaskGrpcLinker;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * 源端和目标端之间，任务控制信息GRPC接口的调用方(客户端)
 *
 * @author Shark.Yin
 * @since 1.0
 */
public class ControlTaskGrpcClient extends AbstractLoggable {

    private ControlTaskGrpc.ControlTaskBlockingStub blockingStub;
    /**
     * 测试代码
     */
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
        linker = new ControlTaskGrpcLinker();
    }

    /**
     * 调用 {@link cn.com.unary.initcopy.grpc.ControlTaskGrpc#METHODID_INIT}
     *
     * @param req 初始化请求
     * @return 初始化响应
     */
    public ServerInitResp invokeGrpcInit(ClientInitReq req) {
        logger.debug("Send file data to the target to confirm.");
        return linker.init(req);
    }

    /**
     * 调用 {@link cn.com.unary.initcopy.grpc.ControlTaskGrpc#METHODID_DELETE}
     *
     * @param deleteTaskDO 删除任务的相关参数
     * @return 执行结果
     */
    public ExecResultDO invokeGrpcDelete(DeleteTaskDO deleteTaskDO) {
        try {
            ExecResult result = blockingStub.delete(BeanConverter.convert(deleteTaskDO, DeleteTask.class));
            return BeanConverter.convert(result, ExecResultDO.class);
        } catch (Exception e) {
            logger.error("Error.", e);
            throw new IllegalStateException(e);
        }
    }

    /**
     * 调用 {@link cn.com.unary.initcopy.grpc.ControlTaskGrpc#METHODID_MODIFY}
     *
     * @param modifyTaskDO 修改任务的相关参数
     * @return 执行结果
     */
    public ExecResultDO invokeGrpcModify(ModifyTaskDO modifyTaskDO) {
        try {
            ExecResult result = blockingStub.modify(BeanConverter.convert(modifyTaskDO, ModifyTask.class));
            return BeanConverter.convert(result, ExecResultDO.class);
        } catch (Exception e) {
            logger.error("Error.", e);
            throw new IllegalStateException(e);
        }
    }
}
