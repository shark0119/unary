package cn.com.unary.initcopy;

import api.UnaryProcess;
import api.UnaryTServer;
import cn.com.unary.initcopy.grpc.GrpcServiceStarter;
import cn.com.unary.initcopy.grpc.linker.ControlTaskGrpcLinker;
import cn.com.unary.initcopy.grpc.linker.InitCopyGrpcLinker;
import cn.com.unary.initcopy.grpc.service.ControlTaskGrpcImpl;
import cn.com.unary.initcopy.grpc.service.InitCopyGrpcImpl;
import cn.com.unary.initcopy.utils.AbstractLogable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

/**
 * 初始化复制上下文启动类
 * 包括源端和目标端相关参数的初始化
 *
 * @author shark
 */
@Component("InitCopyContext")
@Scope("singleton")
public class InitCopyContext extends AbstractLogable {
    public static int CONTROL_TASK_GRPC_PORT = 23456;
    // 全局的属性
    public final static String CHARSET = "UTF-8";
    // 传输模块监听的端口
    protected static int transPort;
    // 面向外部 GRPC 服务监听的端口
    protected static int grpcPort;
    // 源端与目标端之间 GRPC 通讯监听的端口
    protected static int innerGrpcPort = 6002;
    protected static UnaryTServer uts;
    private static volatile Boolean isActive = Boolean.FALSE;
    @Autowired
    protected UnaryProcess process;
    @Autowired
    @Qualifier("contextExecutor")
    protected ExecutorService exec;

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
            synchronized (isActive) {
                if (!isActive) {
                    // 初始化相关参数
                    InitCopyContext.transPort = transPort;
                    InitCopyContext.grpcPort = grpcPort;
                    InitCopyContext.innerGrpcPort = innerGrpcPort;
                    this.serverInit().clientInit();
                    isActive = Boolean.TRUE;
                }
            }
        }
    }

    @PreDestroy
    public void destroy() {
        uts = null;
        System.out.println("Context destroy");
        throw new IllegalStateException("eeee");
    }

    @Autowired
    public InitCopyContext setUts(UnaryTServer uts) {
        InitCopyContext.uts = uts;
        return this;
    }

    private InitCopyContext clientInit() throws IOException, InterruptedException {
        // 启动向外部提供任务管理的 GRPC 服务（源端）
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new GrpcServiceStarter(new InitCopyGrpcImpl(new InitCopyGrpcLinker()), grpcPort).start();
                } catch (Exception e) {
                    logger.error(e);
                    throw new IllegalStateException(e);
                }
            }
        }, "InitCopyGrpcService");
        exec.execute(thread);
        return this;
    }

    private InitCopyContext serverInit() throws IOException, InterruptedException {
        // 启动和初始化传输模块（目标端）
        uts.startServer();
        uts.setProcess(process);
        // 启动内部控制任务信息的 GRPC 服务（目标端）
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new GrpcServiceStarter(new ControlTaskGrpcImpl(new ControlTaskGrpcLinker()), grpcPort).start();
                } catch (Exception e) {
                    logger.error(e);
                    throw new IllegalStateException(e);
                }
            }
        }, "ControlTaskGrpcService");
        exec.execute(thread);
        return this;
    }
}
