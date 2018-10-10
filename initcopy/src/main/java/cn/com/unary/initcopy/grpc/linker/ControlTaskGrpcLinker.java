package cn.com.unary.initcopy.grpc.linker;

import cn.com.unary.initcopy.common.AbstractLoggable;
import cn.com.unary.initcopy.filecopy.ServerFileCopy;
import cn.com.unary.initcopy.grpc.entity.ClientInitReq;
import cn.com.unary.initcopy.grpc.entity.DeleteTask;
import cn.com.unary.initcopy.grpc.entity.ExecResult;
import cn.com.unary.initcopy.grpc.entity.ModifyTask;
import cn.com.unary.initcopy.grpc.entity.ServerInitResp;
import cn.com.unary.initcopy.service.ServerTaskUpdater;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 控制任务 GRPC 服务的任务管理中心
 * 线程安全
 * 1. 转发任务到具体业务逻辑
 * 2. 负责 GRPC 实体和 POJO 的映射。
 * 3. 参数合法校验
 * 4. 会处理下层抛出的所有异常。
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Component("ControlTaskGrpcLinker")
@Scope("singleton")
public class ControlTaskGrpcLinker extends AbstractLoggable {

    private static final String TASK_SUCCESS_MSG = "Task success!";

    private static ServerFileCopy fileCopy;
    private static ServerTaskUpdater taskUpdater;

    @Autowired
    public void setServerFileCopy(ServerFileCopy fileCopy) {
        ControlTaskGrpcLinker.fileCopy = fileCopy;
    }

    @Autowired
    public void setTaskUpdater(ServerTaskUpdater updater) {
        ControlTaskGrpcLinker.taskUpdater = updater;
    }

    /**
     * 调用 {@link cn.com.unary.initcopy.grpc.ControlTaskGrpc#METHODID_INIT}
     *
     * @param req 初始化请求
     * @return 初始化响应
     */
    public ServerInitResp init(ClientInitReq req) {
        ServerInitResp.Builder builder = ServerInitResp.newBuilder().setTaskId(req.getTaskId());
        if (req.getBaseFileInfosCount() <= 0) {
            builder.setMsg("Illegal request. No files.").setReady(false);
        } else if (req.getTaskId() < 0) {
            builder.setReady(false).setMsg("Illegal request. Task Id");
        } else {
            try {
                return fileCopy.startInit(req);
            } catch (Exception e) {
                logger.error("init fail", e);
                builder.setReady(false).setMsg(e.getMessage());
            }
        }
        return builder.build();
    }

    /**
     * 调用 {@link cn.com.unary.initcopy.grpc.ControlTaskGrpc#METHODID_DELETE}
     *
     * @param task 删除任务的相关参数
     * @return 执行结果
     */
    public ExecResult delete(@NonNull DeleteTask task) {
        if (task.getTaskId() < 0) {
            throw new IllegalArgumentException("task id can't be negative.");
        }
        ExecResult.Builder builder = ExecResult.newBuilder();
        try {
            taskUpdater.delete(task);
            builder.setIsHealthy(true).setMsg(TASK_SUCCESS_MSG);
        } catch (Exception e) {
            logger.error("delete fail", e);
            builder.setIsHealthy(false).setMsg(e.getMessage());
        }
        return builder.build();
    }

    /**
     * 调用 {@link cn.com.unary.initcopy.grpc.ControlTaskGrpc#METHODID_MODIFY}
     *
     * @param task 修改任务的相关参数
     * @return 执行结果
     */
    public ExecResult modify(ModifyTask task) {
        Objects.requireNonNull(task);
        if (task.getTaskId() < 0) {
            throw new IllegalArgumentException("task id can't be negative.");
        }
        Objects.requireNonNull(task.getModifyType());
        ExecResult.Builder builder = ExecResult.newBuilder();
        try {
            taskUpdater.modify(task);
            builder.setIsHealthy(true).setMsg(TASK_SUCCESS_MSG);
        } catch (Exception e) {
            logger.error("modify fail", e);
            builder.setIsHealthy(false).setMsg(e.getMessage());
        }
        return builder.build();
    }
}
