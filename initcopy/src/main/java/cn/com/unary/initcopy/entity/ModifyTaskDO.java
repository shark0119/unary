package cn.com.unary.initcopy.entity;

import cn.com.unary.initcopy.grpc.constant.ModifyType;

/**
 * 修改任務的实体相关信息
 *
 * @author Shark.Yin
 * @since 1.0
 */
public class ModifyTaskDO {
    private int taskId;
    private ModifyType modifyType;
    private int speedLimit;

    @Override
    public String toString() {
        return "ModifyTaskDO{" +
                "taskId=" + taskId +
                ", modifyType=" + modifyType +
                ", speedLimit=" + speedLimit +
                '}';
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public ModifyType getModifyType() {
        return modifyType;
    }

    public void setModifyType(ModifyType modifyType) {
        this.modifyType = modifyType;
    }

    public int getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(int speedLimit) {
        this.speedLimit = speedLimit;
    }
}
