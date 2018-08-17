package cn.com.unary.initcopy.client;

import cn.com.unary.initcopy.grpc.entity.DeleteTask;
import cn.com.unary.initcopy.grpc.entity.ModifyTask;
import cn.com.unary.initcopy.grpc.entity.QueryTask;
import cn.com.unary.initcopy.grpc.entity.SyncTask;
import cn.com.unary.initcopy.grpc.entity.TaskState;
/**
 * Should Thread Safe
 * 任务管理中心
 * @author shark
 *
 */
public class TaskControlCenter {
	public static TaskState add (SyncTask task) {
		/*System.out.println(task.getTaskId());
		InfceClientInit aci = Mock.getCI();
		InfceReadPackFromFile rpff = Mock.getRPFF(task.getSyncType());
		try {
			List<String> fIds = aci.startInit(
					task.getTargetInfo().getIp(), 
					task.getTargetInfo().getPort(), 
					null, 
					null);
			rpff.setReadOption(FileReadOption.DEFAULT);
			rpff.registerFileIds(fIds);
			// 一直打包发送直到所有文件全部发送完毕
			byte [] pack;
			while (true) {
				pack = rpff.read(Mock.Transfer.MAX_SIZE);
				if (pack != null) {
					Mock.newTransfer().sendData(pack);
				} else {
					break;
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		return TaskState.newBuilder().setMsg("addSuccess").build();
	}
	public static TaskState query (QueryTask queryTask) {
		System.out.println(queryTask.getTaskId());
		return TaskState.newBuilder().setMsg("querySuccess").build();
	}
	public static TaskState delete (DeleteTask deleteTask) {
		System.out.println(deleteTask.getTaskId());
		return TaskState.newBuilder().setMsg("deleteSuccess").build();
	}
	public static TaskState modify (ModifyTask modifyTask) {
		System.out.println(modifyTask.getTaskId());
		return TaskState.newBuilder().setMsg("modifySuccess").build();
	}
}
