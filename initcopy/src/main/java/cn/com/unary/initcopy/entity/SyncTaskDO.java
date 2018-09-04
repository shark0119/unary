package cn.com.unary.initcopy.entity;

import cn.com.unary.initcopy.common.Entity;
import cn.com.unary.initcopy.grpc.constant.CompressType;
import cn.com.unary.initcopy.grpc.constant.EncryptType;
import cn.com.unary.initcopy.grpc.constant.PackType;
import cn.com.unary.initcopy.grpc.constant.SyncType;

import java.util.List;

/**
 * 同步任务实体
 *
 * @author Shark.Yin
 * @since 1.0
 */
public class SyncTaskDO implements Entity {
    private int taskId;
    private SyncTargetDO targetInfo;
    private SyncType syncType;
    private CompressType compressType;
    private EncryptType encryptType;
    private PackType packType;
    private int speedLimit;
    private String targetDir;
    private List<String> files;
    private long totalSize;

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    @Override
    public String toString() {
        return "SyncTaskDO{" +
                "taskId=" + taskId +
                ", targetInfo=" + targetInfo +
                ", syncType=" + syncType +
                ", compressType=" + compressType +
                ", encryptType=" + encryptType +
                ", packType=" + packType +
                ", speedLimit=" + speedLimit +
                ", targetDir='" + targetDir + '\'' +
                ", file=" + files +
                '}';
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public SyncTargetDO getTargetInfo() {
        return targetInfo;
    }

    public void setTargetInfo(SyncTargetDO targetInfo) {
        this.targetInfo = targetInfo;
    }

    public SyncType getSyncType() {
        return syncType;
    }

    public void setSyncType(SyncType syncType) {
        this.syncType = syncType;
    }

    public CompressType getCompressType() {
        return compressType;
    }

    public void setCompressType(CompressType compressType) {
        this.compressType = compressType;
    }

    public EncryptType getEncryptType() {
        return encryptType;
    }

    public void setEncryptType(EncryptType encryptType) {
        this.encryptType = encryptType;
    }

    public PackType getPackType() {
        return packType;
    }

    public void setPackType(PackType packType) {
        this.packType = packType;
    }

    public int getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(int speedLimit) {
        this.speedLimit = speedLimit;
    }

    public String getTargetDir() {
        return targetDir;
    }

    public void setTargetDir(String targetDir) {
        this.targetDir = targetDir;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> file) {
        this.files = file;
    }
}
