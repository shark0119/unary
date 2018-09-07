package cn.com.unary.initcopy.entity;

import cn.com.unary.initcopy.common.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 目标端IP及端口信息
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SyncTargetDO implements Entity {
    private String ip;
    private int transferPort;
    private int grpcPort;
}
