package cn.com.unary.initcopy.entity;

import cn.com.unary.initcopy.grpc.constant.EncryptType;
import cn.com.unary.initcopy.grpc.constant.SyncType;
/**
 * 初始化配置选项。默认的配置中TaskId为-1.
 * @author shark
 *
 */
public class InitOption {

	public static final InitOption DEFAULT = 
			new InitOption(
				EncryptType.ENCRYPT_NONE,
				SyncType.SYNC_ALL, "", -1);
	private EncryptType encryptType;
	private SyncType syncType;
	private String targetDir;
	private int taskId;
	
	public InitOption(EncryptType encryptType,
			SyncType syncType, String targetDir, int taskId) {
		super();
		this.encryptType = encryptType;
		this.syncType = syncType;
		this.targetDir = targetDir;
		this.taskId = taskId;
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public EncryptType getEncryptType() {
		return encryptType;
	}
	public SyncType getSyncType() {
		return syncType;
	}
	public String getTargetDir() {
		return targetDir;
	}
}
