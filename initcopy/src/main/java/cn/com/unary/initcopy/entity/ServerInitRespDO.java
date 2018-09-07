package cn.com.unary.initcopy.entity;

import cn.com.unary.initcopy.common.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 服务端返回的初始化响应
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerInitRespDO implements Entity {
    private int taskId;
    private boolean ready;
    private String msg;
    private List<DiffFileInfoDO> diffFileInfos;
}
