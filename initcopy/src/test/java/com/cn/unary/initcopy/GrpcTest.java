package com.cn.unary.initcopy;

import java.util.concurrent.TimeUnit;

import cn.com.unary.initcopy.grpc.InitCopyGrpc;
import cn.com.unary.initcopy.grpc.entity.DeleteTask;
import cn.com.unary.initcopy.grpc.entity.ExecResult;
import cn.com.unary.initcopy.grpc.entity.ModifyTask;
import cn.com.unary.initcopy.grpc.entity.QueryTask;
import cn.com.unary.initcopy.grpc.entity.SyncTask;
import cn.com.unary.initcopy.grpc.entity.TaskState;
import io.grpc.internal.ManagedChannelImpl;
import io.grpc.netty.NettyChannelBuilder;

public class GrpcTest {
	private ManagedChannelImpl channel;
	private InitCopyGrpc.InitCopyBlockingStub blockingStub;

	public GrpcTest () {}
	public GrpcTest(String host, int port) {
		channel = NettyChannelBuilder.forAddress(host, port).usePlaintext(true).build();
		blockingStub = InitCopyGrpc.newBlockingStub(channel);
	}

	public void shutdown() throws InterruptedException {
		channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	}

	public void add() {
		SyncTask request = SyncTask.newBuilder().setTaskId(11).build();
		ExecResult state = blockingStub.add(request);
		System.out.println(state.getMsg());
	}
	public void query() {
		QueryTask request = QueryTask.newBuilder().setTaskId(11).build();
		TaskState state = blockingStub.query(request);
		System.out.println(state.getExecResult().getMsg());
	}
	public void delete() {
		DeleteTask request = DeleteTask.newBuilder().setTaskId(11).build();
		ExecResult state = blockingStub.delete(request);
		System.out.println(state.getMsg());
	}
	public void modify() {
		ModifyTask request = ModifyTask.newBuilder().setTaskId(11).build();
		ExecResult state = blockingStub.modify(request);
		System.out.println(state.getMsg());
	}
}
