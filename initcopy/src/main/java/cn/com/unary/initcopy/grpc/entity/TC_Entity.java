// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: task_control_entity.proto

package cn.com.unary.initcopy.grpc.entity;

public final class TC_Entity {
    private TC_Entity() {
    }

    public static void registerAllExtensions(
            com.google.protobuf.ExtensionRegistryLite registry) {
    }

    public static void registerAllExtensions(
            com.google.protobuf.ExtensionRegistry registry) {
        registerAllExtensions(
                (com.google.protobuf.ExtensionRegistryLite) registry);
    }

    static final com.google.protobuf.Descriptors.Descriptor
            internal_static_cn_com_unary_initcopy_grpc_entity_BaseFileInfo_descriptor;
    static final
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
            internal_static_cn_com_unary_initcopy_grpc_entity_BaseFileInfo_fieldAccessorTable;
    static final com.google.protobuf.Descriptors.Descriptor
            internal_static_cn_com_unary_initcopy_grpc_entity_ClientInitReq_descriptor;
    static final
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
            internal_static_cn_com_unary_initcopy_grpc_entity_ClientInitReq_fieldAccessorTable;
    static final com.google.protobuf.Descriptors.Descriptor
            internal_static_cn_com_unary_initcopy_grpc_entity_DiffFileChunk_descriptor;
    static final
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
            internal_static_cn_com_unary_initcopy_grpc_entity_DiffFileChunk_fieldAccessorTable;
    static final com.google.protobuf.Descriptors.Descriptor
            internal_static_cn_com_unary_initcopy_grpc_entity_DiffFileInfo_descriptor;
    static final
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
            internal_static_cn_com_unary_initcopy_grpc_entity_DiffFileInfo_fieldAccessorTable;
    static final com.google.protobuf.Descriptors.Descriptor
            internal_static_cn_com_unary_initcopy_grpc_entity_ServerInitResp_descriptor;
    static final
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
            internal_static_cn_com_unary_initcopy_grpc_entity_ServerInitResp_fieldAccessorTable;

    public static com.google.protobuf.Descriptors.FileDescriptor
    getDescriptor() {
        return descriptor;
    }

    private static com.google.protobuf.Descriptors.FileDescriptor
            descriptor;

