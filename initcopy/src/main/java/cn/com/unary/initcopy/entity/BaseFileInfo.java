package cn.com.unary.initcopy.entity;

import java.io.Serializable;

/**
 * 在初始化时所需要的文件基础信息
 * @author shark
 *
 */
public class BaseFileInfo implements Serializable {

	private static final long serialVersionUID = 1852745479396286807L;
	private int taskId;
	private String id;
	private long modifyTime;
	private long fileSize;
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
}
