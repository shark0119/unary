package cn.com.unary.initcopy.filecopy;

import cn.com.unary.initcopy.grpc.entity.SyncTask;

public interface InfceFileCopy {
	void addTask (SyncTask syncTask)throws Exception;// 添加任务
}
