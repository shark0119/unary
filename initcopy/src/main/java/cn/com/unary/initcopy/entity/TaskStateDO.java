package cn.com.unary.initcopy.entity;

import cn.com.unary.initcopy.common.Entity;

/**
 * 任务状态实体
 *
 * @author Shark.Yin
 * @since 1.0
 */
public class TaskStateDO implements Entity {
    private Integer taskId;
    private ExecResultDO execResult;
    private ProgressInfoDO progressInfo;

    @Override
    public String toString() {
        return "TaskStateDO{" +
                "taskId=" + taskId +
                ", execResult=" + execResult +
                ", progressInfo=" + progressInfo +
                '}';
    }

    public ExecResultDO getExecResult() {
        return execResult;
    }

    public void setExecResult(ExecResultDO execResult) {
        this.execResult = execResult;
    }

    public ProgressInfoDO getProgressInfo() {
        return progressInfo;
    }

    public void setProgressInfo(ProgressInfoDO progressInfo) {
        this.progressInfo = progressInfo;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}
