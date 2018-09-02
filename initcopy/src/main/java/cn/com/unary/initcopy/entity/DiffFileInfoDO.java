package cn.com.unary.initcopy.entity;

import cn.com.unary.initcopy.common.Entity;

import java.util.List;

/**
 * 差异文件信息
 *
 * @author Shark.Yin
 * @since 1.0
 */
public class DiffFileInfoDO implements Entity {
    private String fileId;
    private List<DiffFileChunksDO> diffFileChunk;

    @Override
    public String toString() {
        return "DiffFileInfoDO{" +
                "fileId='" + fileId + '\'' +
                ", diffFileChunk=" + diffFileChunk +
                '}';
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public List<DiffFileChunksDO> getDiffFileChunk() {
        return diffFileChunk;
    }

    public void setDiffFileChunk(List<DiffFileChunksDO> diffFileChunk) {
        this.diffFileChunk = diffFileChunk;
    }
}
