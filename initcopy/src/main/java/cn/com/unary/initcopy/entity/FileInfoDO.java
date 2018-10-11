package cn.com.unary.initcopy.entity;

import cn.com.unary.initcopy.entity.Constants.FileType;
import lombok.Data;
import lombok.ToString;

import java.beans.Transient;

/**
 * 同步过程中需要获取的文件信息相关数据
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Data
@ToString(callSuper = true)
public class FileInfoDO extends BaseFileInfoDO {
    private static final long serialVersionUID = 7000233780395813428L;
    private String taskId;
    private FileType fileType;
    private Integer beginPackIndex;
    private Integer finishPackIndex;
    private FileAttr attr;
    private STATE state;

    public FileInfoDO() {
    }

    public FileInfoDO(BaseFileInfoDO bfi) {
        this.setFileSize(bfi.getFileSize());
        this.setFullName(bfi.getFullName());
        this.setFileId(bfi.getFileId());
        this.setModifyTime(bfi.getModifyTime());
        this.setBackUpPath(bfi.getBackUpPath());
    }

    public static FileAttr newFileAttr() {
        return new FileAttr();
    }

    @Transient
    public void setState(STATE state) {
        this.state = state;
    }

    @Transient
    public STATE getStateEnum() {
        return this.state;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FileInfoDO) {
            return ((FileInfoDO) obj).getFileId().equals(this.getFileId());
        }
        return false;
    }

    public enum STATE {
        /**
         * 等待同步
         */
        WAIT,
        /**
         * 正在同步
         */
        SYNCING,
        /**
         * 已同步
         */
        SYNCED,
    }

    public static class FileAttr {
        private boolean isHidden;
        private String owner;

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public boolean isHidden() {
            return isHidden;
        }

        public void setHidden(boolean isHidden) {
            this.isHidden = isHidden;
        }

        @Override
        public String toString() {
            return "FileAttr [isHidden=" + isHidden + ", owner=" + owner + "]";
        }
    }
}
