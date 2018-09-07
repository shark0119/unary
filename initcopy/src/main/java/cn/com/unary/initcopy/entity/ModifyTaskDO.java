package cn.com.unary.initcopy.entity;

import cn.com.unary.initcopy.grpc.constant.ModifyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 修改任務的实体相关信息
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModifyTaskDO {
    private int taskId;
    private ModifyType modifyType;
    private int speedLimit;
}
