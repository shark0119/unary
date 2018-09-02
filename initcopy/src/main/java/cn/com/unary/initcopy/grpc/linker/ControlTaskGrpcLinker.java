package cn.com.unary.initcopy.grpc.linker;

import cn.com.unary.initcopy.common.AbstractLogable;
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

import java.lang.reflect.InvocationTargetException;

/**
 * 控制任务 GRPC 服务的任务管理中心。
 * 转发控制任务 GRPC 服务到业务代码。
 * 同时负责 GRPC 实体和 POJO 的映射。
 * 线程安全
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Component("ControlTaskGrpcLinker")
@Scope("singleton")
public class ControlTaskGrpcLinker extends AbstractLogable {

    @Autowired
    private ServerFileCopy fileCopy;
    @Autowired
    private ServerTaskUpdater taskUpdater;

    /**
     * 调用 {@link cn.com.unary.initcopy.grpc.ControlTaskGrpc#METHODID_INIT}
     *
     * @param clientInitReq 初始化请求
     * @return 初始化响应
     */
    public ServerInitResp init(ClientInitReq clientInitReq) {
        try {
            ClientInitReqDO reqDO = BeanConverter.convert(clientInitReq, ClientInitReqDO.class);
            ServerInitRespDO respDO = fileCopy.startInit(reqDO);
            return BeanConverter.convert(respDO, ServerInitResp.class);
        } catch (Exception e) {
            logger.error("program error.");
            throw new IllegalStateException(e);
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
            logger.error("program error.");
            throw new IllegalStateException(e);
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
            logger.error("program error.");
            throw new IllegalStateException(e);
        }
    }
}
