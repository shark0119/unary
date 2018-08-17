package cn.com.unary.initcopy.client;

import java.io.IOException;

import cn.com.unary.initcopy.grpc.TaskServiceGrpc;
import cn.com.unary.initcopy.grpc.entity.DeleteTask;
import cn.com.unary.initcopy.grpc.entity.ModifyTask;
import cn.com.unary.initcopy.grpc.entity.QueryTask;
import cn.com.unary.initcopy.grpc.entity.SyncTask;
import cn.com.unary.initcopy.grpc.entity.TaskState;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

/**
 * 初始化复制模块向外部应用提供的GRPC服务
 * @author shark
 */
public class InitCopyGrpcServer {
	private int port ;
	private Server server;

	private InitCopyGrpcServer (int port) {
		this.port = port;
	}
	private void start() throws IOException {
		server = ServerBuilder.forPort(port).addService(new TaskServiceImpl()).build().start();
		System.out.println("service start...");
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.err.println("*** shutting down gRPC server since JVM is shutting down");
				InitCopyGrpcServer.this.stop();
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

	private class TaskServiceImpl extends TaskServiceGrpc.TaskServiceImplBase {
		@Override
		public void add(SyncTask request, StreamObserver<TaskState> responseObserver) {
			responseObserver.onNext(TaskControlCenter.add(request));
			responseObserver.onCompleted();
		}
		@Override
		public void query(QueryTask request, StreamObserver<TaskState> responseObserver) {
			responseObserver.onNext(TaskControlCenter.query(request));
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
		InitCopyGrpcServer grpcServer = new InitCopyGrpcServer(port);
		grpcServer.start();
		grpcServer.blockUntilShutdown();
	}
}
