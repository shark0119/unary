package cn.com.unary.initcopy.entity;

import cn.com.unary.initcopy.common.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 在初始化时所需要的文件基础信息
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseFileInfoDO implements Entity {

	private static final long serialVersionUID = 1852745479396286807L;
	private String fileId;
	private long modifyTime;
	private long fileSize;
	private String fullName;
	private String checkSum;

}
