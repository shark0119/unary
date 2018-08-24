package cn.com.unary.initcopy.grpc.linker;

import cn.com.unary.initcopy.grpc.entity.ClientInitReq;
import cn.com.unary.initcopy.grpc.entity.DeleteTask;
import cn.com.unary.initcopy.grpc.entity.ExecResult;
import cn.com.unary.initcopy.grpc.entity.ModifyTask;
import cn.com.unary.initcopy.grpc.entity.ServerInitResp;
import org.springframework.stereotype.Component;

/**
 * 作为 GRPC 服务与业务代码通讯的中转站
 * 控制任务 GRPC 服务的任务管理中心，控制任务 GRPC 服务的具体业务逻辑处理
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Component("ControlTaskGrpcLinker")
public class ControlTaskGrpcLinker {

    /**
     * 调用 {@link cn.com.unary.initcopy.grpc.ControlTaskGrpc#METHODID_INIT}
     *
     * @param clientInitReq 初始化请求
     * @return 初始化响应
     */
    public ServerInitResp init(ClientInitReq clientInitReq) {
        return null;
    }

    /**
     * 调用 {@link cn.com.unary.initcopy.grpc.ControlTaskGrpc#METHODID_DELETE}
     *
     * @param deleteTask 删除任务的相关参数
     * @return 执行结果
     */
    public ExecResult delete(DeleteTask deleteTask) {
        return null;
    }

    /**
     * 调用 {@link cn.com.unary.initcopy.grpc.ControlTaskGrpc#METHODID_MODIFY}
     *
     * @param modifyTask 修改任务的相关参数
     * @return 执行结果
     */
    public ExecResult modify(ModifyTask modifyTask) {
        return null;
    }
}
