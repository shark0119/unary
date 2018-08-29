package cn.com.unary.initcopy.grpc;

import cn.com.unary.initcopy.common.AbstractLogable;
import io.grpc.BindableService;
import io.grpc.Server;

import java.io.IOException;

/**
 * 用于启动 GRPC 服务，一个服务对应一个启动器
 *
 * @author Shark.Yin
 * @since 1.0
 */
public class GrpcServiceStarter extends AbstractLogable {
    private int port ;
    private Server server;
    private BindableService service;

    /**
     * 创建一个初始化复制的GRPC 服务
     *
     * @param service 一个可绑定的服务
     */
    public GrpcServiceStarter(BindableService service, int port) {
        this.service = service;
        this.port = port;
    }
    public void start() throws IOException, InterruptedException {
        /*server = ServerBuilder.forPort(port)
                .addService(service)
                .build().start();
        logger.debug("service start...");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                logger.error("*** shutting down gRPC server since JVM is shutting down");
                synchronized (server) {
                    if (server != null) {
                        server.shutdown();
                    }
                }
                logger.error("*** server shut down");
            }
        });
        this.blockUntilShutdown();*/
    }

    // block 一直到退出程序
    private void blockUntilShutdown() throws InterruptedException {
        synchronized (server) {
            if (server != null) {
                server.awaitTermination();
            }
        }
    }
}
