package cn.com.unary.initcopy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 任务删除的相关信息
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteTaskDO {
    private int taskId;
    private boolean deleteFile;
}
