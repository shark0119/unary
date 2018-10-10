package cn.com.unary.initcopy.grpc.linker;

import cn.com.unary.initcopy.common.AbstractLoggable;
import cn.com.unary.initcopy.common.utils.ValidateUtils;
import cn.com.unary.initcopy.filecopy.ClientFileCopy;
import cn.com.unary.initcopy.grpc.entity.DeleteTask;
import cn.com.unary.initcopy.grpc.entity.ExecResult;
import cn.com.unary.initcopy.grpc.entity.ModifyTask;
import cn.com.unary.initcopy.grpc.entity.QueryTask;
import cn.com.unary.initcopy.grpc.entity.SyncTask;
import cn.com.unary.initcopy.grpc.entity.TaskState;
import cn.com.unary.initcopy.service.ClientTaskUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 初始化复制 GRPC 服务的任务管理中心
 * 作为 GRPC 服务与业务代码通讯的
 * 线程安全
 * 1. 转发任务到具体业务逻辑
 * 2. 负责 GRPC 实体和 POJO 的映射。
 * 3. 参数合法校验
 * 4. 会处理下层抛出的所有异常。
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Component("InitCopyGrpcLinker")
@Scope("singleton")
public class InitCopyGrpcLinker extends AbstractLoggable {

    private static ClientFileCopy fileCopy;
    private static ClientTaskUpdater updater;

    @Autowired
    public void setFileCopy(ClientFileCopy fileCopy) {
        InitCopyGrpcLinker.fileCopy = fileCopy;
    }

    @Autowired
    public void setUpdater(ClientTaskUpdater updater) {
        InitCopyGrpcLinker.updater = updater;
    }

    public ExecResult add(SyncTask task) {
        ValidateUtils.requireNotEmpty(task.getFilesList());
        Objects.requireNonNull(task.getTargetInfo());
        if (task.getTaskId() < 0) {
            throw new IllegalArgumentException("task id can't be negative.");
        }
        ExecResult.Builder builder = ExecResult.newBuilder();
        try {
            fileCopy.addTask(task);
            builder.setIsHealthy(true);
        } catch (Exception e) {
            logger.error("task add fail", e);
            builder.setIsHealthy(false);
            builder.setMsg(e.getMessage());
        }
        return builder.build();
    }

    public TaskState query(QueryTask task) {
        Objects.requireNonNull(task);
        if (task.getTaskId() < 0) {
            throw new IllegalArgumentException("task id can't be negative.");
        }
        try {
            TaskState state = updater.query(task.getTaskId());
            logger.info("stateDo:" + state);
            return state;
        } catch (Exception e) {
            logger.error("exception", e);
            ExecResult.Builder builder = ExecResult.newBuilder();
            builder.setMsg(e.getMessage()).setIsHealthy(false);
            return TaskState.newBuilder().setExecResult(builder)
                    .setTaskId(task.getTaskId()).build();
        }
    }

    public ExecResult delete(DeleteTask task) {
        Objects.requireNonNull(task);
        if (task.getTaskId() < 0) {
            throw new IllegalArgumentException("task id can't be negative.");
        }
        try {
            return updater.delete(task);
        } catch (Exception e) {
            logger.error("exception", e);
            return ExecResult.newBuilder().setIsHealthy(false).setMsg(e.getMessage()).build();
        }
    }

    public ExecResult modify(ModifyTask task) {
        Objects.requireNonNull(task);
        if (task.getTaskId() < 0) {
            throw new IllegalArgumentException("task id can't be negative.");
        }
        Objects.requireNonNull(task.getModifyType());
        try {
            return updater.modify(task);
        } catch (Exception e) {
            logger.error("exception", e);
            return ExecResult.newBuilder().setIsHealthy(false).setMsg(e.getMessage()).build();
        }
    }
}
