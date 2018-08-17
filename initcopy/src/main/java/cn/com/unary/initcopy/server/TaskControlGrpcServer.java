package cn.com.unary.initcopy.server;

import java.io.IOException;

import cn.com.unary.initcopy.client.TaskControlCenter;
import cn.com.unary.initcopy.grpc.TaskControlServiceGrpc;
import cn.com.unary.initcopy.grpc.entity.DeleteTask;
import cn.com.unary.initcopy.grpc.entity.ModifyTask;
import cn.com.unary.initcopy.grpc.entity.TaskState;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

/**
 * 源端和目标端之间，任务控制信息GRPC接口的服务提供方
 * @author shark
 *
 */
public class TaskControlGrpcServer {
	private int port ;
	private Server server;

	private TaskControlGrpcServer (int port) {
		this.port = port;
	}
	private void start() throws IOException {
		server = ServerBuilder.forPort(port).addService(new TaskControlServiceImpl()).build().start();
		System.out.println("service start...");
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.err.println("*** shutting down gRPC server since JVM is shutting down");
				TaskControlGrpcServer.this.stop();
				System.err.println("*** server shut down");
			}
		});
	}

	private void stop() {
		synchronized (server) {
			if (server != null) {
				server.shutdown();
			}
		}
	}

	// block 一直到退出程序
	private void blockUntilShutdown() throws InterruptedException {
		synchronized (server) {
			if (server != null) {
				server.awaitTermination();
			}
		}
	}

	private class TaskControlServiceImpl extends TaskControlServiceGrpc.TaskControlServiceImplBase {
		/**
	     * <pre>
	     * 任务初始化
	     * </pre>
	     */
	    public void init(cn.com.unary.initcopy.grpc.entity.ClientInitReq request,
	        StreamObserver<cn.com.unary.initcopy.grpc.entity.ServerInitResp> responseObserver) {
	    	
	    	responseObserver.onCompleted();
	    }
		@Override
		public void delete(DeleteTask request, StreamObserver<TaskState> responseObserver) {
			responseObserver.onNext(TaskControlCenter.delete(request));
			responseObserver.onCompleted();
		}
		@Override
		public void modify(ModifyTask request, StreamObserver<TaskState> responseObserver) {
			responseObserver.onNext(TaskControlCenter.modify(request));
			responseObserver.onCompleted();
		}
	}

	public static void activate (int port) throws IOException, InterruptedException {
		TaskControlGrpcServer grpcServer = new TaskControlGrpcServer(port);
		grpcServer.start();
		grpcServer.blockUntilShutdown();
	}
}
