// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: task_control_entity.proto

package cn.com.unary.initcopy.grpc.entity;

/**
 * Protobuf type {@code cn.com.unary.initcopy.grpc.entity.ResumeTask}
 */
public final class ResumeTask extends
        com.google.protobuf.GeneratedMessageV3 implements
        // @@protoc_insertion_point(message_implements:cn.com.unary.initcopy.grpc.entity.ResumeTask)
        ResumeTaskOrBuilder {
    public static final int TASKID_FIELD_NUMBER = 1;
    private static final long serialVersionUID = 0L;
    // @@protoc_insertion_point(class_scope:cn.com.unary.initcopy.grpc.entity.ResumeTask)
    private static final cn.com.unary.initcopy.grpc.entity.ResumeTask DEFAULT_INSTANCE;
    private static final com.google.protobuf.Parser<ResumeTask>
            PARSER = new com.google.protobuf.AbstractParser<ResumeTask>() {
        public ResumeTask parsePartialFrom(
                com.google.protobuf.CodedInputStream input,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws com.google.protobuf.InvalidProtocolBufferException {
            return new ResumeTask(input, extensionRegistry);
        }
    };

    static {
        DEFAULT_INSTANCE = new cn.com.unary.initcopy.grpc.entity.ResumeTask();
    }

    private volatile java.lang.Object taskId_;
    private byte memoizedIsInitialized = -1;

    // Use ResumeTask.newBuilder() to construct.
    private ResumeTask(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
        super(builder);
    }

    private ResumeTask() {
        taskId_ = "";
    }

    private ResumeTask(
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
                        java.lang.String s = input.readStringRequireUtf8();

                        taskId_ = s;
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
        return cn.com.unary.initcopy.grpc.entity.TC_Entity.internal_static_cn_com_unary_initcopy_grpc_entity_ResumeTask_descriptor;
    }

    public static cn.com.unary.initcopy.grpc.entity.ResumeTask parseFrom(
            com.google.protobuf.ByteString data)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }

    public static cn.com.unary.initcopy.grpc.entity.ResumeTask parseFrom(
            com.google.protobuf.ByteString data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static cn.com.unary.initcopy.grpc.entity.ResumeTask parseFrom(byte[] data)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }

    public static cn.com.unary.initcopy.grpc.entity.ResumeTask parseFrom(
            byte[] data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static cn.com.unary.initcopy.grpc.entity.ResumeTask parseFrom(java.io.InputStream input)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseWithIOException(PARSER, input);
    }

    public static cn.com.unary.initcopy.grpc.entity.ResumeTask parseFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public static cn.com.unary.initcopy.grpc.entity.ResumeTask parseDelimitedFrom(java.io.InputStream input)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseDelimitedWithIOException(PARSER, input);
    }

    public static cn.com.unary.initcopy.grpc.entity.ResumeTask parseDelimitedFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }

    public static cn.com.unary.initcopy.grpc.entity.ResumeTask parseFrom(
            com.google.protobuf.CodedInputStream input)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseWithIOException(PARSER, input);
    }

    public static cn.com.unary.initcopy.grpc.entity.ResumeTask parseFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public static Builder newBuilder() {
        return DEFAULT_INSTANCE.toBuilder();
    }

    public static Builder newBuilder(cn.com.unary.initcopy.grpc.entity.ResumeTask prototype) {
        return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }

    public static cn.com.unary.initcopy.grpc.entity.ResumeTask getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public static com.google.protobuf.Parser<ResumeTask> parser() {
        return PARSER;
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
        return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
    internalGetFieldAccessorTable() {
        return cn.com.unary.initcopy.grpc.entity.TC_Entity.internal_static_cn_com_unary_initcopy_grpc_entity_ResumeTask_fieldAccessorTable
                .ensureFieldAccessorsInitialized(
                        cn.com.unary.initcopy.grpc.entity.ResumeTask.class, cn.com.unary.initcopy.grpc.entity.ResumeTask.Builder.class);
    }

    /**
     * <code>optional string taskId = 1;</code>
     */
    public java.lang.String getTaskId() {
        java.lang.Object ref = taskId_;
        if (ref instanceof java.lang.String) {
            return (java.lang.String) ref;
        } else {
            com.google.protobuf.ByteString bs =
                    (com.google.protobuf.ByteString) ref;
            java.lang.String s = bs.toStringUtf8();
            taskId_ = s;
            return s;
        }
    }

    /**
     * <code>optional string taskId = 1;</code>
     */
    public com.google.protobuf.ByteString
    getTaskIdBytes() {
        java.lang.Object ref = taskId_;
        if (ref instanceof java.lang.String) {
            com.google.protobuf.ByteString b =
                    com.google.protobuf.ByteString.copyFromUtf8(
                            (java.lang.String) ref);
            taskId_ = b;
            return b;
        } else {
            return (com.google.protobuf.ByteString) ref;
        }
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
        if (!getTaskIdBytes().isEmpty()) {
            com.google.protobuf.GeneratedMessageV3.writeString(output, 1, taskId_);
        }
    }

    public int getSerializedSize() {
        int size = memoizedSize;
        if (size != -1) return size;

        size = 0;
        if (!getTaskIdBytes().isEmpty()) {
            size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, taskId_);
        }
        memoizedSize = size;
        return size;
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof cn.com.unary.initcopy.grpc.entity.ResumeTask)) {
            return super.equals(obj);
        }
        cn.com.unary.initcopy.grpc.entity.ResumeTask other = (cn.com.unary.initcopy.grpc.entity.ResumeTask) obj;

        boolean result = true;
        result = result && getTaskId()
                .equals(other.getTaskId());
        return result;
    }

    @java.lang.Override
    public int hashCode() {
        if (memoizedHashCode != 0) {
            return memoizedHashCode;
        }
        int hash = 41;
        hash = (19 * hash) + getDescriptorForType().hashCode();
        hash = (37 * hash) + TASKID_FIELD_NUMBER;
        hash = (53 * hash) + getTaskId().hashCode();
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
    public com.google.protobuf.Parser<ResumeTask> getParserForType() {
        return PARSER;
    }

    public cn.com.unary.initcopy.grpc.entity.ResumeTask getDefaultInstanceForType() {
        return DEFAULT_INSTANCE;
    }

    /**
     * Protobuf type {@code cn.com.unary.initcopy.grpc.entity.ResumeTask}
     */
    public static final class Builder extends
            com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
            // @@protoc_insertion_point(builder_implements:cn.com.unary.initcopy.grpc.entity.ResumeTask)
            cn.com.unary.initcopy.grpc.entity.ResumeTaskOrBuilder {
        private java.lang.Object taskId_ = "";

        // Construct using cn.com.unary.initcopy.grpc.entity.ResumeTask.newBuilder()
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
            return cn.com.unary.initcopy.grpc.entity.TC_Entity.internal_static_cn_com_unary_initcopy_grpc_entity_ResumeTask_descriptor;
        }

        protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
            return cn.com.unary.initcopy.grpc.entity.TC_Entity.internal_static_cn_com_unary_initcopy_grpc_entity_ResumeTask_fieldAccessorTable
                    .ensureFieldAccessorsInitialized(
                            cn.com.unary.initcopy.grpc.entity.ResumeTask.class, cn.com.unary.initcopy.grpc.entity.ResumeTask.Builder.class);
        }

        private void maybeForceBuilderInitialization() {
            if (com.google.protobuf.GeneratedMessageV3
                    .alwaysUseFieldBuilders) {
            }
        }

        public Builder clear() {
            super.clear();
            taskId_ = "";

            return this;
        }

        public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
            return cn.com.unary.initcopy.grpc.entity.TC_Entity.internal_static_cn_com_unary_initcopy_grpc_entity_ResumeTask_descriptor;
        }

        public cn.com.unary.initcopy.grpc.entity.ResumeTask getDefaultInstanceForType() {
            return cn.com.unary.initcopy.grpc.entity.ResumeTask.getDefaultInstance();
        }

        public cn.com.unary.initcopy.grpc.entity.ResumeTask build() {
            cn.com.unary.initcopy.grpc.entity.ResumeTask result = buildPartial();
            if (!result.isInitialized()) {
                throw newUninitializedMessageException(result);
            }
            return result;
        }

        public cn.com.unary.initcopy.grpc.entity.ResumeTask buildPartial() {
            cn.com.unary.initcopy.grpc.entity.ResumeTask result = new cn.com.unary.initcopy.grpc.entity.ResumeTask(this);
            result.taskId_ = taskId_;
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
            if (other instanceof cn.com.unary.initcopy.grpc.entity.ResumeTask) {
                return mergeFrom((cn.com.unary.initcopy.grpc.entity.ResumeTask) other);
            } else {
                super.mergeFrom(other);
                return this;
            }
        }

        public Builder mergeFrom(cn.com.unary.initcopy.grpc.entity.ResumeTask other) {
            if (other == cn.com.unary.initcopy.grpc.entity.ResumeTask.getDefaultInstance()) return this;
            if (!other.getTaskId().isEmpty()) {
                taskId_ = other.taskId_;
                onChanged();
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
            cn.com.unary.initcopy.grpc.entity.ResumeTask parsedMessage = null;
            try {
                parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
            } catch (com.google.protobuf.InvalidProtocolBufferException e) {
                parsedMessage = (cn.com.unary.initcopy.grpc.entity.ResumeTask) e.getUnfinishedMessage();
                throw e.unwrapIOException();
            } finally {
                if (parsedMessage != null) {
                    mergeFrom(parsedMessage);
                }
            }
            return this;
        }

        /**
         * <code>optional string taskId = 1;</code>
         */
        public java.lang.String getTaskId() {
            java.lang.Object ref = taskId_;
            if (!(ref instanceof java.lang.String)) {
                com.google.protobuf.ByteString bs =
                        (com.google.protobuf.ByteString) ref;
                java.lang.String s = bs.toStringUtf8();
                taskId_ = s;
                return s;
            } else {
                return (java.lang.String) ref;
            }
        }

        /**
         * <code>optional string taskId = 1;</code>
         */
        public Builder setTaskId(
                java.lang.String value) {
            if (value == null) {
                throw new NullPointerException();
            }

            taskId_ = value;
            onChanged();
            return this;
        }

        /**
         * <code>optional string taskId = 1;</code>
         */
        public com.google.protobuf.ByteString
        getTaskIdBytes() {
            java.lang.Object ref = taskId_;
            if (ref instanceof String) {
                com.google.protobuf.ByteString b =
                        com.google.protobuf.ByteString.copyFromUtf8(
                                (java.lang.String) ref);
                taskId_ = b;
                return b;
            } else {
                return (com.google.protobuf.ByteString) ref;
            }
        }

        /**
         * <code>optional string taskId = 1;</code>
         */
        public Builder setTaskIdBytes(
                com.google.protobuf.ByteString value) {
            if (value == null) {
                throw new NullPointerException();
            }
            checkByteStringIsUtf8(value);

            taskId_ = value;
            onChanged();
            return this;
        }

        /**
         * <code>optional string taskId = 1;</code>
         */
        public Builder clearTaskId() {

            taskId_ = getDefaultInstance().getTaskId();
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


        // @@protoc_insertion_point(builder_scope:cn.com.unary.initcopy.grpc.entity.ResumeTask)
    }

}

