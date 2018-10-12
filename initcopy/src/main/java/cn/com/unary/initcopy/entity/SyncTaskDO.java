package cn.com.unary.initcopy.entity;

import cn.com.unary.initcopy.grpc.constant.CompressType;
import cn.com.unary.initcopy.grpc.constant.EncryptType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 存储在源端同步任务的实体
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SyncTaskDO {
    String taskId;
    /**
     * 目标端专用信息
     */
    Long totalFileNum;
    BigDecimal totalSize;
    BigDecimal syncedSize;
    Map<String, Long> fileSizeMap;
    /**
     * 源端专用信息
     */
    String ip;
    Integer grpcPort;
    Integer transferPort;
    CompressType compressType;
    EncryptType encryptType;
    Integer speedLimit;
}
