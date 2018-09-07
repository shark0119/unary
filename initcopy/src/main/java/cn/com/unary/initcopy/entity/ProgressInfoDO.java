package cn.com.unary.initcopy.entity;

import cn.com.unary.initcopy.common.Entity;
import lombok.Data;

/**
 * 进程信息
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Data
public class ProgressInfoDO implements Entity {
    private int stage;
    private int progress;
    private long totalFileNum;
    private long totalFileSize;
    private long syncedFileNum;
    private long syncedFileSize;
    private String syncingFileName;
}
