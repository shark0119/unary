// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: task_control_entity.proto

package cn.com.unary.initcopy.grpc.entity;

/**
 * Protobuf type {@code cn.com.unary.initcopy.grpc.entity.SyncProcess}
 */
public final class SyncProcess extends
        com.google.protobuf.GeneratedMessageV3 implements
        // @@protoc_insertion_point(message_implements:cn.com.unary.initcopy.grpc.entity.SyncProcess)
        SyncProcessOrBuilder {
    public static final int EXECRESULT_FIELD_NUMBER = 1;
    public static final int FILEID_FIELD_NUMBER = 2;
    public static final int FILEPOS_FIELD_NUMBER = 3;
    public static final int PACKINDEX_FIELD_NUMBER = 4;
    private static final long serialVersionUID = 0L;
    // @@protoc_insertion_point(class_scope:cn.com.unary.initcopy.grpc.entity.SyncProcess)
    private static final cn.com.unary.initcopy.grpc.entity.SyncProcess DEFAULT_INSTANCE;
    private static final com.google.protobuf.Parser<SyncProcess>
            PARSER = new com.google.protobuf.AbstractParser<SyncProcess>() {
        public SyncProcess parsePartialFrom(
                com.google.protobuf.CodedInputStream input,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws com.google.protobuf.InvalidProtocolBufferException {
            return new SyncProcess(input, extensionRegistry);
        }
    };

    static {
        DEFAULT_INSTANCE = new cn.com.unary.initcopy.grpc.entity.SyncProcess();
    }

    private cn.com.unary.initcopy.grpc.entity.ExecResult execResult_;
    private volatile java.lang.Object fileId_;
    private long filePos_;
    private int packIndex_;
    private byte memoizedIsInitialized = -1;

    // Use SyncProcess.newBuilder() to construct.
    private SyncProcess(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
        super(builder);
    }

    private SyncProcess() {
        fileId_ = "";
        filePos_ = 0L;
        packIndex_ = 0;
    }

    private SyncProcess(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
        this();
        int mutable_bitField0_ = 0;
        try {
            boolean done = false;
            while (!done) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        done = true;
                        break;
                    default: {
                        if (!input.skipField(tag)) {
                            done = true;
                        }
                        break;
                    }
                    case 10: {
                        cn.com.unary.initcopy.grpc.entity.ExecResult.Builder subBuilder = null;
                        if (execResult_ != null) {
                            subBuilder = execResult_.toBuilder();
                        }
                        execResult_ = input.readMessage(cn.com.unary.initcopy.grpc.entity.ExecResult.parser(), extensionRegistry);
                        if (subBuilder != null) {
                            subBuilder.mergeFrom(execResult_);
                            execResult_ = subBuilder.buildPartial();
                        }

                        break;
                    }
                    case 18: {
                        java.lang.String s = input.readStringRequireUtf8();

                        fileId_ = s;
                        break;
                    }
                    case 24: {

                        filePos_ = input.readSInt64();
                        break;
                    }
                    case 32: {

                        packIndex_ = input.readInt32();
                        break;
                    }
                }
            }
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            throw e.setUnfinishedMessage(this);
        } catch (java.io.IOException e) {
            throw new com.google.protobuf.InvalidProtocolBufferException(
                    e).setUnfinishedMessage(this);
        } finally {
            makeExtensionsImmutable();
        }
    }

    public static final com.google.protobuf.Descriptors.Descriptor
    getDescriptor() {
        return cn.com.unary.initcopy.grpc.entity.TC_Entity.internal_static_cn_com_unary_initcopy_grpc_entity_SyncProcess_descriptor;
    }

    public static cn.com.unary.initcopy.grpc.entity.SyncProcess parseFrom(
            com.google.protobuf.ByteString data)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }

    public static cn.com.unary.initcopy.grpc.entity.SyncProcess parseFrom(
            com.google.protobuf.ByteString data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static cn.com.unary.initcopy.grpc.entity.SyncProcess parseFrom(byte[] data)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }

    public static cn.com.unary.initcopy.grpc.entity.SyncProcess parseFrom(
            byte[] data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static cn.com.unary.initcopy.grpc.entity.SyncProcess parseFrom(java.io.InputStream input)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseWithIOException(PARSER, input);
    }

    public static cn.com.unary.initcopy.grpc.entity.SyncProcess parseFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public static cn.com.unary.initcopy.grpc.entity.SyncProcess parseDelimitedFrom(java.io.InputStream input)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseDelimitedWithIOException(PARSER, input);
    }

    public static cn.com.unary.initcopy.grpc.entity.SyncProcess parseDelimitedFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }

    public static cn.com.unary.initcopy.grpc.entity.SyncProcess parseFrom(
            com.google.protobuf.CodedInputStream input)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseWithIOException(PARSER, input);
    }

    public static cn.com.unary.initcopy.grpc.entity.SyncProcess parseFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public static Builder newBuilder() {
        return DEFAULT_INSTANCE.toBuilder();
    }

    public static Builder newBuilder(cn.com.unary.initcopy.grpc.entity.SyncProcess prototype) {
        return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }

    public static cn.com.unary.initcopy.grpc.entity.SyncProcess getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public static com.google.protobuf.Parser<SyncProcess> parser() {
        return PARSER;
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
        return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
    internalGetFieldAccessorTable() {
        return cn.com.unary.initcopy.grpc.entity.TC_Entity.internal_static_cn_com_unary_initcopy_grpc_entity_SyncProcess_fieldAccessorTable
                .ensureFieldAccessorsInitialized(
                        cn.com.unary.initcopy.grpc.entity.SyncProcess.class, cn.com.unary.initcopy.grpc.entity.SyncProcess.Builder.class);
    }

    /**
     * <code>optional .cn.com.unary.initcopy.grpc.entity.ExecResult execResult = 1;</code>
     */
    public boolean hasExecResult() {
        return execResult_ != null;
    }

    /**
     * <code>optional .cn.com.unary.initcopy.grpc.entity.ExecResult execResult = 1;</code>
     */
    public cn.com.unary.initcopy.grpc.entity.ExecResult getExecResult() {
        return execResult_ == null ? cn.com.unary.initcopy.grpc.entity.ExecResult.getDefaultInstance() : execResult_;
    }

    /**
     * <code>optional .cn.com.unary.initcopy.grpc.entity.ExecResult execResult = 1;</code>
     */
    public cn.com.unary.initcopy.grpc.entity.ExecResultOrBuilder getExecResultOrBuilder() {
        return getExecResult();
    }

    /**
     * <pre>
     * 上次已同步的文件名
     * </pre>
     *
     * <code>optional string fileId = 2;</code>
     */
    public java.lang.String getFileId() {
        java.lang.Object ref = fileId_;
        if (ref instanceof java.lang.String) {
            return (java.lang.String) ref;
        } else {
            com.google.protobuf.ByteString bs =
                    (com.google.protobuf.ByteString) ref;
            java.lang.String s = bs.toStringUtf8();
            fileId_ = s;
            return s;
        }
    }

    /**
     * <pre>
     * 上次已同步的文件名
     * </pre>
     *
     * <code>optional string fileId = 2;</code>
     */
    public com.google.protobuf.ByteString
    getFileIdBytes() {
        java.lang.Object ref = fileId_;
        if (ref instanceof java.lang.String) {
            com.google.protobuf.ByteString b =
                    com.google.protobuf.ByteString.copyFromUtf8(
                            (java.lang.String) ref);
            fileId_ = b;
            return b;
        } else {
            return (com.google.protobuf.ByteString) ref;
        }
    }

    /**
     * <pre>
     * 上次已同步的文件位置
     * </pre>
     *
     * <code>optional sint64 filePos = 3;</code>
     */
    public long getFilePos() {
        return filePos_;
    }

    /**
     * <code>optional int32 packIndex = 4;</code>
     */
    public int getPackIndex() {
        return packIndex_;
    }

    public final boolean isInitialized() {
        byte isInitialized = memoizedIsInitialized;
        if (isInitialized == 1) return true;
        if (isInitialized == 0) return false;

        memoizedIsInitialized = 1;
        return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
            throws java.io.IOException {
        if (execResult_ != null) {
            output.writeMessage(1, getExecResult());
        }
        if (!getFileIdBytes().isEmpty()) {
            com.google.protobuf.GeneratedMessageV3.writeString(output, 2, fileId_);
        }
        if (filePos_ != 0L) {
            output.writeSInt64(3, filePos_);
        }
        if (packIndex_ != 0) {
            output.writeInt32(4, packIndex_);
        }
    }

    public int getSerializedSize() {
        int size = memoizedSize;
        if (size != -1) return size;

        size = 0;
        if (execResult_ != null) {
            size += com.google.protobuf.CodedOutputStream
                    .computeMessageSize(1, getExecResult());
        }
        if (!getFileIdBytes().isEmpty()) {
            size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, fileId_);
        }
        if (filePos_ != 0L) {
            size += com.google.protobuf.CodedOutputStream
                    .computeSInt64Size(3, filePos_);
        }
        if (packIndex_ != 0) {
            size += com.google.protobuf.CodedOutputStream
                    .computeInt32Size(4, packIndex_);
        }
        memoizedSize = size;
        return size;
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof cn.com.unary.initcopy.grpc.entity.SyncProcess)) {
            return super.equals(obj);
        }
        cn.com.unary.initcopy.grpc.entity.SyncProcess other = (cn.com.unary.initcopy.grpc.entity.SyncProcess) obj;

        boolean result = true;
        result = result && (hasExecResult() == other.hasExecResult());
        if (hasExecResult()) {
            result = result && getExecResult()
                    .equals(other.getExecResult());
        }
        result = result && getFileId()
                .equals(other.getFileId());
        result = result && (getFilePos()
                == other.getFilePos());
        result = result && (getPackIndex()
                == other.getPackIndex());
        return result;
    }

    @java.lang.Override
    public int hashCode() {
        if (memoizedHashCode != 0) {
            return memoizedHashCode;
        }
        int hash = 41;
        hash = (19 * hash) + getDescriptorForType().hashCode();
        if (hasExecResult()) {
            hash = (37 * hash) + EXECRESULT_FIELD_NUMBER;
            hash = (53 * hash) + getExecResult().hashCode();
        }
        hash = (37 * hash) + FILEID_FIELD_NUMBER;
        hash = (53 * hash) + getFileId().hashCode();
        hash = (37 * hash) + FILEPOS_FIELD_NUMBER;
        hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
                getFilePos());
        hash = (37 * hash) + PACKINDEX_FIELD_NUMBER;
        hash = (53 * hash) + getPackIndex();
        hash = (29 * hash) + unknownFields.hashCode();
        memoizedHashCode = hash;
        return hash;
    }

    public Builder newBuilderForType() {
        return newBuilder();
    }

    public Builder toBuilder() {
        return this == DEFAULT_INSTANCE
                ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
            com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        Builder builder = new Builder(parent);
        return builder;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<SyncProcess> getParserForType() {
        return PARSER;
    }

    public cn.com.unary.initcopy.grpc.entity.SyncProcess getDefaultInstanceForType() {
        return DEFAULT_INSTANCE;
    }

    /**
     * Protobuf type {@code cn.com.unary.initcopy.grpc.entity.SyncProcess}
     */
    public static final class Builder extends
            com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
            // @@protoc_insertion_point(builder_implements:cn.com.unary.initcopy.grpc.entity.SyncProcess)
            cn.com.unary.initcopy.grpc.entity.SyncProcessOrBuilder {
        private cn.com.unary.initcopy.grpc.entity.ExecResult execResult_ = null;
        private com.google.protobuf.SingleFieldBuilderV3<
                cn.com.unary.initcopy.grpc.entity.ExecResult, cn.com.unary.initcopy.grpc.entity.ExecResult.Builder, cn.com.unary.initcopy.grpc.entity.ExecResultOrBuilder> execResultBuilder_;
        private java.lang.Object fileId_ = "";
        private long filePos_;
        private int packIndex_;

        // Construct using cn.com.unary.initcopy.grpc.entity.SyncProcess.newBuilder()
        private Builder() {
            maybeForceBuilderInitialization();
        }

        private Builder(
                com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
            super(parent);
            maybeForceBuilderInitialization();
        }

        public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
            return cn.com.unary.initcopy.grpc.entity.TC_Entity.internal_static_cn_com_unary_initcopy_grpc_entity_SyncProcess_descriptor;
        }

        protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
            return cn.com.unary.initcopy.grpc.entity.TC_Entity.internal_static_cn_com_unary_initcopy_grpc_entity_SyncProcess_fieldAccessorTable
                    .ensureFieldAccessorsInitialized(
                            cn.com.unary.initcopy.grpc.entity.SyncProcess.class, cn.com.unary.initcopy.grpc.entity.SyncProcess.Builder.class);
        }

        private void maybeForceBuilderInitialization() {
            if (com.google.protobuf.GeneratedMessageV3
                    .alwaysUseFieldBuilders) {
            }
        }

        public Builder clear() {
            super.clear();
            if (execResultBuilder_ == null) {
                execResult_ = null;
            } else {
                execResult_ = null;
                execResultBuilder_ = null;
            }
            fileId_ = "";

            filePos_ = 0L;

            packIndex_ = 0;

            return this;
        }

        public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
            return cn.com.unary.initcopy.grpc.entity.TC_Entity.internal_static_cn_com_unary_initcopy_grpc_entity_SyncProcess_descriptor;
        }

        public cn.com.unary.initcopy.grpc.entity.SyncProcess getDefaultInstanceForType() {
            return cn.com.unary.initcopy.grpc.entity.SyncProcess.getDefaultInstance();
        }

        public cn.com.unary.initcopy.grpc.entity.SyncProcess build() {
            cn.com.unary.initcopy.grpc.entity.SyncProcess result = buildPartial();
            if (!result.isInitialized()) {
                throw newUninitializedMessageException(result);
            }
            return result;
        }

        public cn.com.unary.initcopy.grpc.entity.SyncProcess buildPartial() {
            cn.com.unary.initcopy.grpc.entity.SyncProcess result = new cn.com.unary.initcopy.grpc.entity.SyncProcess(this);
            if (execResultBuilder_ == null) {
                result.execResult_ = execResult_;
            } else {
                result.execResult_ = execResultBuilder_.build();
            }
            result.fileId_ = fileId_;
            result.filePos_ = filePos_;
            result.packIndex_ = packIndex_;
            onBuilt();
            return result;
        }

        public Builder clone() {
            return (Builder) super.clone();
        }

        public Builder setField(
                com.google.protobuf.Descriptors.FieldDescriptor field,
                Object value) {
            return (Builder) super.setField(field, value);
        }

        public Builder clearField(
                com.google.protobuf.Descriptors.FieldDescriptor field) {
            return (Builder) super.clearField(field);
        }

        public Builder clearOneof(
                com.google.protobuf.Descriptors.OneofDescriptor oneof) {
            return (Builder) super.clearOneof(oneof);
        }

        public Builder setRepeatedField(
                com.google.protobuf.Descriptors.FieldDescriptor field,
                int index, Object value) {
            return (Builder) super.setRepeatedField(field, index, value);
        }

        public Builder addRepeatedField(
                com.google.protobuf.Descriptors.FieldDescriptor field,
                Object value) {
            return (Builder) super.addRepeatedField(field, value);
        }

        public Builder mergeFrom(com.google.protobuf.Message other) {
            if (other instanceof cn.com.unary.initcopy.grpc.entity.SyncProcess) {
                return mergeFrom((cn.com.unary.initcopy.grpc.entity.SyncProcess) other);
            } else {
                super.mergeFrom(other);
                return this;
            }
        }

        public Builder mergeFrom(cn.com.unary.initcopy.grpc.entity.SyncProcess other) {
            if (other == cn.com.unary.initcopy.grpc.entity.SyncProcess.getDefaultInstance()) return this;
            if (other.hasExecResult()) {
                mergeExecResult(other.getExecResult());
            }
            if (!other.getFileId().isEmpty()) {
                fileId_ = other.fileId_;
                onChanged();
            }
            if (other.getFilePos() != 0L) {
                setFilePos(other.getFilePos());
            }
            if (other.getPackIndex() != 0) {
                setPackIndex(other.getPackIndex());
            }
            onChanged();
            return this;
        }

        public final boolean isInitialized() {
            return true;
        }

        public Builder mergeFrom(
                com.google.protobuf.CodedInputStream input,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws java.io.IOException {
            cn.com.unary.initcopy.grpc.entity.SyncProcess parsedMessage = null;
            try {
                parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
            } catch (com.google.protobuf.InvalidProtocolBufferException e) {
                parsedMessage = (cn.com.unary.initcopy.grpc.entity.SyncProcess) e.getUnfinishedMessage();
                throw e.unwrapIOException();
            } finally {
                if (parsedMessage != null) {
                    mergeFrom(parsedMessage);
                }
            }
            return this;
        }

        /**
         * <code>optional .cn.com.unary.initcopy.grpc.entity.ExecResult execResult = 1;</code>
         */
        public boolean hasExecResult() {
            return execResultBuilder_ != null || execResult_ != null;
        }

        /**
         * <code>optional .cn.com.unary.initcopy.grpc.entity.ExecResult execResult = 1;</code>
         */
        public cn.com.unary.initcopy.grpc.entity.ExecResult getExecResult() {
            if (execResultBuilder_ == null) {
                return execResult_ == null ? cn.com.unary.initcopy.grpc.entity.ExecResult.getDefaultInstance() : execResult_;
            } else {
                return execResultBuilder_.getMessage();
            }
        }

        /**
         * <code>optional .cn.com.unary.initcopy.grpc.entity.ExecResult execResult = 1;</code>
         */
        public Builder setExecResult(
                cn.com.unary.initcopy.grpc.entity.ExecResult.Builder builderForValue) {
            if (execResultBuilder_ == null) {
                execResult_ = builderForValue.build();
                onChanged();
            } else {
                execResultBuilder_.setMessage(builderForValue.build());
            }

            return this;
        }

        /**
         * <code>optional .cn.com.unary.initcopy.grpc.entity.ExecResult execResult = 1;</code>
         */
        public Builder setExecResult(cn.com.unary.initcopy.grpc.entity.ExecResult value) {
            if (execResultBuilder_ == null) {
                if (value == null) {
                    throw new NullPointerException();
                }
                execResult_ = value;
                onChanged();
            } else {
                execResultBuilder_.setMessage(value);
            }

            return this;
        }

        /**
         * <code>optional .cn.com.unary.initcopy.grpc.entity.ExecResult execResult = 1;</code>
         */
        public Builder mergeExecResult(cn.com.unary.initcopy.grpc.entity.ExecResult value) {
            if (execResultBuilder_ == null) {
                if (execResult_ != null) {
                    execResult_ =
                            cn.com.unary.initcopy.grpc.entity.ExecResult.newBuilder(execResult_).mergeFrom(value).buildPartial();
                } else {
                    execResult_ = value;
                }
                onChanged();
            } else {
                execResultBuilder_.mergeFrom(value);
            }

            return this;
        }

        /**
         * <code>optional .cn.com.unary.initcopy.grpc.entity.ExecResult execResult = 1;</code>
         */
        public Builder clearExecResult() {
            if (execResultBuilder_ == null) {
                execResult_ = null;
                onChanged();
            } else {
                execResult_ = null;
                execResultBuilder_ = null;
            }

            return this;
        }

        /**
         * <code>optional .cn.com.unary.initcopy.grpc.entity.ExecResult execResult = 1;</code>
         */
        public cn.com.unary.initcopy.grpc.entity.ExecResult.Builder getExecResultBuilder() {

            onChanged();
            return getExecResultFieldBuilder().getBuilder();
        }

        /**
         * <code>optional .cn.com.unary.initcopy.grpc.entity.ExecResult execResult = 1;</code>
         */
        public cn.com.unary.initcopy.grpc.entity.ExecResultOrBuilder getExecResultOrBuilder() {
            if (execResultBuilder_ != null) {
                return execResultBuilder_.getMessageOrBuilder();
            } else {
                return execResult_ == null ?
                        cn.com.unary.initcopy.grpc.entity.ExecResult.getDefaultInstance() : execResult_;
            }
        }

        /**
         * <code>optional .cn.com.unary.initcopy.grpc.entity.ExecResult execResult = 1;</code>
         */
        private com.google.protobuf.SingleFieldBuilderV3<
                cn.com.unary.initcopy.grpc.entity.ExecResult, cn.com.unary.initcopy.grpc.entity.ExecResult.Builder, cn.com.unary.initcopy.grpc.entity.ExecResultOrBuilder>
        getExecResultFieldBuilder() {
            if (execResultBuilder_ == null) {
                execResultBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
                        cn.com.unary.initcopy.grpc.entity.ExecResult, cn.com.unary.initcopy.grpc.entity.ExecResult.Builder, cn.com.unary.initcopy.grpc.entity.ExecResultOrBuilder>(
                        getExecResult(),
                        getParentForChildren(),
                        isClean());
                execResult_ = null;
            }
            return execResultBuilder_;
        }

        /**
         * <pre>
         * 上次已同步的文件名
         * </pre>
         *
         * <code>optional string fileId = 2;</code>
         */
        public java.lang.String getFileId() {
            java.lang.Object ref = fileId_;
            if (!(ref instanceof java.lang.String)) {
                com.google.protobuf.ByteString bs =
                        (com.google.protobuf.ByteString) ref;
                java.lang.String s = bs.toStringUtf8();
                fileId_ = s;
                return s;
            } else {
                return (java.lang.String) ref;
            }
        }

        /**
         * <pre>
         * 上次已同步的文件名
         * </pre>
         *
         * <code>optional string fileId = 2;</code>
         */
        public Builder setFileId(
                java.lang.String value) {
            if (value == null) {
                throw new NullPointerException();
            }

            fileId_ = value;
            onChanged();
            return this;
        }

        /**
         * <pre>
         * 上次已同步的文件名
         * </pre>
         *
         * <code>optional string fileId = 2;</code>
         */
        public com.google.protobuf.ByteString
        getFileIdBytes() {
            java.lang.Object ref = fileId_;
            if (ref instanceof String) {
                com.google.protobuf.ByteString b =
                        com.google.protobuf.ByteString.copyFromUtf8(
                                (java.lang.String) ref);
                fileId_ = b;
                return b;
            } else {
                return (com.google.protobuf.ByteString) ref;
            }
        }

        /**
         * <pre>
         * 上次已同步的文件名
         * </pre>
         *
         * <code>optional string fileId = 2;</code>
         */
        public Builder setFileIdBytes(
                com.google.protobuf.ByteString value) {
            if (value == null) {
                throw new NullPointerException();
            }
            checkByteStringIsUtf8(value);

            fileId_ = value;
            onChanged();
            return this;
        }

        /**
         * <pre>
         * 上次已同步的文件名
         * </pre>
         *
         * <code>optional string fileId = 2;</code>
         */
        public Builder clearFileId() {

            fileId_ = getDefaultInstance().getFileId();
            onChanged();
            return this;
        }

        /**
         * <pre>
         * 上次已同步的文件位置
         * </pre>
         *
         * <code>optional sint64 filePos = 3;</code>
         */
        public long getFilePos() {
            return filePos_;
        }

        /**
         * <pre>
         * 上次已同步的文件位置
         * </pre>
         *
         * <code>optional sint64 filePos = 3;</code>
         */
        public Builder setFilePos(long value) {

            filePos_ = value;
            onChanged();
            return this;
        }

        /**
         * <pre>
         * 上次已同步的文件位置
         * </pre>
         *
         * <code>optional sint64 filePos = 3;</code>
         */
        public Builder clearFilePos() {

            filePos_ = 0L;
            onChanged();
            return this;
        }

        /**
         * <code>optional int32 packIndex = 4;</code>
         */
        public int getPackIndex() {
            return packIndex_;
        }

        /**
         * <code>optional int32 packIndex = 4;</code>
         */
        public Builder setPackIndex(int value) {

            packIndex_ = value;
            onChanged();
            return this;
        }

        /**
         * <code>optional int32 packIndex = 4;</code>
         */
        public Builder clearPackIndex() {

            packIndex_ = 0;
            onChanged();
            return this;
        }

        public final Builder setUnknownFields(
                final com.google.protobuf.UnknownFieldSet unknownFields) {
            return this;
        }

        public final Builder mergeUnknownFields(
                final com.google.protobuf.UnknownFieldSet unknownFields) {
            return this;
        }


        // @@protoc_insertion_point(builder_scope:cn.com.unary.initcopy.grpc.entity.SyncProcess)
    }

}

