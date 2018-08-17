package cn.com.unary.initcopy.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.0.1)",
    comments = "Source: init_copy.proto")
public class TaskControlServiceGrpc {

  private TaskControlServiceGrpc() {}

  public static final String SERVICE_NAME = "cn.com.unary.initcopy.grpc.TaskControlService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<cn.com.unary.initcopy.grpc.entity.ClientInitReq,
      cn.com.unary.initcopy.grpc.entity.ServerInitResp> METHOD_INIT =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "cn.com.unary.initcopy.grpc.TaskControlService", "Init"),
          io.grpc.protobuf.ProtoUtils.marshaller(cn.com.unary.initcopy.grpc.entity.ClientInitReq.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(cn.com.unary.initcopy.grpc.entity.ServerInitResp.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<cn.com.unary.initcopy.grpc.entity.DeleteTask,
      cn.com.unary.initcopy.grpc.entity.TaskState> METHOD_DELETE =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "cn.com.unary.initcopy.grpc.TaskControlService", "Delete"),
          io.grpc.protobuf.ProtoUtils.marshaller(cn.com.unary.initcopy.grpc.entity.DeleteTask.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(cn.com.unary.initcopy.grpc.entity.TaskState.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<cn.com.unary.initcopy.grpc.entity.ModifyTask,
      cn.com.unary.initcopy.grpc.entity.TaskState> METHOD_MODIFY =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "cn.com.unary.initcopy.grpc.TaskControlService", "Modify"),
          io.grpc.protobuf.ProtoUtils.marshaller(cn.com.unary.initcopy.grpc.entity.ModifyTask.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(cn.com.unary.initcopy.grpc.entity.TaskState.getDefaultInstance()));

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static TaskControlServiceStub newStub(io.grpc.Channel channel) {
    return new TaskControlServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static TaskControlServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new TaskControlServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary and streaming output calls on the service
   */
  public static TaskControlServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new TaskControlServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class TaskControlServiceImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * 任务初始化
     * </pre>
     */
    public void init(cn.com.unary.initcopy.grpc.entity.ClientInitReq request,
        io.grpc.stub.StreamObserver<cn.com.unary.initcopy.grpc.entity.ServerInitResp> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_INIT, responseObserver);
    }

    /**
     * <pre>
     * 删除任务
     * </pre>
     */
    public void delete(cn.com.unary.initcopy.grpc.entity.DeleteTask request,
        io.grpc.stub.StreamObserver<cn.com.unary.initcopy.grpc.entity.TaskState> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_DELETE, responseObserver);
    }

    /**
     * <pre>
     * 修改任务 
     * </pre>
     */
    public void modify(cn.com.unary.initcopy.grpc.entity.ModifyTask request,
        io.grpc.stub.StreamObserver<cn.com.unary.initcopy.grpc.entity.TaskState> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_MODIFY, responseObserver);
    }

    @java.lang.Override public io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_INIT,
            asyncUnaryCall(
              new MethodHandlers<
                cn.com.unary.initcopy.grpc.entity.ClientInitReq,
                cn.com.unary.initcopy.grpc.entity.ServerInitResp>(
                  this, METHODID_INIT)))
          .addMethod(
            METHOD_DELETE,
            asyncUnaryCall(
              new MethodHandlers<
                cn.com.unary.initcopy.grpc.entity.DeleteTask,
                cn.com.unary.initcopy.grpc.entity.TaskState>(
                  this, METHODID_DELETE)))
          .addMethod(
            METHOD_MODIFY,
            asyncUnaryCall(
              new MethodHandlers<
                cn.com.unary.initcopy.grpc.entity.ModifyTask,
                cn.com.unary.initcopy.grpc.entity.TaskState>(
                  this, METHODID_MODIFY)))
          .build();
    }
  }

  /**
   */
  public static final class TaskControlServiceStub extends io.grpc.stub.AbstractStub<TaskControlServiceStub> {
    private TaskControlServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TaskControlServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TaskControlServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TaskControlServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * 任务初始化
     * </pre>
     */
    public void init(cn.com.unary.initcopy.grpc.entity.ClientInitReq request,
        io.grpc.stub.StreamObserver<cn.com.unary.initcopy.grpc.entity.ServerInitResp> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_INIT, getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 删除任务
     * </pre>
     */
    public void delete(cn.com.unary.initcopy.grpc.entity.DeleteTask request,
        io.grpc.stub.StreamObserver<cn.com.unary.initcopy.grpc.entity.TaskState> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_DELETE, getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 修改任务 
     * </pre>
     */
    public void modify(cn.com.unary.initcopy.grpc.entity.ModifyTask request,
        io.grpc.stub.StreamObserver<cn.com.unary.initcopy.grpc.entity.TaskState> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_MODIFY, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class TaskControlServiceBlockingStub extends io.grpc.stub.AbstractStub<TaskControlServiceBlockingStub> {
    private TaskControlServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TaskControlServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TaskControlServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TaskControlServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * 任务初始化
     * </pre>
     */
    public cn.com.unary.initcopy.grpc.entity.ServerInitResp init(cn.com.unary.initcopy.grpc.entity.ClientInitReq request) {
      return blockingUnaryCall(
          getChannel(), METHOD_INIT, getCallOptions(), request);
    }

    /**
     * <pre>
     * 删除任务
     * </pre>
     */
    public cn.com.unary.initcopy.grpc.entity.TaskState delete(cn.com.unary.initcopy.grpc.entity.DeleteTask request) {
      return blockingUnaryCall(
          getChannel(), METHOD_DELETE, getCallOptions(), request);
    }

    /**
     * <pre>
     * 修改任务 
     * </pre>
     */
    public cn.com.unary.initcopy.grpc.entity.TaskState modify(cn.com.unary.initcopy.grpc.entity.ModifyTask request) {
      return blockingUnaryCall(
          getChannel(), METHOD_MODIFY, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class TaskControlServiceFutureStub extends io.grpc.stub.AbstractStub<TaskControlServiceFutureStub> {
    private TaskControlServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TaskControlServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TaskControlServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TaskControlServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * 任务初始化
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<cn.com.unary.initcopy.grpc.entity.ServerInitResp> init(
        cn.com.unary.initcopy.grpc.entity.ClientInitReq request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_INIT, getCallOptions()), request);
    }

    /**
     * <pre>
     * 删除任务
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<cn.com.unary.initcopy.grpc.entity.TaskState> delete(
        cn.com.unary.initcopy.grpc.entity.DeleteTask request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_DELETE, getCallOptions()), request);
    }

    /**
     * <pre>
     * 修改任务 
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<cn.com.unary.initcopy.grpc.entity.TaskState> modify(
        cn.com.unary.initcopy.grpc.entity.ModifyTask request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_MODIFY, getCallOptions()), request);
    }
  }

  private static final int METHODID_INIT = 0;
  private static final int METHODID_DELETE = 1;
  private static final int METHODID_MODIFY = 2;

  private static class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final TaskControlServiceImplBase serviceImpl;
    private final int methodId;

    public MethodHandlers(TaskControlServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_INIT:
          serviceImpl.init((cn.com.unary.initcopy.grpc.entity.ClientInitReq) request,
              (io.grpc.stub.StreamObserver<cn.com.unary.initcopy.grpc.entity.ServerInitResp>) responseObserver);
          break;
        case METHODID_DELETE:
          serviceImpl.delete((cn.com.unary.initcopy.grpc.entity.DeleteTask) request,
              (io.grpc.stub.StreamObserver<cn.com.unary.initcopy.grpc.entity.TaskState>) responseObserver);
          break;
        case METHODID_MODIFY:
          serviceImpl.modify((cn.com.unary.initcopy.grpc.entity.ModifyTask) request,
              (io.grpc.stub.StreamObserver<cn.com.unary.initcopy.grpc.entity.TaskState>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
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
        METHOD_INIT,
        METHOD_DELETE,
        METHOD_MODIFY);
  }

}
