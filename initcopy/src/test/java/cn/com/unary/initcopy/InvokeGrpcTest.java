package cn.com.unary.initcopy;

import cn.com.unary.initcopy.common.utils.CommonUtils;
import cn.com.unary.initcopy.grpc.constant.SyncType;
import cn.com.unary.initcopy.grpc.entity.QueryTask;
import cn.com.unary.initcopy.grpc.entity.SyncTarget;
import cn.com.unary.initcopy.grpc.entity.SyncTask;
import cn.com.unary.initcopy.grpc.entity.TaskState;
import cn.com.unary.initcopy.mock.InitCopyGrpcClient;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于发出 GRPC 请求的测试类
 *
 * @author Shark.Yin
 * @since 1.0
 */
public class InvokeGrpcTest {
    public static final String TARGET_IP = "127.0.0.1";
    public static final String GRPC_IP = "10.10.3.207";

    public static void main(String[] args) {
        List<String> syncFiles = new ArrayList<>();
        List<String> targetDirs = new ArrayList<>();

        syncFiles.add("E:\\monitor1");
        targetDirs.add("D:\\target");
        // targetDirs.add("D:\\target\\t2");

        SyncTarget.Builder targetBuilder =
                SyncTarget.newBuilder()
                        .setIp(TARGET_IP)
                        .setGrpcPort(ServerTest.SERVER_INNER_GRPC_PORT)
                        .setTransferPort(ServerTest.SERVER_TRANSFER_PORT);
        // 客户端添加任务。
        SyncTask.Builder builder = SyncTask.newBuilder();
        builder.setSyncType(SyncType.SYNC_ALL)
                .addAllTargetDirs(targetDirs)
                .addAllFiles(syncFiles)
                .setTargetInfo(targetBuilder);

        InitCopyGrpcClient client = new
                InitCopyGrpcClient(GRPC_IP, 10002);

        /*ExecResult result = client.add(builder.build());
        System.out.println("Result:" + CommonUtils.formatGrpcEntity(result));
        TaskState state = client.query(QueryTask.newBuilder()
                .setTaskId(result.getTaskId())
                .build());
        System.out.println("State:" + CommonUtils.formatGrpcEntity(state));*/
        TaskState state = client.query(QueryTask.newBuilder()
                .setTaskId("582fd05f-7e62-4bbc-b909-0fa38fcf4f45")
                .build());
        System.out.println("State:" + CommonUtils.formatGrpcEntity(state));
        client.close();
        while (!client.isTerminated()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }
}
