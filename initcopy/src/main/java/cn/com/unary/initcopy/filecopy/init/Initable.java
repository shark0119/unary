package cn.com.unary.initcopy.filecopy.init;

import cn.com.unary.initcopy.grpc.entity.ServerInitResp;
import cn.com.unary.initcopy.grpc.entity.SyncTask;

/**
 * 初始化模块
 * @author shark
 *
 */
public interface Initable {
	// 源端开始初始化
	ServerInitResp startInit (SyncTask syncTask)throws Exception; 
}