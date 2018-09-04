package cn.com.unary.initcopy.grpc.linker;

import cn.com.unary.initcopy.InitCopyContext;
import cn.com.unary.initcopy.common.AbstractLoggable;
import cn.com.unary.initcopy.config.BeanConfig;
import cn.com.unary.initcopy.grpc.constant.SyncType;
import cn.com.unary.initcopy.grpc.entity.ExecResult;
import cn.com.unary.initcopy.grpc.entity.SyncTarget;
import cn.com.unary.initcopy.grpc.entity.SyncTask;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InitCopyGrpcLinkerTest extends AbstractLoggable {
    AnnotationConfigApplicationContext ac;
    InitCopyGrpcLinker linker;
    InitCopyContext context;

    public static void main(String[] args) throws Exception {
        args = new String[2];
        args[0] = "G:/test";
        args[1] = "G:";
        InitCopyGrpcLinkerTest test = new InitCopyGrpcLinkerTest();
        test.setUp();
        test.add(args[0], args[1]);
    }

    @Before
    public void setUp() {
        ac = new AnnotationConfigApplicationContext(BeanConfig.class);
        context = ac.getBean(InitCopyContext.class);
        linker = ac.getBean(InitCopyGrpcLinker.class);
    }

    @After
    public void tearDown() {
        //ac.close();
    }

    @Test
    public void add(String source, String target) {
        try {
            // 客户端添加任务。
            SyncTask.Builder builder = SyncTask.newBuilder();
            List<String> syncFiles = new ArrayList<>();
            syncFiles.add(source);
            builder.setTaskId(1)
                    .setSyncType(SyncType.SYNC_ALL)
                    .setTargetDir(target)
                    .addAllFiles(syncFiles)
                    .setTargetInfo(SyncTarget.newBuilder()
                            .setIp("localhost").setTransferPort(80).setGrpcPort(30).build());
            logger.debug("Start add a task.");
            ExecResult result = linker.add(builder.build());
            logger.debug("Task finish");
            Objects.requireNonNull(result);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            logger.error("I got you" + throwable);
        }
    }

    @Test
    public void query() {
        logger.debug("******************logger test ****************");
    }

    @Test
    public void delete() {
    }

    @Test
    public void modify() {
    }
}