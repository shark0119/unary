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

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadFactory;

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
            syncFiles.add("C:\\Users\\shark\\Desktop\\文件\\极客时间   趣谈网络协议_files\\0.js");
            builder.setTaskId(1)
                    .setSyncType(SyncType.SYNC_ALL)
                    .setTargetDir("G:/")
                    .addAllFile(syncFiles)
                    .setTargetInfo(SyncTarget.newBuilder().setIp("localhost").setPort(80).build());
            logger.debug("Start add a task.");
            ExecResult result = linker.add(builder.build());
            // Thread.sleep(10000);
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
        File file = new File("G:\\java\\unary\\java_pid27908.hprof");
        file.createNewFile();
        Path path = Paths.get("G:\\java\\unary\\java_pid27908.hprof");
        FileChannel channel = FileChannel.open(path, StandardOpenOption.WRITE);
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.putInt(2);
        buffer.putInt(2);
        for (int i=0; i<Integer.MAX_VALUE; i++) {
            channel.write(buffer);
            channel.force(true);
        }
    }
}