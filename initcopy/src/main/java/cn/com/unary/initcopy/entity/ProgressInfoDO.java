package cn.com.unary.initcopy.entity;

import cn.com.unary.initcopy.common.Entity;

/**
 * 进程信息
 *
 * @author Shark.Yin
 * @since 1.0
 */
public class ProgressInfoDO implements Entity {
    private int stage;
    private int progress;
    private long totalFileNum;
    private long totalFileSize;
    private long syncedFileNum;
    private long syncedFileSize;
    private String syncingFileName;

    @Override
    public String toString() {
        return "ProgressInfoDO{" +
                "stage=" + stage +
                ", progress=" + progress +
                ", totalFileNum=" + totalFileNum +
                ", totalFileSize=" + totalFileSize +
                ", syncedFileNum=" + syncedFileNum +
                ", syncedFileSize=" + syncedFileSize +
                ", syncingFileName='" + syncingFileName + '\'' +
                '}';
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public long getTotalFileNum() {
        return totalFileNum;
    }

    public void setTotalFileNum(long totalFileNum) {
        this.totalFileNum = totalFileNum;
    }

    public long getTotalFileSize() {
        return totalFileSize;
    }

    public void setTotalFileSize(long totalFileSize) {
        this.totalFileSize = totalFileSize;
    }

    public long getSyncedFileNum() {
        return syncedFileNum;
    }

    public void setSyncedFileNum(long syncedFileNum) {
        this.syncedFileNum = syncedFileNum;
    }

    public long getSyncedFileSize() {
        return syncedFileSize;
    }

    public void setSyncedFileSize(long syncedFileSize) {
        this.syncedFileSize = syncedFileSize;
    }

    public String getSyncingFileName() {
        return syncingFileName;
    }

    public void setSyncingFileName(String syncingFileName) {
        this.syncingFileName = syncingFileName;
    }
}
