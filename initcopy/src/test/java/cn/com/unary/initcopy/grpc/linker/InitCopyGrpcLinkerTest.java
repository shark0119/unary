package cn.com.unary.initcopy.grpc.linker;

import cn.com.unary.initcopy.InitCopyContext;
import cn.com.unary.initcopy.config.BeanConfig;
import cn.com.unary.initcopy.grpc.constant.SyncType;
import cn.com.unary.initcopy.grpc.entity.ExecResult;
import cn.com.unary.initcopy.grpc.entity.SyncTarget;
import cn.com.unary.initcopy.grpc.entity.SyncTask;
import cn.com.unary.initcopy.common.AbstractLogable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InitCopyGrpcLinkerTest extends AbstractLogable {
    AnnotationConfigApplicationContext ac;
    InitCopyGrpcLinker linker;
    InitCopyContext context;

    @Before
    public void setUp() throws Exception {
        ac = new AnnotationConfigApplicationContext(BeanConfig.class);
        context = ac.getBean(InitCopyContext.class);
        linker = ac.getBean(InitCopyGrpcLinker.class);
    }
    @After
    public void tearDown() throws Exception {
        //ac.close();
    }

    @Test
    public void add() throws IOException, InterruptedException {
        try {
            // 启动服务端
            context.start(34567,34568,34569);
            // 客户端添加任务。
            SyncTask.Builder builder = SyncTask.newBuilder();
            List<String> syncFiles = new ArrayList<>();
            syncFiles.add("E:\\Shark_File\\Download\\CentOS-6.0-x86_64-bin-DVD1.iso");
            builder.setTaskId(1)
                    .setSyncType(SyncType.SYNC_ALL)
                    .setTargetDir("F:/")
                    .addAllFile(syncFiles)
                    .setTargetInfo(SyncTarget.newBuilder().setIp("localhost").setPort(80).build());
            logger.debug("Start add a task.");
            ExecResult result = linker.add(builder.build());
            logger.debug("Task finish");
            Objects.requireNonNull(result);
        } catch (Throwable throwable) {
            System.out.println("I got you");
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

    public static void main(String[] args) throws Exception {
        InitCopyGrpcLinkerTest test = new InitCopyGrpcLinkerTest();
        test.setUp();
        test.add();
    }
}