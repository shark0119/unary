package cn.com.unary.initcopy.service;

import cn.com.unary.initcopy.common.AbstractLoggable;
import cn.com.unary.initcopy.dao.FileManager;
import cn.com.unary.initcopy.entity.Constants;
import cn.com.unary.initcopy.entity.DeleteTaskDO;
import cn.com.unary.initcopy.entity.ExecResultDO;
import cn.com.unary.initcopy.entity.ModifyTaskDO;
import cn.com.unary.initcopy.exception.TaskFailException;
import cn.com.unary.initcopy.filecopy.ClientFileCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * 目标端的任务修改器
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Component("ServerTaskUpdater")
public class ServerTaskUpdater extends AbstractLoggable {

    @Autowired
    @Qualifier("serverFM")
    private FileManager fm;
    @Autowired
    private ClientFileCopy fileCopy;

    /**
     * 删除任务
     *
     * @param task 任务相关信息实体
     * @return 执行结果
     */
    public ExecResultDO delete(DeleteTaskDO task) throws TaskFailException {
        ExecResultDO resultDO = new ExecResultDO(true, 0, "task success");
        fileCopy.updateTask(task.getTaskId(), Constants.UpdateType.DELETE);
        return resultDO;
    }

    /**
     * 修改任务相关信息实体
     *
     * @param task 任务实体
     * @return 执行结果
     */
    public ExecResultDO modify(ModifyTaskDO task) throws TaskFailException {
        Constants.UpdateType updateType;
        switch (task.getModifyType()) {
            case SPEED_LIMIT:
                return new ExecResultDO(true, 0, "unSupport operate");
            case START:
                updateType = Constants.UpdateType.RESUME;
                break;
            case PAUSE:
            default:
                updateType = Constants.UpdateType.PAUSE;
                break;
        }
        ExecResultDO resultDO = new ExecResultDO();
        resultDO.setHealthy(true);
        resultDO.setCode(0);
        resultDO.setMsg("task success");
        fileCopy.updateTask(task.getTaskId(), updateType);
        return resultDO;
    }
}
