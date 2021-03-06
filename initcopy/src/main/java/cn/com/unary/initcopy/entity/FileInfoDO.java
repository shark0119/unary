package cn.com.unary.initcopy.entity;

import cn.com.unary.initcopy.entity.Constants.FileType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.beans.Transient;

/**
 * 同步过程中需要获取的文件信息相关数据
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class FileInfoDO extends BaseFileInfoDO {
    private static final long serialVersionUID = 7000233780395813428L;
    private STATE state;
    private String taskId;
    private FileAttr attr;
    private Long syncDoneTime;
    private FileType fileType;
    private Integer beginPackIndex;
    private Integer finishPackIndex;

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
        private boolean hidden;
        private String owner;

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public boolean isHidden() {
            return hidden;
        }

        public void setHidden(boolean isHidden) {
            this.hidden = isHidden;
        }

        @Override
        public String toString() {
            return "FileAttr [hidden=" + hidden + ", owner=" + owner + "]";
        }
    }
}
