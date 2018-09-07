package cn.com.unary.initcopy.entity;

import cn.com.unary.initcopy.common.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 差异文件信息
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiffFileInfoDO implements Entity {
    private String fileId;
    private List<DiffFileChunksDO> diffFileChunks;
}
