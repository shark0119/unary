package cn.com.unary.initcopy.entity;

import cn.com.unary.initcopy.common.Entity;
import cn.com.unary.initcopy.grpc.constant.SyncType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 客户端初始化请求实体
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientInitReqDO implements Entity {
    private int taskId;
    private long totalSize;
    private String targetDir;
    private SyncType syncType;
    private List<BaseFileInfoDO> baseFileInfos;
}
