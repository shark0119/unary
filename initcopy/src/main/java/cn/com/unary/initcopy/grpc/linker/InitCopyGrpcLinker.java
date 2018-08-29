package cn.com.unary.initcopy.grpc.linker;

import cn.com.unary.initcopy.filecopy.ClientFileCopy;
import cn.com.unary.initcopy.grpc.entity.*;
import cn.com.unary.initcopy.common.AbstractLogable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 作为 GRPC 服务与业务代码通讯的中转站
 * 任务管理中心，初始化复制 GRPC 服务的具体业务逻辑处理
 * 线程安全
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Component("InitCopyGrpcLinker")
@Scope("singleton")
public class InitCopyGrpcLinker extends AbstractLogable {


    @Autowired
    @Qualifier("ClientFileCopy")
    private ClientFileCopy clientFileCopy;

	public ExecResult add (SyncTask task) {
	    ExecResult.Builder builder = ExecResult.newBuilder();
        try {
            clientFileCopy.addTask(task);
            builder.setIsHealthy(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            builder.setIsHealthy(false);
            builder.setMsg(e.getMessage());
        }
        return builder.build();
    }
	public TaskState query (QueryTask queryTask) {
		System.out.println(queryTask.getTaskId());
		return TaskState.newBuilder().build();
	}
	public ExecResult delete (DeleteTask deleteTask) {
		System.out.println(deleteTask.getTaskId());
		return ExecResult.newBuilder().build();
	}
	public ExecResult modify (ModifyTask modifyTask) {
		System.out.println(modifyTask.getTaskId());
		return ExecResult.newBuilder().build();
	}
}
