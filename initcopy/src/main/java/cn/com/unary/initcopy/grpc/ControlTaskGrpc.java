package cn.com.unary.initcopy.grpc;

import cn.com.unary.initcopy.common.AbstractLoggable;

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
        comments = "Source: task_control.proto")
public class ControlTaskGrpc {

  private ControlTaskGrpc() {}

  public static final String SERVICE_NAME = "cn.com.unary.initcopy.grpc.ControlTask";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<cn.com.unary.initcopy.grpc.entity.ClientInitReq,
      cn.com.unary.initcopy.grpc.entity.ServerInitResp> METHOD_INIT =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "cn.com.unary.initcopy.grpc.ControlTask", "Init"),
          io.grpc.protobuf.ProtoUtils.marshaller(cn.com.unary.initcopy.grpc.entity.ClientInitReq.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(cn.com.unary.initcopy.grpc.entity.ServerInitResp.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<cn.com.unary.initcopy.grpc.entity.DeleteTask,
      cn.com.unary.initcopy.grpc.entity.ExecResult> METHOD_DELETE =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "cn.com.unary.initcopy.grpc.ControlTask", "Delete"),
          io.grpc.protobuf.ProtoUtils.marshaller(cn.com.unary.initcopy.grpc.entity.DeleteTask.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(cn.com.unary.initcopy.grpc.entity.ExecResult.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<cn.com.unary.initcopy.grpc.entity.ResumeTask,
          cn.com.unary.initcopy.grpc.entity.SyncProcess> METHOD_RESUME =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
                  "cn.com.unary.initcopy.grpc.ControlTask", "Resume"),
              io.grpc.protobuf.ProtoUtils.marshaller(cn.com.unary.initcopy.grpc.entity.ResumeTask.getDefaultInstance()),
              io.grpc.protobuf.ProtoUtils.marshaller(cn.com.unary.initcopy.grpc.entity.SyncProcess.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<cn.com.unary.initcopy.grpc.entity.QueryTask,
          cn.com.unary.initcopy.grpc.entity.TaskState> METHOD_QUERY =
          io.grpc.MethodDescriptor.create(
                  io.grpc.MethodDescriptor.MethodType.UNARY,
                  generateFullMethodName(
                          "cn.com.unary.initcopy.grpc.ControlTask", "Query"),
                  io.grpc.protobuf.ProtoUtils.marshaller(cn.com.unary.initcopy.grpc.entity.QueryTask.getDefaultInstance()),
                  io.grpc.protobuf.ProtoUtils.marshaller(cn.com.unary.initcopy.grpc.entity.TaskState.getDefaultInstance()));

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ControlTaskStub newStub(io.grpc.Channel channel) {
    return new ControlTaskStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ControlTaskBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new ControlTaskBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary and streaming output calls on the service
   */
  public static ControlTaskFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new ControlTaskFutureStub(channel);
  }

  private static final int METHODID_RESUME = 2;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    return new io.grpc.ServiceDescriptor(SERVICE_NAME,
            METHOD_INIT,
            METHOD_DELETE,
            METHOD_RESUME,
            METHOD_QUERY);
  }

  /**
   */
  public static abstract class ControlTaskImplBase extends AbstractLoggable implements io.grpc.BindableService {

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
        io.grpc.stub.StreamObserver<cn.com.unary.initcopy.grpc.entity.ExecResult> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_DELETE, responseObserver);
    }

    /**
     * <pre>
     * 唤醒任务
     * </pre>
     */
    public void resume(cn.com.unary.initcopy.grpc.entity.ResumeTask request,
                       io.grpc.stub.StreamObserver<cn.com.unary.initcopy.grpc.entity.SyncProcess> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_RESUME, responseObserver);
    }

    /**
     * <pre>
     * 查询任务
     * </pre>
     */
    public void query(cn.com.unary.initcopy.grpc.entity.QueryTask request,
                      io.grpc.stub.StreamObserver<cn.com.unary.initcopy.grpc.entity.TaskState> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_QUERY, responseObserver);
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
                cn.com.unary.initcopy.grpc.entity.ExecResult>(
                  this, METHODID_DELETE)))
              .addMethod(
                      METHOD_RESUME,
            asyncUnaryCall(
              new MethodHandlers<
                      cn.com.unary.initcopy.grpc.entity.ResumeTask,
                      cn.com.unary.initcopy.grpc.entity.SyncProcess>(
                      this, METHODID_RESUME)))
              .addMethod(
                      METHOD_QUERY,
                      asyncUnaryCall(
                              new MethodHandlers<
                                      cn.com.unary.initcopy.grpc.entity.QueryTask,
                                      cn.com.unary.initcopy.grpc.entity.TaskState>(
                                      this, METHODID_QUERY)))
          .build();
    }
  }

  /**
   */
  public static final class ControlTaskStub extends io.grpc.stub.AbstractStub<ControlTaskStub> {
    private ControlTaskStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ControlTaskStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ControlTaskStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ControlTaskStub(channel, callOptions);
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
        io.grpc.stub.StreamObserver<cn.com.unary.initcopy.grpc.entity.ExecResult> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_DELETE, getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 唤醒任务
     * </pre>
     */
    public void resume(cn.com.unary.initcopy.grpc.entity.ResumeTask request,
                       io.grpc.stub.StreamObserver<cn.com.unary.initcopy.grpc.entity.SyncProcess> responseObserver) {
      asyncUnaryCall(
              getChannel().newCall(METHOD_RESUME, getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * 查询任务
     * </pre>
     */
    public void query(cn.com.unary.initcopy.grpc.entity.QueryTask request,
                      io.grpc.stub.StreamObserver<cn.com.unary.initcopy.grpc.entity.TaskState> responseObserver) {
      asyncUnaryCall(
              getChannel().newCall(METHOD_QUERY, getCallOptions()), request, responseObserver);
    }
  }

  private static final int METHODID_INIT = 0;
  private static final int METHODID_DELETE = 1;

  /**
   */
  public static final class ControlTaskBlockingStub extends io.grpc.stub.AbstractStub<ControlTaskBlockingStub> {
    private ControlTaskBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ControlTaskBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ControlTaskBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ControlTaskBlockingStub(channel, callOptions);
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
    public cn.com.unary.initcopy.grpc.entity.ExecResult delete(cn.com.unary.initcopy.grpc.entity.DeleteTask request) {
      return blockingUnaryCall(
          getChannel(), METHOD_DELETE, getCallOptions(), request);
    }

    /**
     * <pre>
     * 唤醒任务
     * </pre>
     */
    public cn.com.unary.initcopy.grpc.entity.SyncProcess resume(cn.com.unary.initcopy.grpc.entity.ResumeTask request) {
      return blockingUnaryCall(
              getChannel(), METHOD_RESUME, getCallOptions(), request);
    }

    /**
     * <pre>
     * 查询任务
     * </pre>
     */
    public cn.com.unary.initcopy.grpc.entity.TaskState query(cn.com.unary.initcopy.grpc.entity.QueryTask request) {
      return blockingUnaryCall(
              getChannel(), METHOD_QUERY, getCallOptions(), request);
    }
  }

  private static final int METHODID_QUERY = 3;

  /**
   */
  public static final class ControlTaskFutureStub extends io.grpc.stub.AbstractStub<ControlTaskFutureStub> {
    private ControlTaskFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ControlTaskFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ControlTaskFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ControlTaskFutureStub(channel, callOptions);
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
    public com.google.common.util.concurrent.ListenableFuture<cn.com.unary.initcopy.grpc.entity.ExecResult> delete(
        cn.com.unary.initcopy.grpc.entity.DeleteTask request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_DELETE, getCallOptions()), request);
    }

    /**
     * <pre>
     * 唤醒任务
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<cn.com.unary.initcopy.grpc.entity.SyncProcess> resume(
            cn.com.unary.initcopy.grpc.entity.ResumeTask request) {
      return futureUnaryCall(
              getChannel().newCall(METHOD_RESUME, getCallOptions()), request);
    }

    /**
     * <pre>
     * 查询任务
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<cn.com.unary.initcopy.grpc.entity.TaskState> query(
            cn.com.unary.initcopy.grpc.entity.QueryTask request) {
      return futureUnaryCall(
              getChannel().newCall(METHOD_QUERY, getCallOptions()), request);
    }
  }

  private static class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ControlTaskImplBase serviceImpl;
    private final int methodId;

    public MethodHandlers(ControlTaskImplBase serviceImpl, int methodId) {
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
              (io.grpc.stub.StreamObserver<cn.com.unary.initcopy.grpc.entity.ExecResult>) responseObserver);
          break;
        case METHODID_RESUME:
          serviceImpl.resume((cn.com.unary.initcopy.grpc.entity.ResumeTask) request,
                  (io.grpc.stub.StreamObserver<cn.com.unary.initcopy.grpc.entity.SyncProcess>) responseObserver);
          break;
        case METHODID_QUERY:
          serviceImpl.query((cn.com.unary.initcopy.grpc.entity.QueryTask) request,
                  (io.grpc.stub.StreamObserver<cn.com.unary.initcopy.grpc.entity.TaskState>) responseObserver);
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

}
