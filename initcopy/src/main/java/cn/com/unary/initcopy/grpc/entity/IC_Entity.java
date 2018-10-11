// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: init_copy_entity.proto

package cn.com.unary.initcopy.grpc.entity;

public final class IC_Entity {
  private IC_Entity() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_cn_com_unary_initcopy_grpc_entity_SyncTarget_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_cn_com_unary_initcopy_grpc_entity_SyncTarget_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_cn_com_unary_initcopy_grpc_entity_SyncTask_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_cn_com_unary_initcopy_grpc_entity_SyncTask_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_cn_com_unary_initcopy_grpc_entity_ExecResult_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_cn_com_unary_initcopy_grpc_entity_ExecResult_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_cn_com_unary_initcopy_grpc_entity_ProgressInfo_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_cn_com_unary_initcopy_grpc_entity_ProgressInfo_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_cn_com_unary_initcopy_grpc_entity_QueryTask_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_cn_com_unary_initcopy_grpc_entity_QueryTask_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_cn_com_unary_initcopy_grpc_entity_TaskState_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_cn_com_unary_initcopy_grpc_entity_TaskState_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_cn_com_unary_initcopy_grpc_entity_ModifyTask_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_cn_com_unary_initcopy_grpc_entity_ModifyTask_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_cn_com_unary_initcopy_grpc_entity_DeleteTask_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_cn_com_unary_initcopy_grpc_entity_DeleteTask_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\026init_copy_entity.proto\022!cn.com.unary.i" +
      "nitcopy.grpc.entity\032\030init_copy_constant." +
      "proto\"@\n\nSyncTarget\022\n\n\002ip\030\001 \001(\t\022\024\n\014trans" +
              "ferPort\030\002 \001(\005\022\020\n\010grpcPort\030\003 \001(\005\"\246\003\n\010Sync" +
              "Task\022\016\n\006taskId\030\001 \001(\t\022A\n\ntargetInfo\030\002 \001(\013" +
      "2-.cn.com.unary.initcopy.grpc.entity.Syn" +
      "cTarget\022?\n\010syncType\030\003 \001(\0162-.cn.com.unary" +
      ".initcopy.grpc.constant.SyncType\022G\n\014comp" +
      "ressType\030\004 \001(\01621.cn.com.unary.initcopy.g" +
      "rpc.constant.CompressType\022E\n\013encryptType",
      "\030\005 \001(\01620.cn.com.unary.initcopy.grpc.cons" +
      "tant.EncryptType\022?\n\010packType\030\006 \001(\0162-.cn." +
      "com.unary.initcopy.grpc.constant.PackTyp" +
              "e\022\022\n\nspeedLimit\030\007 \001(\005\022\022\n\ntargetDirs\030\010 \003(" +
              "\t\022\r\n\005files\030\t \003(\t\"H\n\nExecResult\022\017\n\007health" +
              "y\030\001 \001(\010\022\014\n\004code\030\002 \001(\005\022\013\n\003msg\030\003 \001(\t\022\016\n\006ta" +
              "skId\030\004 \001(\t\"\244\001\n\014ProgressInfo\022\r\n\005stage\030\001 \001" +
              "(\005\022\020\n\010progress\030\002 \001(\005\022\024\n\014totalFileNum\030\003 \001" +
              "(\022\022\025\n\rtotalFileSize\030\004 \001(\022\022\025\n\rsyncedFileN" +
              "um\030\005 \001(\022\022\026\n\016syncedFileSize\030\006 \001(\022\022\027\n\017sync",
            "ingFileName\030\007 \001(\t\"\033\n\tQueryTask\022\016\n\006taskId" +
                    "\030\001 \001(\t\"\225\001\n\tTaskState\022A\n\nexecResult\030\002 \001(\013" +
                    "2-.cn.com.unary.initcopy.grpc.entity.Exe" +
                    "cResult\022E\n\014progressInfo\030\003 \001(\0132/.cn.com.u" +
                    "nary.initcopy.grpc.entity.ProgressInfo\"u" +
                    "\n\nModifyTask\022\016\n\006taskId\030\001 \001(\t\022C\n\nmodifyTy" +
                    "pe\030\002 \001(\0162/.cn.com.unary.initcopy.grpc.co" +
                    "nstant.ModifyType\022\022\n\nspeedLimit\030\003 \001(\005\"0\n" +
                    "\nDeleteTask\022\016\n\006taskId\030\001 \001(\t\022\022\n\ndeleteFil" +
                    "e\030\002 \001(\010B0\n!cn.com.unary.initcopy.grpc.en",
      "tityB\tIC_EntityP\001b\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          cn.com.unary.initcopy.grpc.constant.IC_Constant.getDescriptor(),
        }, assigner);
    internal_static_cn_com_unary_initcopy_grpc_entity_SyncTarget_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_cn_com_unary_initcopy_grpc_entity_SyncTarget_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_cn_com_unary_initcopy_grpc_entity_SyncTarget_descriptor,
        new java.lang.String[] { "Ip", "TransferPort", "GrpcPort", });
    internal_static_cn_com_unary_initcopy_grpc_entity_SyncTask_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_cn_com_unary_initcopy_grpc_entity_SyncTask_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_cn_com_unary_initcopy_grpc_entity_SyncTask_descriptor,
            new java.lang.String[]{"TaskId", "TargetInfo", "SyncType", "CompressType", "EncryptType", "PackType", "SpeedLimit", "TargetDirs", "Files",});
    internal_static_cn_com_unary_initcopy_grpc_entity_ExecResult_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_cn_com_unary_initcopy_grpc_entity_ExecResult_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_cn_com_unary_initcopy_grpc_entity_ExecResult_descriptor,
            new java.lang.String[]{"Healthy", "Code", "Msg", "TaskId",});
    internal_static_cn_com_unary_initcopy_grpc_entity_ProgressInfo_descriptor =
      getDescriptor().getMessageTypes().get(3);
    internal_static_cn_com_unary_initcopy_grpc_entity_ProgressInfo_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_cn_com_unary_initcopy_grpc_entity_ProgressInfo_descriptor,
        new java.lang.String[] { "Stage", "Progress", "TotalFileNum", "TotalFileSize", "SyncedFileNum", "SyncedFileSize", "SyncingFileName", });
    internal_static_cn_com_unary_initcopy_grpc_entity_QueryTask_descriptor =
      getDescriptor().getMessageTypes().get(4);
    internal_static_cn_com_unary_initcopy_grpc_entity_QueryTask_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_cn_com_unary_initcopy_grpc_entity_QueryTask_descriptor,
        new java.lang.String[] { "TaskId", });
    internal_static_cn_com_unary_initcopy_grpc_entity_TaskState_descriptor =
      getDescriptor().getMessageTypes().get(5);
    internal_static_cn_com_unary_initcopy_grpc_entity_TaskState_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_cn_com_unary_initcopy_grpc_entity_TaskState_descriptor,
            new java.lang.String[]{"ExecResult", "ProgressInfo",});
    internal_static_cn_com_unary_initcopy_grpc_entity_ModifyTask_descriptor =
      getDescriptor().getMessageTypes().get(6);
    internal_static_cn_com_unary_initcopy_grpc_entity_ModifyTask_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_cn_com_unary_initcopy_grpc_entity_ModifyTask_descriptor,
        new java.lang.String[] { "TaskId", "ModifyType", "SpeedLimit", });
    internal_static_cn_com_unary_initcopy_grpc_entity_DeleteTask_descriptor =
      getDescriptor().getMessageTypes().get(7);
    internal_static_cn_com_unary_initcopy_grpc_entity_DeleteTask_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_cn_com_unary_initcopy_grpc_entity_DeleteTask_descriptor,
        new java.lang.String[] { "TaskId", "DeleteFile", });
    cn.com.unary.initcopy.grpc.constant.IC_Constant.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