    static {
        java.lang.String[] descriptorData = {
                "\n\031task_control_entity.proto\022!cn.com.unar" +
                        "y.initcopy.grpc.entity\032\030init_copy_consta" +
                        "nt.proto\032\026init_copy_entity.proto\"|\n\014Base" +
                        "FileInfo\022\020\n\010checkSum\030\001 \001(\t\022\022\n\nmodifyTime" +
                        "\030\002 \001(\022\022\020\n\010fileSize\030\003 \001(\022\022\020\n\010fullName\030\004 \001" +
                        "(\t\022\016\n\006fileId\030\005 \001(\t\022\022\n\nbackUpPath\030\006 \001(\t\"\317" +
                        "\001\n\rClientInitReq\022\016\n\006taskId\030\001 \001(\t\022\021\n\ttota" +
                        "lSize\030\002 \001(\t\022\022\n\nbackUpPath\030\003 \001(\t\022?\n\010syncT" +
                        "ype\030\004 \001(\0162-.cn.com.unary.initcopy.grpc.c" +
                        "onstant.SyncType\022F\n\rbaseFileInfos\030\005 \003(\0132",
                "/.cn.com.unary.initcopy.grpc.entity.Base" +
                        "FileInfo\"Q\n\rDiffFileChunk\022\027\n\017strongCheck" +
                        "Code\030\001 \001(\t\022\025\n\rweakCheckCode\030\002 \001(\t\022\020\n\010chu" +
                        "nkSeq\030\003 \001(\t\"h\n\014DiffFileInfo\022\016\n\006fileId\030\001 " +
                        "\001(\t\022H\n\016diffFileChunks\030\002 \003(\01320.cn.com.una" +
                        "ry.initcopy.grpc.entity.DiffFileChunk\"\233\001" +
                        "\n\016ServerInitResp\022A\n\nexecResult\030\001 \001(\0132-.c" +
                        "n.com.unary.initcopy.grpc.entity.ExecRes" +
                        "ult\022F\n\rdiffFileInfos\030\002 \003(\0132/.cn.com.unar" +
                        "y.initcopy.grpc.entity.DiffFileInfoB0\n!c",
                "n.com.unary.initcopy.grpc.entityB\tTC_Ent" +
                        "ityP\001b\006proto3"
        };
        com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
                new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
                    public com.google.protobuf.ExtensionRegistry assignDescriptors(
                            com.google.protobuf.Descriptors.FileDescriptor root) {
                        descriptor = root;
                        return null;
                    }
                };
        com.google.protobuf.Descriptors.FileDescriptor
                .internalBuildGeneratedFileFrom(descriptorData,
                        new com.google.protobuf.Descriptors.FileDescriptor[]{
                                cn.com.unary.initcopy.grpc.constant.IC_Constant.getDescriptor(),
                                cn.com.unary.initcopy.grpc.entity.IC_Entity.getDescriptor(),
                        }, assigner);
        internal_static_cn_com_unary_initcopy_grpc_entity_BaseFileInfo_descriptor =
                getDescriptor().getMessageTypes().get(0);
        internal_static_cn_com_unary_initcopy_grpc_entity_BaseFileInfo_fieldAccessorTable = new
                com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
                internal_static_cn_com_unary_initcopy_grpc_entity_BaseFileInfo_descriptor,
                new java.lang.String[]{"CheckSum", "ModifyTime", "FileSize", "FullName", "FileId", "BackUpPath",});
        internal_static_cn_com_unary_initcopy_grpc_entity_ClientInitReq_descriptor =
                getDescriptor().getMessageTypes().get(1);
        internal_static_cn_com_unary_initcopy_grpc_entity_ClientInitReq_fieldAccessorTable = new
                com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
                internal_static_cn_com_unary_initcopy_grpc_entity_ClientInitReq_descriptor,
                new java.lang.String[]{"TaskId", "TotalSize", "BackUpPath", "SyncType", "BaseFileInfos",});
        internal_static_cn_com_unary_initcopy_grpc_entity_DiffFileChunk_descriptor =
                getDescriptor().getMessageTypes().get(2);
        internal_static_cn_com_unary_initcopy_grpc_entity_DiffFileChunk_fieldAccessorTable = new
                com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
                internal_static_cn_com_unary_initcopy_grpc_entity_DiffFileChunk_descriptor,
                new java.lang.String[]{"StrongCheckCode", "WeakCheckCode", "ChunkSeq",});
        internal_static_cn_com_unary_initcopy_grpc_entity_DiffFileInfo_descriptor =
                getDescriptor().getMessageTypes().get(3);
        internal_static_cn_com_unary_initcopy_grpc_entity_DiffFileInfo_fieldAccessorTable = new
                com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
                internal_static_cn_com_unary_initcopy_grpc_entity_DiffFileInfo_descriptor,
                new java.lang.String[]{"FileId", "DiffFileChunks",});
        internal_static_cn_com_unary_initcopy_grpc_entity_ServerInitResp_descriptor =
                getDescriptor().getMessageTypes().get(4);
        internal_static_cn_com_unary_initcopy_grpc_entity_ServerInitResp_fieldAccessorTable = new
                com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
                internal_static_cn_com_unary_initcopy_grpc_entity_ServerInitResp_descriptor,
                new java.lang.String[]{"ExecResult", "DiffFileInfos",});
        cn.com.unary.initcopy.grpc.constant.IC_Constant.getDescriptor();
        cn.com.unary.initcopy.grpc.entity.IC_Entity.getDescriptor();
    }

    // @@protoc_insertion_point(outer_class_scope)
}
