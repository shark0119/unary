package cn.com.unary.initcopy;

import api.UnaryProcess;
import api.UnaryTServer;
import cn.com.unary.initcopy.common.AbstractLoggable;
import cn.com.unary.initcopy.grpc.GrpcServiceStarter;
import cn.com.unary.initcopy.grpc.linker.ControlTaskGrpcLinker;
import cn.com.unary.initcopy.grpc.linker.InitCopyGrpcLinker;
import cn.com.unary.initcopy.grpc.service.ControlTaskGrpcImpl;
import cn.com.unary.initcopy.grpc.service.InitCopyGrpcImpl;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 初始化复制上下文启动类
 * 包括源端和目标端相关参数的初始化
 *
 * @author shark
 */
@Component("InitCopyContext")
@Scope("singleton")
public class InitCopyContext extends AbstractLoggable {
    /**
     * 以下全是全局的属性
     */
    public final static String CHARSET = "UTF-8";
    private ExecutorService exec;
    /**
     * 传输模块监听的端口
     */
    private static int transPort;
    /**
     * 面向外部 GRPC 服务监听的端口
     */
    private static int grpcPort;
    private static UnaryTServer uts;
    /**
     * 源端与目标端之间 GRPC 通讯监听的端口
     */
    private static int innerGrpcPort;
    private GrpcServiceStarter clientStarter;
    private GrpcServiceStarter serverStarter;
    private static final Object LOCK = new Object();
    private static Boolean isActive = Boolean.FALSE;
    @Autowired
    protected UnaryProcess process;

    public InitCopyContext() {
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
     * @throws InterruptedException 服务被中断
     * @throws IOException          端口占用
     */
    public void start(int transPort, int grpcPort, int innerGrpcPort) throws IOException, InterruptedException {
        // 判断上下文是否已经启动
        if (!isActive) {
            synchronized (LOCK) {
                if (!isActive) {
                    // 初始化相关参数
                    InitCopyContext.transPort = transPort;
                    InitCopyContext.grpcPort = grpcPort;
                    InitCopyContext.innerGrpcPort = innerGrpcPort;
                    ThreadFactory executorThreadFactory = new BasicThreadFactory.Builder()
                            .namingPattern("init-copy-context-executor-%d")
                            .uncaughtExceptionHandler(new ContextExecExceptHandler())
                            .build();
                    exec = new ThreadPoolExecutor(2,
                            2, 1000L, TimeUnit.MILLISECONDS,
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
    public void destroy() throws IOException {
        uts.stopServer();
        exec.shutdownNow();
        if (clientStarter != null) {
            clientStarter.shutdown();
        }
        if (serverStarter != null) {
            serverStarter.shutdown();
        }
    }

    @Autowired
    public InitCopyContext setUts(UnaryTServer uts) {
        InitCopyContext.uts = uts;
        return this;
    }

    private InitCopyContext clientInit() throws IOException, InterruptedException {
        // 启动向外部提供任务管理的 GRPC 服务（源端）
        clientStarter = new GrpcServiceStarter(new InitCopyGrpcImpl(new InitCopyGrpcLinker()), grpcPort);
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

    private InitCopyContext serverInit() throws IOException, InterruptedException {
        // 启动和初始化传输模块（目标端）
        uts.startServer();
        uts.setProcess(process);
        serverStarter = new GrpcServiceStarter(new ControlTaskGrpcImpl(new ControlTaskGrpcLinker()), grpcPort);
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

    private class ContextExecExceptHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            logger.error(t.getName(), e);
            try {
                InitCopyContext.this.destroy();
            } catch (IOException e1) {
                logger.error(e1);
            }
        }
    }
}
