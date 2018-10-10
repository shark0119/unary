package cn.com.unary.initcopy;

import cn.com.unary.initcopy.grpc.constant.SyncType;
import cn.com.unary.initcopy.grpc.entity.QueryTask;
import cn.com.unary.initcopy.grpc.entity.SyncTarget;
import cn.com.unary.initcopy.grpc.entity.SyncTask;
import cn.com.unary.initcopy.grpc.linker.InitCopyGrpcLinker;
import cn.com.unary.initcopy.mock.InitCopyGrpcClient;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class ClientTest {

    private static AnnotationConfigApplicationContext ac;
    private static InitCopyGrpcLinker linker;

    private static void setUp() {
        /*ac = new AnnotationConfigApplicationContext(BeanConfig.class);
        InitCopyContext context = ac.getBean(InitCopyContext.class);
        linker = ac.getBean(InitCopyGrpcLinker.class);
        context.start(50005, 50006, 50007);*/

    }

    public static void main(String[] args) {
        setUp();
        String source = "G:\\test";
        String targetDir = "G:/";
        // 客户端添加任务。
        SyncTask.Builder builder = SyncTask.newBuilder();
        List<String> syncFiles = new ArrayList<>();
        syncFiles.add(source);
        builder.setTaskId(1)
                .setSyncType(SyncType.SYNC_ALL)
                .setTargetDir(targetDir)
                .addAllFiles(syncFiles)
                .setTargetInfo(SyncTarget.newBuilder()
                        .setIp("127.0.0.1")
                        .setTransferPort(10001)
                        .setGrpcPort(10003)
                        .build());
        InitCopyGrpcClient client = new
                InitCopyGrpcClient("127.0.0.1", 10002);
        System.out.println(client.add(builder.build()));
        System.out.println(client.query(QueryTask.newBuilder().setTaskId(1).build()));

    }
}
