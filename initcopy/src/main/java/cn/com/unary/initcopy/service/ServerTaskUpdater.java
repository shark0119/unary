package cn.com.unary.initcopy.service;

import cn.com.unary.initcopy.common.AbstractLoggable;
import cn.com.unary.initcopy.dao.FileManager;
import cn.com.unary.initcopy.entity.Constants;
import cn.com.unary.initcopy.exception.TaskFailException;
import cn.com.unary.initcopy.filecopy.ServerFileCopy;
import cn.com.unary.initcopy.grpc.entity.DeleteTask;
import cn.com.unary.initcopy.grpc.entity.ModifyTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;

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
    private ServerFileCopy fileCopy;

    /**
     * 删除任务
     *
     * @param task 任务相关信息实体
     */
    public void delete(DeleteTask task) throws TaskFailException {
        // 暂停当前任务
        try {
            fileCopy.deleteTask(task.getTaskId());
        } catch (IOException e) {
            throw new TaskFailException(e);
        }
        // 删除任务相关信息
        fm.deleteTask(task.getTaskId());
    }

    /**
     * 修改任务相关信息实体
     *
     * @param task 任务实体
     */
    public void modify(ModifyTask task) throws TaskFailException {
        Constants.UpdateType updateType;
        switch (task.getModifyType()) {
            case START:
                updateType = Constants.UpdateType.RESUME;
                break;
            case PAUSE:
                updateType = Constants.UpdateType.PAUSE;
                break;
            case SPEED_LIMIT:
            default:
                throw new TaskFailException("unSupport operation " + task.getModifyType());
        }
        try {
            fileCopy.updateTask(task.getTaskId(), updateType);
        } catch (IOException e) {
            throw new TaskFailException(e);
        }
    }
}
