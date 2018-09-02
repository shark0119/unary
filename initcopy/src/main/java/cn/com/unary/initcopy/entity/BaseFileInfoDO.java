package cn.com.unary.initcopy.entity;

import cn.com.unary.initcopy.common.Entity;

/**
 * 在初始化时所需要的文件基础信息
 *
 * @author Shark.Yin
 * @since 1.0
 */
public class BaseFileInfoDO implements Entity {

	private static final long serialVersionUID = 1852745479396286807L;
	private Integer taskId;
	private String id;
	private Long modifyTime;
	private Long fileSize;
	private String fullName;
	
	public int getTaskId() {		return taskId;	}
	public void setTaskId(int taskId) {		this.taskId = taskId;	}
	public String getFullName() {		return fullName;	}
	public void setFullName(String fullName) {		this.fullName = fullName;	}
	public String getId() {		return id;	}
	public void setId(String id) {		this.id = id;	}
	public long getModifyTime() {		return modifyTime; }
	public void setModifyTime(long modifyTime) {		this.modifyTime = modifyTime;	}
	public long getFileSize() {		return fileSize;	}
	public void setFileSize(long fileSize) {		this.fileSize = fileSize;	}

	@Override
	public String toString() {
		return "BaseFileInfoDO{" +
				"taskId=" + taskId +
				", id='" + id + '\'' +
				", modifyTime=" + modifyTime +
				", fileSize=" + fileSize +
				", fullName='" + fullName + '\'' +
				'}';
	}
}
