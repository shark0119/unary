package cn.com.unary.initcopy.service;

import cn.com.unary.initcopy.InitCopyContext;
import cn.com.unary.initcopy.common.AbstractLogable;
import cn.com.unary.initcopy.dao.FileManager;
import cn.com.unary.initcopy.entity.Constants;
import cn.com.unary.initcopy.entity.DeleteTaskDO;
import cn.com.unary.initcopy.entity.ExecResultDO;
import cn.com.unary.initcopy.entity.ModifyTaskDO;
import cn.com.unary.initcopy.entity.SyncTargetDO;
import cn.com.unary.initcopy.entity.TaskStateDO;
import cn.com.unary.initcopy.exception.TaskException;
import cn.com.unary.initcopy.filecopy.ClientFileCopy;
import cn.com.unary.initcopy.grpc.client.ControlTaskGrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * 客户端的任务修改器
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Component("ClientTaskUpdater")
public class ClientTaskUpdater extends AbstractLogable {

    @Autowired
    @Qualifier("clientFM")
    private FileManager fm;
    @Autowired
    private ClientFileCopy fileCopy;
    @Autowired
    private InitCopyContext context;

    public ExecResultDO delete(DeleteTaskDO task) {
        ExecResultDO resultDO;
        try {
            fileCopy.updateTask(task.getTaskId(), Constants.UpdateType.DELETE);
        } catch (TaskException e) {
            resultDO = new ExecResultDO();
            resultDO.setIsHealthy(false).setMsg(e.getMessage()).setCode(1);
            return resultDO;
        }
        SyncTargetDO targetInfo = fm.queryTask(task.getTaskId()).getTargetInfo();
        ControlTaskGrpcClient client = new ControlTaskGrpcClient(targetInfo.getIp(), context.getInnerGrpcPort());
        return client.invokeGrpcDelete(task);
    }

    private ExecResultDO modify(ModifyTaskDO task) {
        Constants.UpdateType updateType;
        switch (task.getModifyType()) {
            case SPEED_LIMIT:

            case START:
                updateType = Constants.UpdateType.RESUME;
                break;
            case PAUSE:
                // treat as default option
            default:
                updateType = Constants.UpdateType.PAUSE;
                break;
        }
        ExecResultDO resultDO;
        try {
            fileCopy.updateTask(task.getTaskId(), updateType);
        } catch (TaskException e) {
            resultDO = new ExecResultDO();
            resultDO.setIsHealthy(false).setMsg(e.getMessage()).setCode(1);
            return resultDO;
        }
        SyncTargetDO targetInfo = fm.queryTask(task.getTaskId()).getTargetInfo();
        ControlTaskGrpcClient client = new ControlTaskGrpcClient(targetInfo.getIp(), context.getInnerGrpcPort());
        return client.invokeGrpcModify(task);
    }
    private TaskStateDO query (int taskId) {
        return null;
    }
}
