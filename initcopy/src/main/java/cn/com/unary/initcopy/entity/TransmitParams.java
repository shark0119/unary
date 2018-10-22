package cn.com.unary.initcopy.entity;

import cn.com.unary.initcopy.grpc.constant.CompressType;
import cn.com.unary.initcopy.grpc.constant.EncryptType;
import lombok.Data;

/**
 * 用于设置传输模块的连接参数
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Data
public class TransmitParams {
    private String ip;
    private Integer port;
    private EncryptType encryptType;
    private CompressType compressType;
    private Integer speedLimit;
}
