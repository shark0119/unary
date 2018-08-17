package cn.com.unary.initcopy.entity;

import cn.com.unary.initcopy.grpc.constant.SyncType;

public class FileReadOption {
	public static final FileReadOption DEFAULT 
		= new FileReadOption(SyncType.SYNC_ALL);;
	private SyncType syncType;
	
	public FileReadOption(SyncType syncType) {
		super();
		this.syncType = syncType;
	}
	public SyncType getSyncType() {
		return syncType;
	}
}
