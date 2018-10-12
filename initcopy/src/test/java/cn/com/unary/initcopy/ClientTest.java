package cn.com.unary.initcopy;

import cn.com.unary.initcopy.common.utils.CommonUtils;
import cn.com.unary.initcopy.config.BeanConfig;
import cn.com.unary.initcopy.grpc.constant.SyncType;
import cn.com.unary.initcopy.grpc.entity.QueryTask;
import cn.com.unary.initcopy.grpc.entity.SyncTarget;
import cn.com.unary.initcopy.grpc.entity.SyncTask;
import cn.com.unary.initcopy.grpc.entity.TaskState;
import cn.com.unary.initcopy.mock.InitCopyGrpcClient;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class ClientTest {

    private static AnnotationConfigApplicationContext ac;

    private static void setUp() {
        ac = new AnnotationConfigApplicationContext(BeanConfig.class);
        InitCopyContext context = ac.getBean(InitCopyContext.class);
        context.start(50005, 50006, 50007);
    }

    public static void main(String[] args) {
        setUp();
        List<String> syncFiles = new ArrayList<>();
        List<String> targetDirs = new ArrayList<>();
        // 客户端添加任务。
        SyncTask.Builder builder = SyncTask.newBuilder();

        syncFiles.add("G:\\temp\\rpc");
        syncFiles.add("G:\\tc");
        targetDirs.add("D:\\target\\t1");
        targetDirs.add("D:\\target\\t2");

        builder.setSyncType(SyncType.SYNC_ALL)
                .addAllTargetDirs(targetDirs)
                .addAllFiles(syncFiles)
                .setTargetInfo(SyncTarget.newBuilder()
                        .setIp("127.0.0.1")
                        .setTransferPort(ServerTest.SERVER_TRANSFER_PORT)
                        .setGrpcPort(ServerTest.SERVER_INNER_GRPC_PORT)
                        .build());
        InitCopyGrpcClient client = new
                InitCopyGrpcClient("127.0.0.1", ServerTest.SERVER_GRPC_PORT);
        /*ExecResult result = client.add(builder.build());
        System.out.println(CommonUtils.formatGrpcEntity(result));*/
        TaskState state = client.query(QueryTask.newBuilder()
                .setTaskId("e037a2c2-91ce-404b-abd4-9afa51021204")
                // .setTaskId(result.getTaskId())
                .build());
        System.out.println(CommonUtils.formatGrpcEntity(state));
    }
}
