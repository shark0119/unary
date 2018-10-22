package cn.com.unary.initcopy;

import cn.com.unary.initcopy.adapter.TransmitServerAdapter;
import cn.com.unary.initcopy.common.AbstractLoggable;
import cn.com.unary.initcopy.common.ExecExceptionsHandler;
import cn.com.unary.initcopy.grpc.GrpcServiceStarter;
import cn.com.unary.initcopy.service.filecopy.ServerFileCopy;
import io.grpc.BindableService;
import lombok.Setter;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 初始化复制上下文启动类
 * 包括源端和目标端相关参数的初始化
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Component("InitCopyContext")
@Scope("singleton")
public class InitCopyContext extends AbstractLoggable implements Closeable, ApplicationContextAware {
    public static final int TASK_NUMBER = 10;
    public static final Charset CHARSET = StandardCharsets.UTF_8;
    public static final int MAX_PACK_SIZE = 8 * 1024 * 1024;
    public static final int SERVER_CORE_POOL_SIZE = 2;
    public static final int SERVER_MAX_POOL_SIZE = 30;
    public static final int UUID_LEN = UUID.randomUUID().toString().getBytes(InitCopyContext.CHARSET).length;

    private final Object lock;
    /**
     * 面向外部 GRPC 服务监听的端口
     */
    private int grpcPort;
    /**
     * 传输模块监听的端口
     */
    private int transPort;
    private boolean isActive;
    /**
     * 源端与目标端之间 GRPC 通讯监听的端口
     */
    private int innerGrpcPort;
    private ExecutorService exec;
    private GrpcServiceStarter clientStarter;
    private GrpcServiceStarter serverStarter;
    @Autowired
    private TransmitServerAdapter server;
    @Autowired
    @Qualifier("InitCopyGrpcImpl")
    private BindableService icGrpcService;
    @Autowired
    @Qualifier("ControlTaskGrpcService")
    private BindableService ctGrpcService;
    @Setter
    private ApplicationContext applicationContext;

    public InitCopyContext() {
        lock = new Object();
        isActive = false;
    }

    /**
     * 初始化相关变量；
     * 启动向外部提供任务管理的 GRPC 服务（源端）；
     * 启动内部控制任务信息的 GRPC 服务（目标端）；
     * 启动传输模块（目标端）；
     *
     * @param transPort     文件数据传输端口
     * @param grpcPort      服务监听的端口
     * @param innerGrpcPort 内部服务的监听端口
     */
    public void start(int transPort, int grpcPort, int innerGrpcPort) {
        // 判断上下文是否已经启动
        if (!isActive) {
            synchronized (lock) {
                if (!isActive) {
                    // 初始化相关参数
                    this.transPort = transPort;
                    this.grpcPort = grpcPort;
                    this.innerGrpcPort = innerGrpcPort;
                    ThreadFactory executorThreadFactory = new BasicThreadFactory.Builder()
                            .namingPattern("init-copy-context-executor-%d")
                            .uncaughtExceptionHandler(new ExecExceptionsHandler(this))
                            .build();
                    exec = new ThreadPoolExecutor(3,
                            3, 1000L, TimeUnit.MILLISECONDS,
                            new LinkedBlockingQueue<Runnable>(3),
                            executorThreadFactory);
                    this.serverInit().clientInit();
                    exec.shutdown();
                    isActive = Boolean.TRUE;
                }
            }
        }
    }

    @PreDestroy
    @Override
    public void close() throws IOException {
        server.close();
        exec.shutdownNow();
        if (clientStarter != null) {
            clientStarter.close();
        }
        if (serverStarter != null) {
            serverStarter.close();
        }
    }

    private InitCopyContext clientInit() {
        // 启动向外部提供任务管理的 GRPC 服务（源端）
        clientStarter = new GrpcServiceStarter(icGrpcService, getGrpcPort());
        exec.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    clientStarter.start();
                } catch (Exception e) {
                    logger.error(e);
                    throw new IllegalStateException(e);
                }
            }
        });
        return this;
    }

    private InitCopyContext serverInit() {
        final ServerFileCopy fileCopy = applicationContext.getBean("ServerFileCopy", ServerFileCopy.class);
        // 启动和初始化传输模块（目标端）
        exec.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    server.start(getTransPort(), fileCopy);
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
        });
        serverStarter = new GrpcServiceStarter(ctGrpcService, innerGrpcPort);
        // 启动内部控制任务信息的 GRPC 服务（目标端）
        exec.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    serverStarter.start();
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            }
        });
        return this;
    }

    public int getTransPort() {
        return transPort;
    }

    public int getGrpcPort() {
        return grpcPort;
    }

    public int getInnerGrpcPort() {
        return innerGrpcPort;
    }

}
