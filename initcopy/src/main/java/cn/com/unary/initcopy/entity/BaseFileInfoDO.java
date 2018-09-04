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
	private String fileId;
	private long modifyTime;
	private long fileSize;
	private String fullName;

	public String getFullName() {		return fullName;	}
	public void setFullName(String fullName) {		this.fullName = fullName;	}
	public long getModifyTime() {		return modifyTime; }
	public void setModifyTime(long modifyTime) {		this.modifyTime = modifyTime;	}
	public long getFileSize() {		return fileSize;	}
	public void setFileSize(long fileSize) {		this.fileSize = fileSize;	}
    public String getFileId() {        return fileId;    }
    public void setFileId(String fileId) {        this.fileId = fileId;    }

	@Override
	public String toString() {
		return "BaseFileInfoDO{" +
				"fileId='" + fileId + '\'' +
				", modifyTime=" + modifyTime +
				", fileSize=" + fileSize +
				", fullName='" + fullName + '\'' +
				'}';
	}
}
