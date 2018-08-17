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
public class TaskServiceGrpc {

  private TaskServiceGrpc() {}

  public static final String SERVICE_NAME = "cn.com.unary.initcopy.grpc.TaskService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<cn.com.unary.initcopy.grpc.entity.SyncTask,
      cn.com.unary.initcopy.grpc.entity.TaskState> METHOD_ADD =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "cn.com.unary.initcopy.grpc.TaskService", "Add"),
          io.grpc.protobuf.ProtoUtils.marshaller(cn.com.unary.initcopy.grpc.entity.SyncTask.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(cn.com.unary.initcopy.grpc.entity.TaskState.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<cn.com.unary.initcopy.grpc.entity.QueryTask,
      cn.com.unary.initcopy.grpc.entity.TaskState> METHOD_QUERY =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "cn.com.unary.initcopy.grpc.TaskService", "Query"),
          io.grpc.protobuf.ProtoUtils.marshaller(cn.com.unary.initcopy.grpc.entity.QueryTask.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(cn.com.unary.initcopy.grpc.entity.TaskState.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<cn.com.unary.initcopy.grpc.entity.DeleteTask,
      cn.com.unary.initcopy.grpc.entity.TaskState> METHOD_DELETE =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "cn.com.unary.initcopy.grpc.TaskService", "Delete"),
          io.grpc.protobuf.ProtoUtils.marshaller(cn.com.unary.initcopy.grpc.entity.DeleteTask.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(cn.com.unary.initcopy.grpc.entity.TaskState.getDefaultInstance()));
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<cn.com.unary.initcopy.grpc.entity.ModifyTask,
      cn.com.unary.initcopy.grpc.entity.TaskState> METHOD_MODIFY =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "cn.com.unary.initcopy.grpc.TaskService", "Modify"),
          io.grpc.protobuf.ProtoUtils.marshaller(cn.com.unary.initcopy.grpc.entity.ModifyTask.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(cn.com.unary.initcopy.grpc.entity.TaskState.getDefaultInstance()));

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static TaskServiceStub newStub(io.grpc.Channel channel) {
    return new TaskServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static TaskServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new TaskServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary and streaming output calls on the service
   */
  public static TaskServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new TaskServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class TaskServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void add(cn.com.unary.initcopy.grpc.entity.SyncTask request,
        io.grpc.stub.StreamObserver<cn.com.unary.initcopy.grpc.entity.TaskState> responseObserver) {
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
        io.grpc.stub.StreamObserver<cn.com.unary.initcopy.grpc.entity.TaskState> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_DELETE, responseObserver);
    }

    /**
     */
    public void modify(cn.com.unary.initcopy.grpc.entity.ModifyTask request,
        io.grpc.stub.StreamObserver<cn.com.unary.initcopy.grpc.entity.TaskState> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_MODIFY, responseObserver);
    }

    @java.lang.Override public io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_ADD,
            asyncUnaryCall(
              new MethodHandlers<
                cn.com.unary.initcopy.grpc.entity.SyncTask,
                cn.com.unary.initcopy.grpc.entity.TaskState>(
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
  public static final class TaskServiceStub extends io.grpc.stub.AbstractStub<TaskServiceStub> {
    private TaskServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TaskServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TaskServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TaskServiceStub(channel, callOptions);
    }

    /**
     */
    public void add(cn.com.unary.initcopy.grpc.entity.SyncTask request,
        io.grpc.stub.StreamObserver<cn.com.unary.initcopy.grpc.entity.TaskState> responseObserver) {
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
        io.grpc.stub.StreamObserver<cn.com.unary.initcopy.grpc.entity.TaskState> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_DELETE, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void modify(cn.com.unary.initcopy.grpc.entity.ModifyTask request,
        io.grpc.stub.StreamObserver<cn.com.unary.initcopy.grpc.entity.TaskState> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_MODIFY, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class TaskServiceBlockingStub extends io.grpc.stub.AbstractStub<TaskServiceBlockingStub> {
    private TaskServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TaskServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TaskServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TaskServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public cn.com.unary.initcopy.grpc.entity.TaskState add(cn.com.unary.initcopy.grpc.entity.SyncTask request) {
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
    public cn.com.unary.initcopy.grpc.entity.TaskState delete(cn.com.unary.initcopy.grpc.entity.DeleteTask request) {
      return blockingUnaryCall(
          getChannel(), METHOD_DELETE, getCallOptions(), request);
    }

    /**
     */
    public cn.com.unary.initcopy.grpc.entity.TaskState modify(cn.com.unary.initcopy.grpc.entity.ModifyTask request) {
      return blockingUnaryCall(
          getChannel(), METHOD_MODIFY, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class TaskServiceFutureStub extends io.grpc.stub.AbstractStub<TaskServiceFutureStub> {
    private TaskServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TaskServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TaskServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TaskServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<cn.com.unary.initcopy.grpc.entity.TaskState> add(
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
    public com.google.common.util.concurrent.ListenableFuture<cn.com.unary.initcopy.grpc.entity.TaskState> delete(
        cn.com.unary.initcopy.grpc.entity.DeleteTask request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_DELETE, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<cn.com.unary.initcopy.grpc.entity.TaskState> modify(
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
    private final TaskServiceImplBase serviceImpl;
    private final int methodId;

    public MethodHandlers(TaskServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_ADD:
          serviceImpl.add((cn.com.unary.initcopy.grpc.entity.SyncTask) request,
              (io.grpc.stub.StreamObserver<cn.com.unary.initcopy.grpc.entity.TaskState>) responseObserver);
          break;
        case METHODID_QUERY:
          serviceImpl.query((cn.com.unary.initcopy.grpc.entity.QueryTask) request,
              (io.grpc.stub.StreamObserver<cn.com.unary.initcopy.grpc.entity.TaskState>) responseObserver);
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
        METHOD_ADD,
        METHOD_QUERY,
        METHOD_DELETE,
        METHOD_MODIFY);
  }

}
