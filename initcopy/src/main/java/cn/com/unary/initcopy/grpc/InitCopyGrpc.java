package cn.com.unary.initcopy.grpc;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.0.1)",
    comments = "Source: init_copy.proto")
public class InitCopyGrpc {

  private InitCopyGrpc() {}

  public static final String SERVICE_NAME = "cn.com.unary.initcopy.grpc.InitCopy";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<cn.com.unary.initcopy.grpc.entity.SyncTask,
      cn.com.unary.initcopy.grpc.entity.ExecResult> METHOD_ADD =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "cn.com.unary.initcopy.grpc.InitCopy", "Add"),
          io.grpc.protobuf.ProtoUtils.marshaller(cn.com.unary.initcopy.grpc.entity.SyncTask.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(cn.com.unary.initcopy.grpc.entity.ExecResult.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<cn.com.unary.initcopy.grpc.entity.QueryTask,
      cn.com.unary.initcopy.grpc.entity.TaskState> METHOD_QUERY =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "cn.com.unary.initcopy.grpc.InitCopy", "Query"),
          io.grpc.protobuf.ProtoUtils.marshaller(cn.com.unary.initcopy.grpc.entity.QueryTask.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(cn.com.unary.initcopy.grpc.entity.TaskState.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<cn.com.unary.initcopy.grpc.entity.DeleteTask,
      cn.com.unary.initcopy.grpc.entity.ExecResult> METHOD_DELETE =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "cn.com.unary.initcopy.grpc.InitCopy", "Delete"),
          io.grpc.protobuf.ProtoUtils.marshaller(cn.com.unary.initcopy.grpc.entity.DeleteTask.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(cn.com.unary.initcopy.grpc.entity.ExecResult.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<cn.com.unary.initcopy.grpc.entity.ModifyTask,
      cn.com.unary.initcopy.grpc.entity.ExecResult> METHOD_MODIFY =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "cn.com.unary.initcopy.grpc.InitCopy", "Modify"),
          io.grpc.protobuf.ProtoUtils.marshaller(cn.com.unary.initcopy.grpc.entity.ModifyTask.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(cn.com.unary.initcopy.grpc.entity.ExecResult.getDefaultInstance()));

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static InitCopyStub newStub(io.grpc.Channel channel) {
    return new InitCopyStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static InitCopyBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new InitCopyBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary and streaming output calls on the service
   */
  public static InitCopyFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new InitCopyFutureStub(channel);
  }

  /**
   */
  public static abstract class InitCopyImplBase implements io.grpc.BindableService {

    /**
     */
    public void add(cn.com.unary.initcopy.grpc.entity.SyncTask request,
        io.grpc.stub.StreamObserver<cn.com.unary.initcopy.grpc.entity.ExecResult> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_ADD, responseObserver);
    }

    /**
     */
    public void query(cn.com.unary.initcopy.grpc.entity.QueryTask request,
        io.grpc.stub.StreamObserver<cn.com.unary.initcopy.grpc.entity.TaskState> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_QUERY, responseObserver);
    }

    /**
     */
    public void delete(cn.com.unary.initcopy.grpc.entity.DeleteTask request,
        io.grpc.stub.StreamObserver<cn.com.unary.initcopy.grpc.entity.ExecResult> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_DELETE, responseObserver);
    }

    /**
     */
    public void modify(cn.com.unary.initcopy.grpc.entity.ModifyTask request,
        io.grpc.stub.StreamObserver<cn.com.unary.initcopy.grpc.entity.ExecResult> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_MODIFY, responseObserver);
    }

    @java.lang.Override public io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_ADD,
            asyncUnaryCall(
              new MethodHandlers<
                cn.com.unary.initcopy.grpc.entity.SyncTask,
                cn.com.unary.initcopy.grpc.entity.ExecResult>(
                  this, METHODID_ADD)))
          .addMethod(
            METHOD_QUERY,
            asyncUnaryCall(
              new MethodHandlers<
                cn.com.unary.initcopy.grpc.entity.QueryTask,
                cn.com.unary.initcopy.grpc.entity.TaskState>(
                  this, METHODID_QUERY)))
          .addMethod(
            METHOD_DELETE,
            asyncUnaryCall(
              new MethodHandlers<
                cn.com.unary.initcopy.grpc.entity.DeleteTask,
                cn.com.unary.initcopy.grpc.entity.ExecResult>(
                  this, METHODID_DELETE)))
          .addMethod(
            METHOD_MODIFY,
            asyncUnaryCall(
              new MethodHandlers<
                cn.com.unary.initcopy.grpc.entity.ModifyTask,
                cn.com.unary.initcopy.grpc.entity.ExecResult>(
                  this, METHODID_MODIFY)))
          .build();
    }
  }

  /**
   */
  public static final class InitCopyStub extends io.grpc.stub.AbstractStub<InitCopyStub> {
    private InitCopyStub(io.grpc.Channel channel) {
      super(channel);
    }

    private InitCopyStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected InitCopyStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new InitCopyStub(channel, callOptions);
    }

    /**
     */
    public void add(cn.com.unary.initcopy.grpc.entity.SyncTask request,
        io.grpc.stub.StreamObserver<cn.com.unary.initcopy.grpc.entity.ExecResult> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_ADD, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void query(cn.com.unary.initcopy.grpc.entity.QueryTask request,
        io.grpc.stub.StreamObserver<cn.com.unary.initcopy.grpc.entity.TaskState> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_QUERY, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void delete(cn.com.unary.initcopy.grpc.entity.DeleteTask request,
        io.grpc.stub.StreamObserver<cn.com.unary.initcopy.grpc.entity.ExecResult> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_DELETE, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void modify(cn.com.unary.initcopy.grpc.entity.ModifyTask request,
        io.grpc.stub.StreamObserver<cn.com.unary.initcopy.grpc.entity.ExecResult> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_MODIFY, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class InitCopyBlockingStub extends io.grpc.stub.AbstractStub<InitCopyBlockingStub> {
    private InitCopyBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private InitCopyBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected InitCopyBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new InitCopyBlockingStub(channel, callOptions);
    }

    /**
     */
    public cn.com.unary.initcopy.grpc.entity.ExecResult add(cn.com.unary.initcopy.grpc.entity.SyncTask request) {
      return blockingUnaryCall(
          getChannel(), METHOD_ADD, getCallOptions(), request);
    }

    /**
     */
    public cn.com.unary.initcopy.grpc.entity.TaskState query(cn.com.unary.initcopy.grpc.entity.QueryTask request) {
      return blockingUnaryCall(
          getChannel(), METHOD_QUERY, getCallOptions(), request);
    }

    /**
     */
    public cn.com.unary.initcopy.grpc.entity.ExecResult delete(cn.com.unary.initcopy.grpc.entity.DeleteTask request) {
      return blockingUnaryCall(
          getChannel(), METHOD_DELETE, getCallOptions(), request);
    }

    /**
     */
    public cn.com.unary.initcopy.grpc.entity.ExecResult modify(cn.com.unary.initcopy.grpc.entity.ModifyTask request) {
      return blockingUnaryCall(
          getChannel(), METHOD_MODIFY, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class InitCopyFutureStub extends io.grpc.stub.AbstractStub<InitCopyFutureStub> {
    private InitCopyFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private InitCopyFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected InitCopyFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new InitCopyFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<cn.com.unary.initcopy.grpc.entity.ExecResult> add(
        cn.com.unary.initcopy.grpc.entity.SyncTask request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_ADD, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<cn.com.unary.initcopy.grpc.entity.TaskState> query(
        cn.com.unary.initcopy.grpc.entity.QueryTask request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_QUERY, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<cn.com.unary.initcopy.grpc.entity.ExecResult> delete(
        cn.com.unary.initcopy.grpc.entity.DeleteTask request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_DELETE, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<cn.com.unary.initcopy.grpc.entity.ExecResult> modify(
        cn.com.unary.initcopy.grpc.entity.ModifyTask request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_MODIFY, getCallOptions()), request);
    }
  }

  private static final int METHODID_ADD = 0;
  private static final int METHODID_QUERY = 1;
  private static final int METHODID_DELETE = 2;
  private static final int METHODID_MODIFY = 3;

  private static class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final InitCopyImplBase serviceImpl;
    private final int methodId;

    public MethodHandlers(InitCopyImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_ADD:
          serviceImpl.add((cn.com.unary.initcopy.grpc.entity.SyncTask) request,
              (io.grpc.stub.StreamObserver<cn.com.unary.initcopy.grpc.entity.ExecResult>) responseObserver);
          break;
        case METHODID_QUERY:
          serviceImpl.query((cn.com.unary.initcopy.grpc.entity.QueryTask) request,
              (io.grpc.stub.StreamObserver<cn.com.unary.initcopy.grpc.entity.TaskState>) responseObserver);
          break;
        case METHODID_DELETE:
          serviceImpl.delete((cn.com.unary.initcopy.grpc.entity.DeleteTask) request,
              (io.grpc.stub.StreamObserver<cn.com.unary.initcopy.grpc.entity.ExecResult>) responseObserver);
          break;
        case METHODID_MODIFY:
          serviceImpl.modify((cn.com.unary.initcopy.grpc.entity.ModifyTask) request,
              (io.grpc.stub.StreamObserver<cn.com.unary.initcopy.grpc.entity.ExecResult>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    return new io.grpc.ServiceDescriptor(SERVICE_NAME,
        METHOD_ADD,
        METHOD_QUERY,
        METHOD_DELETE,
        METHOD_MODIFY);
  }

}
