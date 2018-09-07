package cn.com.unary.initcopy.entity;

import cn.com.unary.initcopy.common.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 任务状态实体
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskStateDO implements Entity {
    private int taskId;
    private ExecResultDO execResult;
    private ProgressInfoDO progressInfo;
}
