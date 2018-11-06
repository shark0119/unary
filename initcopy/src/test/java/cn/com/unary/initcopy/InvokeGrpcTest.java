package cn.com.unary.initcopy;

import cn.com.unary.initcopy.common.utils.CommonUtils;
import cn.com.unary.initcopy.grpc.constant.ModifyType;
import cn.com.unary.initcopy.grpc.constant.SyncType;
import cn.com.unary.initcopy.grpc.entity.ExecResult;
import cn.com.unary.initcopy.grpc.entity.ModifyTask;
import cn.com.unary.initcopy.grpc.entity.QueryTask;
import cn.com.unary.initcopy.grpc.entity.SyncTarget;
import cn.com.unary.initcopy.grpc.entity.SyncTask;
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
    public static final String GRPC_IP = "127.0.0.1";

    public static void main(String[] args) {
        List<String> syncFiles = new ArrayList<>();
        List<String> targetDirs = new ArrayList<>();

        syncFiles.add("E:\\monitor1");
        targetDirs.add("D:\\target");

        SyncTarget.Builder targetBuilder =
                SyncTarget.newBuilder()
                        .setIp(TARGET_IP)
                        .setGrpcPort(ServerTest.SERVER_INNER_GRPC_PORT)
                        .setTransferPort(ServerTest.SERVER_TRANSFER_PORT);
        SyncTask.Builder addBuilder =
                SyncTask.newBuilder()
                        .setSyncType(SyncType.SYNC_ALL)
                        .addAllTargetDirs(targetDirs)
                        .addAllFiles(syncFiles)
                        .setTargetInfo(targetBuilder);
        ModifyTask.Builder modifyBuilder = ModifyTask.newBuilder()
                .setModifyType(ModifyType.RESUME);
        QueryTask.Builder queryBuilder = QueryTask.newBuilder()
                .setTaskId("");
        InitCopyGrpcClient client = new
                InitCopyGrpcClient(GRPC_IP, ClientTest.CLIENT_GRPC_PORT);

        // add-task
        ExecResult result = client.add(addBuilder.build());
        System.out.println("Result:" + CommonUtils.formatGrpcEntity(result));
        // query-task
        /*TaskState state = client.query(queryBuilder.setTaskId("eb4d9484-3fa1-40fa-9094-3a684155376c").build());
        System.out.println("State:" + CommonUtils.formatGrpcEntity(state));*/
        // modify-task
        ExecResult execResult = client.modify(modifyBuilder.setTaskId("f53b22ae-f33e-46a8-bf95-cdf9cd04facb").build());
        System.out.println(CommonUtils.formatGrpcEntity(execResult));

        client.close();
        while (!client.isTerminated()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }
}
