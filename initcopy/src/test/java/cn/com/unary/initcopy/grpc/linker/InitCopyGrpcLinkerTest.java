package cn.com.unary.initcopy.grpc.linker;

import cn.com.unary.initcopy.InitCopyContext;
import cn.com.unary.initcopy.config.BeanConfig;
import cn.com.unary.initcopy.grpc.constant.SyncType;
import cn.com.unary.initcopy.grpc.entity.ExecResult;
import cn.com.unary.initcopy.grpc.entity.SyncTarget;
import cn.com.unary.initcopy.grpc.entity.SyncTask;
import cn.com.unary.initcopy.utils.AbstractLogable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.Assert.*;

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
        ac.close();
    }

    @Test
    public void add() throws IOException, InterruptedException {
        // 启动服务端
        context.start(10,10,20);
        // 客户端添加任务。
        SyncTask.Builder builder = SyncTask.newBuilder();
        List<String> files = new ArrayList<>();
        builder.setTaskId(1)
                .setSyncType(SyncType.SYNC_ALL)
                .setTargetDir("G:/")
                .addAllFile(files)
                .setTargetInfo(SyncTarget.newBuilder().setIp("localhost").setPort(80).build());
        ExecResult result = linker.add(builder.build());
        Objects.requireNonNull(result);
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