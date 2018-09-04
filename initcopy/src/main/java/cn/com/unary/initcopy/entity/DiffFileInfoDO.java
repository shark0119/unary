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
    private List<DiffFileChunksDO> diffFileChunks;

    @Override
    public String toString() {
        return "DiffFileInfoDO{" +
                "fileId='" + fileId + '\'' +
                ", diffFileChunk=" + diffFileChunks +
                '}';
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public List<DiffFileChunksDO> getDiffFileChunks() {
        return diffFileChunks;
    }

    public void setDiffFileChunks(List<DiffFileChunksDO> diffFileChunk) {
        this.diffFileChunks = diffFileChunk;
    }
}
