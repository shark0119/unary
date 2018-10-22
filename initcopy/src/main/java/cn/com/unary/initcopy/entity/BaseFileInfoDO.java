package cn.com.unary.initcopy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 在初始化时所需要的文件基础信息
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseFileInfoDO implements Serializable {
    private static final long serialVersionUID = 1852745479396286807L;
    private String fileId;
    private Long modifyTime;
    private Long fileSize;
    private String fullName;
    private String checkSum;
    private String backUpPath;
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BaseFileInfoDO) {
            return ((BaseFileInfoDO) obj).getFileId().equals(this.getFileId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getFileId().hashCode();
    }
}
