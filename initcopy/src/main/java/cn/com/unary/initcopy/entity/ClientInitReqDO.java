package cn.com.unary.initcopy.entity;

import cn.com.unary.initcopy.common.Entity;
import cn.com.unary.initcopy.grpc.constant.SyncType;

import java.util.List;

/**
 * 客户端初始化请求实体
 *
 * @author Shark.Yin
 * @since 1.0
 */
public class ClientInitReqDO implements Entity {

    private Integer taskId;
    private Long totalSize;
    private String targetDir;
    private SyncType syncType;
    private List<BaseFileInfoDO> fileBaseInfos;

    @Override
    public String toString() {
        return "ClientInitReqDO{" +
                "taskId=" + taskId +
                ", totalSize=" + totalSize +
                ", targetDir='" + targetDir + '\'' +
                ", syncType=" + syncType +
                ", fileBaseInfos=" + fileBaseInfos +
                '}';
    }

    public int getTaskId() {        return taskId;    }
    public void setTaskId(int taskId) {        this.taskId = taskId;    }
    public long getTotalSize() {        return totalSize;    }
    public void setTotalSize(long totalSize) {        this.totalSize = totalSize;    }
    public String getTargetDir() {        return targetDir;    }
    public void setTargetDir(String targetDir) {        this.targetDir = targetDir;    }
    public SyncType getSyncType() {        return syncType;    }
    public void setSyncType(SyncType syncType) {        this.syncType = syncType;    }
    public List<BaseFileInfoDO> getFileBaseInfos() {        return fileBaseInfos;    }
    public void setFileBaseInfos(List<BaseFileInfoDO> fileBaseInfos) {        this.fileBaseInfos = fileBaseInfos;    }
}
