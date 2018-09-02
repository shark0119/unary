package cn.com.unary.initcopy.entity;

import cn.com.unary.initcopy.common.Entity;

import java.util.List;

/**
 * 服务端返回的初始化响应
 *
 * @author Shark.Yin
 * @since 1.0
 */
public class ServerInitRespDO implements Entity {
    private Integer taskId;
    private Boolean ready;
    private String msg;
    private List<DiffFileInfoDO> diffFileInfo;

    @Override
    public String toString() {
        return "ServerInitResp{" +
                "taskId=" + taskId +
                ", ready=" + ready +
                ", msg='" + msg + '\'' +
                ", diffFileInfo=" + diffFileInfo +
                '}';
    }

    public int getTaskId() {
        return taskId;
    }

    public ServerInitRespDO setTaskId(int taskId) {
        this.taskId = taskId;
        return this;
    }

    public boolean isReady() {
        return ready;
    }

    public ServerInitRespDO setReady(boolean ready) {
        this.ready = ready;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ServerInitRespDO setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public List<DiffFileInfoDO> getDiffFileInfo() {
        return diffFileInfo;
    }

    public ServerInitRespDO setDiffFileInfo(List<DiffFileInfoDO> diffFileInfo) {
        this.diffFileInfo = diffFileInfo;
        return this;
    }
}
