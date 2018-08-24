package cn.com.unary.initcopy.server;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 初始化复制目标端服务的启动类
 *
 * 1.启动内部控制任务GRPC服务
 * 2.设置传输模块监听端口，接收外部文件数据包
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Component("InitCopyServer")
@Scope("singleton")
public class InitCopyServer {
	private InitCopyServer() {}

	public static void blockUntilShutdown () {

	}

    /**
     *
     * 1.启动内部控制任务GRPC服务
     * 2.设置传输模块监听端口，接收外部文件数据包
     *
     * @throws IOException 发生IO错误，端口占用等。
     * @throws InterruptedException 服务被异常中断
     */
	public static void start(int grpcPort, int transferPort) throws IOException, InterruptedException  {


	    blockUntilShutdown();
	}
}
