package cn.com.unary.initcopy.entity;

import cn.com.unary.initcopy.entity.Constants.FileType;

import java.beans.Transient;

/**
 * 同步过程中需要获取的文件信息相关数据
 *
 * @author Shark.Yin
 * @since 1.0
 */
public class FileInfoDO extends BaseFileInfoDO {
    private static final long serialVersionUID = 7000233780395813428L;
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
        this.setTaskId(bfi.getTaskId());
        this.setId(bfi.getId());
        this.setModifyTime(bfi.getModifyTime());
    }

    public static FileAttr newFileAttr() {
        return new FileAttr();
    }

    public FileAttr getAttr() {
        return attr;
    }

    public void setAttr(FileAttr attr) {
        this.attr = attr;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public int getBeginPackIndex() {
        return beginPackIndex;
    }

    public void setBeginPackIndex(int beginPackIndex) {
        this.beginPackIndex = beginPackIndex;
    }

    public int getFinishPackIndex() {
        return finishPackIndex;
    }

    public void setFinishPackIndex(int finishPackIndex) {
        this.finishPackIndex = finishPackIndex;
    }

    public String getState() {
        return state.toString();
    }

    public void setState(String state) {
        this.state = STATE.valueOf(state);
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
            if (((FileInfoDO) obj).getId().equals(this.getId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "FileInfoDO{" +
                "fileType=" + fileType +
                ", beginPackIndex=" + beginPackIndex +
                ", finishPackIndex=" + finishPackIndex +
                ", attr=" + attr +
                ", state=" + state +
                '}';
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
