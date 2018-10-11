// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: task_control_entity.proto

package cn.com.unary.initcopy.grpc.entity;

/**
 * Protobuf type {@code cn.com.unary.initcopy.grpc.entity.ServerInitResp}
 */
public  final class ServerInitResp extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:cn.com.unary.initcopy.grpc.entity.ServerInitResp)
    ServerInitRespOrBuilder {
  // Use ServerInitResp.newBuilder() to construct.
  private ServerInitResp(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private ServerInitResp() {
    taskId_ = "";
    ready_ = false;
    msg_ = "";
    diffFileInfos_ = java.util.Collections.emptyList();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
  }
  private ServerInitResp(
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
          case 16: {

            ready_ = input.readBool();
            break;
          }
          case 26: {
            java.lang.String s = input.readStringRequireUtf8();

            msg_ = s;
            break;
          }
          case 34: {
            if (!((mutable_bitField0_ & 0x00000008) == 0x00000008)) {
              diffFileInfos_ = new java.util.ArrayList<cn.com.unary.initcopy.grpc.entity.DiffFileInfo>();
              mutable_bitField0_ |= 0x00000008;
            }
            diffFileInfos_.add(
                input.readMessage(cn.com.unary.initcopy.grpc.entity.DiffFileInfo.parser(), extensionRegistry));
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
      if (((mutable_bitField0_ & 0x00000008) == 0x00000008)) {
        diffFileInfos_ = java.util.Collections.unmodifiableList(diffFileInfos_);
      }
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return cn.com.unary.initcopy.grpc.entity.TC_Entity.internal_static_cn_com_unary_initcopy_grpc_entity_ServerInitResp_descriptor;
  }

  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return cn.com.unary.initcopy.grpc.entity.TC_Entity.internal_static_cn_com_unary_initcopy_grpc_entity_ServerInitResp_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            cn.com.unary.initcopy.grpc.entity.ServerInitResp.class, cn.com.unary.initcopy.grpc.entity.ServerInitResp.Builder.class);
  }

  private int bitField0_;
  public static final int TASKID_FIELD_NUMBER = 1;
  private volatile java.lang.Object taskId_;

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

  public static final int READY_FIELD_NUMBER = 2;
  private boolean ready_;
  /**
   * <code>optional bool ready = 2;</code>
   */
  public boolean getReady() {
    return ready_;
  }

  public static final int MSG_FIELD_NUMBER = 3;
  private volatile java.lang.Object msg_;
  /**
   * <code>optional string msg = 3;</code>
   */
  public java.lang.String getMsg() {
    java.lang.Object ref = msg_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      msg_ = s;
      return s;
    }
  }
  /**
   * <code>optional string msg = 3;</code>
   */
  public com.google.protobuf.ByteString
      getMsgBytes() {
    java.lang.Object ref = msg_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      msg_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int DIFFFILEINFOS_FIELD_NUMBER = 4;
  private java.util.List<cn.com.unary.initcopy.grpc.entity.DiffFileInfo> diffFileInfos_;
  /**
   * <code>repeated .cn.com.unary.initcopy.grpc.entity.DiffFileInfo diffFileInfos = 4;</code>
   */
  public java.util.List<cn.com.unary.initcopy.grpc.entity.DiffFileInfo> getDiffFileInfosList() {
    return diffFileInfos_;
  }
  /**
   * <code>repeated .cn.com.unary.initcopy.grpc.entity.DiffFileInfo diffFileInfos = 4;</code>
   */
  public java.util.List<? extends cn.com.unary.initcopy.grpc.entity.DiffFileInfoOrBuilder> 
      getDiffFileInfosOrBuilderList() {
    return diffFileInfos_;
  }
  /**
   * <code>repeated .cn.com.unary.initcopy.grpc.entity.DiffFileInfo diffFileInfos = 4;</code>
   */
  public int getDiffFileInfosCount() {
    return diffFileInfos_.size();
  }
  /**
   * <code>repeated .cn.com.unary.initcopy.grpc.entity.DiffFileInfo diffFileInfos = 4;</code>
   */
  public cn.com.unary.initcopy.grpc.entity.DiffFileInfo getDiffFileInfos(int index) {
    return diffFileInfos_.get(index);
  }
  /**
   * <code>repeated .cn.com.unary.initcopy.grpc.entity.DiffFileInfo diffFileInfos = 4;</code>
   */
  public cn.com.unary.initcopy.grpc.entity.DiffFileInfoOrBuilder getDiffFileInfosOrBuilder(
      int index) {
    return diffFileInfos_.get(index);
  }

  private byte memoizedIsInitialized = -1;
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
    if (ready_ != false) {
      output.writeBool(2, ready_);
    }
    if (!getMsgBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 3, msg_);
    }
    for (int i = 0; i < diffFileInfos_.size(); i++) {
      output.writeMessage(4, diffFileInfos_.get(i));
    }
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!getTaskIdBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, taskId_);
    }
    if (ready_ != false) {
      size += com.google.protobuf.CodedOutputStream
        .computeBoolSize(2, ready_);
    }
    if (!getMsgBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(3, msg_);
    }
    for (int i = 0; i < diffFileInfos_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(4, diffFileInfos_.get(i));
    }
    memoizedSize = size;
    return size;
  }

  private static final long serialVersionUID = 0L;
  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof cn.com.unary.initcopy.grpc.entity.ServerInitResp)) {
      return super.equals(obj);
    }
    cn.com.unary.initcopy.grpc.entity.ServerInitResp other = (cn.com.unary.initcopy.grpc.entity.ServerInitResp) obj;

    boolean result = true;
    result = result && getTaskId()
            .equals(other.getTaskId());
    result = result && (getReady()
        == other.getReady());
    result = result && getMsg()
        .equals(other.getMsg());
    result = result && getDiffFileInfosList()
        .equals(other.getDiffFileInfosList());
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
    hash = (37 * hash) + READY_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
        getReady());
    hash = (37 * hash) + MSG_FIELD_NUMBER;
    hash = (53 * hash) + getMsg().hashCode();
    if (getDiffFileInfosCount() > 0) {
      hash = (37 * hash) + DIFFFILEINFOS_FIELD_NUMBER;
      hash = (53 * hash) + getDiffFileInfosList().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static cn.com.unary.initcopy.grpc.entity.ServerInitResp parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static cn.com.unary.initcopy.grpc.entity.ServerInitResp parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static cn.com.unary.initcopy.grpc.entity.ServerInitResp parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static cn.com.unary.initcopy.grpc.entity.ServerInitResp parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static cn.com.unary.initcopy.grpc.entity.ServerInitResp parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static cn.com.unary.initcopy.grpc.entity.ServerInitResp parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static cn.com.unary.initcopy.grpc.entity.ServerInitResp parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static cn.com.unary.initcopy.grpc.entity.ServerInitResp parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static cn.com.unary.initcopy.grpc.entity.ServerInitResp parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static cn.com.unary.initcopy.grpc.entity.ServerInitResp parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(cn.com.unary.initcopy.grpc.entity.ServerInitResp prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
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
  /**
   * Protobuf type {@code cn.com.unary.initcopy.grpc.entity.ServerInitResp}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:cn.com.unary.initcopy.grpc.entity.ServerInitResp)
      cn.com.unary.initcopy.grpc.entity.ServerInitRespOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return cn.com.unary.initcopy.grpc.entity.TC_Entity.internal_static_cn_com_unary_initcopy_grpc_entity_ServerInitResp_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return cn.com.unary.initcopy.grpc.entity.TC_Entity.internal_static_cn_com_unary_initcopy_grpc_entity_ServerInitResp_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              cn.com.unary.initcopy.grpc.entity.ServerInitResp.class, cn.com.unary.initcopy.grpc.entity.ServerInitResp.Builder.class);
    }

    // Construct using cn.com.unary.initcopy.grpc.entity.ServerInitResp.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
        getDiffFileInfosFieldBuilder();
      }
    }
    public Builder clear() {
      super.clear();
      taskId_ = "";

      ready_ = false;

      msg_ = "";

      if (diffFileInfosBuilder_ == null) {
        diffFileInfos_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000008);
      } else {
        diffFileInfosBuilder_.clear();
      }
      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return cn.com.unary.initcopy.grpc.entity.TC_Entity.internal_static_cn_com_unary_initcopy_grpc_entity_ServerInitResp_descriptor;
    }

    public cn.com.unary.initcopy.grpc.entity.ServerInitResp getDefaultInstanceForType() {
      return cn.com.unary.initcopy.grpc.entity.ServerInitResp.getDefaultInstance();
    }

    public cn.com.unary.initcopy.grpc.entity.ServerInitResp build() {
      cn.com.unary.initcopy.grpc.entity.ServerInitResp result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public cn.com.unary.initcopy.grpc.entity.ServerInitResp buildPartial() {
      cn.com.unary.initcopy.grpc.entity.ServerInitResp result = new cn.com.unary.initcopy.grpc.entity.ServerInitResp(this);
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      result.taskId_ = taskId_;
      result.ready_ = ready_;
      result.msg_ = msg_;
      if (diffFileInfosBuilder_ == null) {
        if (((bitField0_ & 0x00000008) == 0x00000008)) {
          diffFileInfos_ = java.util.Collections.unmodifiableList(diffFileInfos_);
          bitField0_ = (bitField0_ & ~0x00000008);
        }
        result.diffFileInfos_ = diffFileInfos_;
      } else {
        result.diffFileInfos_ = diffFileInfosBuilder_.build();
      }
      result.bitField0_ = to_bitField0_;
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
      if (other instanceof cn.com.unary.initcopy.grpc.entity.ServerInitResp) {
        return mergeFrom((cn.com.unary.initcopy.grpc.entity.ServerInitResp)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(cn.com.unary.initcopy.grpc.entity.ServerInitResp other) {
      if (other == cn.com.unary.initcopy.grpc.entity.ServerInitResp.getDefaultInstance()) return this;
      if (!other.getTaskId().isEmpty()) {
        taskId_ = other.taskId_;
        onChanged();
      }
      if (other.getReady() != false) {
        setReady(other.getReady());
      }
      if (!other.getMsg().isEmpty()) {
        msg_ = other.msg_;
        onChanged();
      }
      if (diffFileInfosBuilder_ == null) {
        if (!other.diffFileInfos_.isEmpty()) {
          if (diffFileInfos_.isEmpty()) {
            diffFileInfos_ = other.diffFileInfos_;
            bitField0_ = (bitField0_ & ~0x00000008);
          } else {
            ensureDiffFileInfosIsMutable();
            diffFileInfos_.addAll(other.diffFileInfos_);
          }
          onChanged();
        }
      } else {
        if (!other.diffFileInfos_.isEmpty()) {
          if (diffFileInfosBuilder_.isEmpty()) {
            diffFileInfosBuilder_.dispose();
            diffFileInfosBuilder_ = null;
            diffFileInfos_ = other.diffFileInfos_;
            bitField0_ = (bitField0_ & ~0x00000008);
            diffFileInfosBuilder_ = 
              com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                 getDiffFileInfosFieldBuilder() : null;
          } else {
            diffFileInfosBuilder_.addAllMessages(other.diffFileInfos_);
          }
        }
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
      cn.com.unary.initcopy.grpc.entity.ServerInitResp parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (cn.com.unary.initcopy.grpc.entity.ServerInitResp) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private java.lang.Object taskId_ = "";

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
    public Builder clearTaskId() {

      taskId_ = getDefaultInstance().getTaskId();
      onChanged();
      return this;
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

    private boolean ready_ ;
    /**
     * <code>optional bool ready = 2;</code>
     */
    public boolean getReady() {
      return ready_;
    }
    /**
     * <code>optional bool ready = 2;</code>
     */
    public Builder setReady(boolean value) {
      
      ready_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>optional bool ready = 2;</code>
     */
    public Builder clearReady() {
      
      ready_ = false;
      onChanged();
      return this;
    }

    private java.lang.Object msg_ = "";
    /**
     * <code>optional string msg = 3;</code>
     */
    public java.lang.String getMsg() {
      java.lang.Object ref = msg_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        msg_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>optional string msg = 3;</code>
     */
    public com.google.protobuf.ByteString
        getMsgBytes() {
      java.lang.Object ref = msg_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        msg_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>optional string msg = 3;</code>
     */
    public Builder setMsg(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      msg_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>optional string msg = 3;</code>
     */
    public Builder clearMsg() {
      
      msg_ = getDefaultInstance().getMsg();
      onChanged();
      return this;
    }
    /**
     * <code>optional string msg = 3;</code>
     */
    public Builder setMsgBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      msg_ = value;
      onChanged();
      return this;
    }

    private java.util.List<cn.com.unary.initcopy.grpc.entity.DiffFileInfo> diffFileInfos_ =
      java.util.Collections.emptyList();
    private void ensureDiffFileInfosIsMutable() {
      if (!((bitField0_ & 0x00000008) == 0x00000008)) {
        diffFileInfos_ = new java.util.ArrayList<cn.com.unary.initcopy.grpc.entity.DiffFileInfo>(diffFileInfos_);
        bitField0_ |= 0x00000008;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
        cn.com.unary.initcopy.grpc.entity.DiffFileInfo, cn.com.unary.initcopy.grpc.entity.DiffFileInfo.Builder, cn.com.unary.initcopy.grpc.entity.DiffFileInfoOrBuilder> diffFileInfosBuilder_;

    /**
     * <code>repeated .cn.com.unary.initcopy.grpc.entity.DiffFileInfo diffFileInfos = 4;</code>
     */
    public java.util.List<cn.com.unary.initcopy.grpc.entity.DiffFileInfo> getDiffFileInfosList() {
      if (diffFileInfosBuilder_ == null) {
        return java.util.Collections.unmodifiableList(diffFileInfos_);
      } else {
        return diffFileInfosBuilder_.getMessageList();
      }
    }
    /**
     * <code>repeated .cn.com.unary.initcopy.grpc.entity.DiffFileInfo diffFileInfos = 4;</code>
     */
    public int getDiffFileInfosCount() {
      if (diffFileInfosBuilder_ == null) {
        return diffFileInfos_.size();
      } else {
        return diffFileInfosBuilder_.getCount();
      }
    }
    /**
     * <code>repeated .cn.com.unary.initcopy.grpc.entity.DiffFileInfo diffFileInfos = 4;</code>
     */
    public cn.com.unary.initcopy.grpc.entity.DiffFileInfo getDiffFileInfos(int index) {
      if (diffFileInfosBuilder_ == null) {
        return diffFileInfos_.get(index);
      } else {
        return diffFileInfosBuilder_.getMessage(index);
      }
    }
    /**
     * <code>repeated .cn.com.unary.initcopy.grpc.entity.DiffFileInfo diffFileInfos = 4;</code>
     */
    public Builder setDiffFileInfos(
        int index, cn.com.unary.initcopy.grpc.entity.DiffFileInfo value) {
      if (diffFileInfosBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureDiffFileInfosIsMutable();
        diffFileInfos_.set(index, value);
        onChanged();
      } else {
        diffFileInfosBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .cn.com.unary.initcopy.grpc.entity.DiffFileInfo diffFileInfos = 4;</code>
     */
    public Builder setDiffFileInfos(
        int index, cn.com.unary.initcopy.grpc.entity.DiffFileInfo.Builder builderForValue) {
      if (diffFileInfosBuilder_ == null) {
        ensureDiffFileInfosIsMutable();
        diffFileInfos_.set(index, builderForValue.build());
        onChanged();
      } else {
        diffFileInfosBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .cn.com.unary.initcopy.grpc.entity.DiffFileInfo diffFileInfos = 4;</code>
     */
    public Builder addDiffFileInfos(cn.com.unary.initcopy.grpc.entity.DiffFileInfo value) {
      if (diffFileInfosBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureDiffFileInfosIsMutable();
        diffFileInfos_.add(value);
        onChanged();
      } else {
        diffFileInfosBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <code>repeated .cn.com.unary.initcopy.grpc.entity.DiffFileInfo diffFileInfos = 4;</code>
     */
    public Builder addDiffFileInfos(
        int index, cn.com.unary.initcopy.grpc.entity.DiffFileInfo value) {
      if (diffFileInfosBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureDiffFileInfosIsMutable();
        diffFileInfos_.add(index, value);
        onChanged();
      } else {
        diffFileInfosBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .cn.com.unary.initcopy.grpc.entity.DiffFileInfo diffFileInfos = 4;</code>
     */
    public Builder addDiffFileInfos(
        cn.com.unary.initcopy.grpc.entity.DiffFileInfo.Builder builderForValue) {
      if (diffFileInfosBuilder_ == null) {
        ensureDiffFileInfosIsMutable();
        diffFileInfos_.add(builderForValue.build());
        onChanged();
      } else {
        diffFileInfosBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .cn.com.unary.initcopy.grpc.entity.DiffFileInfo diffFileInfos = 4;</code>
     */
    public Builder addDiffFileInfos(
        int index, cn.com.unary.initcopy.grpc.entity.DiffFileInfo.Builder builderForValue) {
      if (diffFileInfosBuilder_ == null) {
        ensureDiffFileInfosIsMutable();
        diffFileInfos_.add(index, builderForValue.build());
        onChanged();
      } else {
        diffFileInfosBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .cn.com.unary.initcopy.grpc.entity.DiffFileInfo diffFileInfos = 4;</code>
     */
    public Builder addAllDiffFileInfos(
        java.lang.Iterable<? extends cn.com.unary.initcopy.grpc.entity.DiffFileInfo> values) {
      if (diffFileInfosBuilder_ == null) {
        ensureDiffFileInfosIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, diffFileInfos_);
        onChanged();
      } else {
        diffFileInfosBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <code>repeated .cn.com.unary.initcopy.grpc.entity.DiffFileInfo diffFileInfos = 4;</code>
     */
    public Builder clearDiffFileInfos() {
      if (diffFileInfosBuilder_ == null) {
        diffFileInfos_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000008);
        onChanged();
      } else {
        diffFileInfosBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>repeated .cn.com.unary.initcopy.grpc.entity.DiffFileInfo diffFileInfos = 4;</code>
     */
    public Builder removeDiffFileInfos(int index) {
      if (diffFileInfosBuilder_ == null) {
        ensureDiffFileInfosIsMutable();
        diffFileInfos_.remove(index);
        onChanged();
      } else {
        diffFileInfosBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <code>repeated .cn.com.unary.initcopy.grpc.entity.DiffFileInfo diffFileInfos = 4;</code>
     */
    public cn.com.unary.initcopy.grpc.entity.DiffFileInfo.Builder getDiffFileInfosBuilder(
        int index) {
      return getDiffFileInfosFieldBuilder().getBuilder(index);
    }
    /**
     * <code>repeated .cn.com.unary.initcopy.grpc.entity.DiffFileInfo diffFileInfos = 4;</code>
     */
    public cn.com.unary.initcopy.grpc.entity.DiffFileInfoOrBuilder getDiffFileInfosOrBuilder(
        int index) {
      if (diffFileInfosBuilder_ == null) {
        return diffFileInfos_.get(index);  } else {
        return diffFileInfosBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <code>repeated .cn.com.unary.initcopy.grpc.entity.DiffFileInfo diffFileInfos = 4;</code>
     */
    public java.util.List<? extends cn.com.unary.initcopy.grpc.entity.DiffFileInfoOrBuilder> 
         getDiffFileInfosOrBuilderList() {
      if (diffFileInfosBuilder_ != null) {
        return diffFileInfosBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(diffFileInfos_);
      }
    }
    /**
     * <code>repeated .cn.com.unary.initcopy.grpc.entity.DiffFileInfo diffFileInfos = 4;</code>
     */
    public cn.com.unary.initcopy.grpc.entity.DiffFileInfo.Builder addDiffFileInfosBuilder() {
      return getDiffFileInfosFieldBuilder().addBuilder(
          cn.com.unary.initcopy.grpc.entity.DiffFileInfo.getDefaultInstance());
    }
    /**
     * <code>repeated .cn.com.unary.initcopy.grpc.entity.DiffFileInfo diffFileInfos = 4;</code>
     */
    public cn.com.unary.initcopy.grpc.entity.DiffFileInfo.Builder addDiffFileInfosBuilder(
        int index) {
      return getDiffFileInfosFieldBuilder().addBuilder(
          index, cn.com.unary.initcopy.grpc.entity.DiffFileInfo.getDefaultInstance());
    }
    /**
     * <code>repeated .cn.com.unary.initcopy.grpc.entity.DiffFileInfo diffFileInfos = 4;</code>
     */
    public java.util.List<cn.com.unary.initcopy.grpc.entity.DiffFileInfo.Builder> 
         getDiffFileInfosBuilderList() {
      return getDiffFileInfosFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilderV3<
        cn.com.unary.initcopy.grpc.entity.DiffFileInfo, cn.com.unary.initcopy.grpc.entity.DiffFileInfo.Builder, cn.com.unary.initcopy.grpc.entity.DiffFileInfoOrBuilder> 
        getDiffFileInfosFieldBuilder() {
      if (diffFileInfosBuilder_ == null) {
        diffFileInfosBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
            cn.com.unary.initcopy.grpc.entity.DiffFileInfo, cn.com.unary.initcopy.grpc.entity.DiffFileInfo.Builder, cn.com.unary.initcopy.grpc.entity.DiffFileInfoOrBuilder>(
                diffFileInfos_,
                ((bitField0_ & 0x00000008) == 0x00000008),
                getParentForChildren(),
                isClean());
        diffFileInfos_ = null;
      }
      return diffFileInfosBuilder_;
    }
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return this;
    }

    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return this;
    }


    // @@protoc_insertion_point(builder_scope:cn.com.unary.initcopy.grpc.entity.ServerInitResp)
  }

  // @@protoc_insertion_point(class_scope:cn.com.unary.initcopy.grpc.entity.ServerInitResp)
  private static final cn.com.unary.initcopy.grpc.entity.ServerInitResp DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new cn.com.unary.initcopy.grpc.entity.ServerInitResp();
  }

  public static cn.com.unary.initcopy.grpc.entity.ServerInitResp getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<ServerInitResp>
      PARSER = new com.google.protobuf.AbstractParser<ServerInitResp>() {
    public ServerInitResp parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return new ServerInitResp(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<ServerInitResp> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<ServerInitResp> getParserForType() {
    return PARSER;
  }

  public cn.com.unary.initcopy.grpc.entity.ServerInitResp getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

