package cn.com.unary.initcopy.grpc.linker;

import cn.com.unary.initcopy.common.AbstractLoggable;
import cn.com.unary.initcopy.common.BeanConverter;
import cn.com.unary.initcopy.entity.ClientInitReqDO;
import cn.com.unary.initcopy.entity.DeleteTaskDO;
import cn.com.unary.initcopy.entity.ExecResultDO;
import cn.com.unary.initcopy.entity.ModifyTaskDO;
import cn.com.unary.initcopy.entity.ServerInitRespDO;
import cn.com.unary.initcopy.filecopy.ServerFileCopy;
import cn.com.unary.initcopy.grpc.entity.ClientInitReq;
import cn.com.unary.initcopy.grpc.entity.DeleteTask;
import cn.com.unary.initcopy.grpc.entity.ExecResult;
import cn.com.unary.initcopy.grpc.entity.ModifyTask;
import cn.com.unary.initcopy.grpc.entity.ServerInitResp;
import cn.com.unary.initcopy.service.ServerTaskUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 控制任务 GRPC 服务的任务管理中心。
 * 转发控制任务 GRPC 服务到业务代码。
 * 同时负责 GRPC 实体和 POJO 的映射。
 * 会处理下层抛出的所有异常。
 * 线程安全
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Component("ControlTaskGrpcLinker")
@Scope("singleton")
public class ControlTaskGrpcLinker extends AbstractLoggable {

    @Autowired
    private ServerFileCopy fileCopy;
    @Autowired
    private ServerTaskUpdater taskUpdater;

    /**
     * 调用 {@link cn.com.unary.initcopy.grpc.ControlTaskGrpc#METHODID_INIT}
     *
     * @param req 初始化请求
     * @return 初始化响应
     */
    public ServerInitResp init(ClientInitReq req) {
        try {
            ClientInitReqDO reqDO = BeanConverter.convert(req, ClientInitReqDO.class);
            ServerInitRespDO respDO = fileCopy.startInit(reqDO);
            return BeanConverter.convert(respDO, ServerInitResp.class);
        } catch (Exception e) {
            logger.error("init fail", e);
            ServerInitResp.Builder builder = ServerInitResp.newBuilder();
            builder.setReady(false).setTaskId(req.getTaskId()).setMsg(e.getMessage());
            return builder.build();
        }
    }

    /**
     * 调用 {@link cn.com.unary.initcopy.grpc.ControlTaskGrpc#METHODID_DELETE}
     *
     * @param deleteTask 删除任务的相关参数
     * @return 执行结果
     */
    public ExecResult delete(DeleteTask deleteTask) {
        try {
            DeleteTaskDO taskDO = BeanConverter.convert(deleteTask, DeleteTaskDO.class);
            ExecResultDO respDO = taskUpdater.delete(taskDO);
            return BeanConverter.convert(respDO, ExecResult.class);
        } catch (Exception e) {
            logger.error("delete fail", e);
            ExecResult.Builder builder = ExecResult.newBuilder();
            builder.setIsHealthy(false).setMsg(e.getMessage());
            return builder.build();
        }
    }

    /**
     * 调用 {@link cn.com.unary.initcopy.grpc.ControlTaskGrpc#METHODID_MODIFY}
     *
     * @param modifyTask 修改任务的相关参数
     * @return 执行结果
     */
    public ExecResult modify(ModifyTask modifyTask) {
        try {
            ModifyTaskDO taskDO = BeanConverter.convert(modifyTask, ModifyTaskDO.class);
            ExecResultDO respDO = taskUpdater.modify(taskDO);
            return BeanConverter.convert(respDO, ExecResult.class);
        } catch (Exception e) {
            logger.error("modify fail", e);
            ExecResult.Builder builder = ExecResult.newBuilder();
            builder.setIsHealthy(false).setMsg(e.getMessage());
            return builder.build();
        }
    }
}
