package cn.com.unary.initcopy.entity;

import cn.com.unary.initcopy.common.Entity;
import cn.com.unary.initcopy.grpc.constant.CompressType;
import cn.com.unary.initcopy.grpc.constant.EncryptType;
import cn.com.unary.initcopy.grpc.constant.PackType;
import cn.com.unary.initcopy.grpc.constant.SyncType;
import lombok.Data;

import java.util.List;

/**
 * 同步任务实体
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Data
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
}
