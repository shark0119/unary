// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: task_control_entity.proto

package cn.com.unary.initcopy.grpc.entity;

/**
 * Protobuf type {@code cn.com.unary.initcopy.grpc.entity.DiffFileChunk}
 */
public  final class DiffFileChunk extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:cn.com.unary.initcopy.grpc.entity.DiffFileChunk)
    DiffFileChunkOrBuilder {
  // Use DiffFileChunk.newBuilder() to construct.
  private DiffFileChunk(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private DiffFileChunk() {
    strongCheckCode_ = "";
    weakCheckCode_ = "";
    chunkSeq_ = "";
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
  }
  private DiffFileChunk(
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

            strongCheckCode_ = s;
            break;
          }
          case 18: {
            java.lang.String s = input.readStringRequireUtf8();

            weakCheckCode_ = s;
            break;
          }
          case 26: {
            java.lang.String s = input.readStringRequireUtf8();

            chunkSeq_ = s;
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
      return cn.com.unary.initcopy.grpc.entity.TC_Entity.internal_static_cn_com_unary_initcopy_grpc_entity_DiffFileChunk_descriptor;
  }

  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
      return cn.com.unary.initcopy.grpc.entity.TC_Entity.internal_static_cn_com_unary_initcopy_grpc_entity_DiffFileChunk_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            cn.com.unary.initcopy.grpc.entity.DiffFileChunk.class, cn.com.unary.initcopy.grpc.entity.DiffFileChunk.Builder.class);
  }

  public static final int STRONGCHECKCODE_FIELD_NUMBER = 1;
  private volatile java.lang.Object strongCheckCode_;
  /**
   * <pre>
   * 强校验码Md5
   * </pre>
   *
   * <code>optional string strongCheckCode = 1;</code>
   */
  public java.lang.String getStrongCheckCode() {
    java.lang.Object ref = strongCheckCode_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      strongCheckCode_ = s;
      return s;
    }
  }
  /**
   * <pre>
   * 强校验码Md5
   * </pre>
   *
   * <code>optional string strongCheckCode = 1;</code>
   */
  public com.google.protobuf.ByteString
      getStrongCheckCodeBytes() {
    java.lang.Object ref = strongCheckCode_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      strongCheckCode_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int WEAKCHECKCODE_FIELD_NUMBER = 2;
  private volatile java.lang.Object weakCheckCode_;
  /**
   * <pre>
   * 弱校验码 CRC
   * </pre>
   *
   * <code>optional string weakCheckCode = 2;</code>
   */
  public java.lang.String getWeakCheckCode() {
    java.lang.Object ref = weakCheckCode_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      weakCheckCode_ = s;
      return s;
    }
  }
  /**
   * <pre>
   * 弱校验码 CRC
   * </pre>
   *
   * <code>optional string weakCheckCode = 2;</code>
   */
  public com.google.protobuf.ByteString
      getWeakCheckCodeBytes() {
    java.lang.Object ref = weakCheckCode_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      weakCheckCode_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int CHUNKSEQ_FIELD_NUMBER = 3;
  private volatile java.lang.Object chunkSeq_;
  /**
   * <pre>
   * 块序号
   * </pre>
   *
   * <code>optional string chunkSeq = 3;</code>
   */
  public java.lang.String getChunkSeq() {
    java.lang.Object ref = chunkSeq_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      chunkSeq_ = s;
      return s;
    }
  }
  /**
   * <pre>
   * 块序号
   * </pre>
   *
   * <code>optional string chunkSeq = 3;</code>
   */
  public com.google.protobuf.ByteString
      getChunkSeqBytes() {
    java.lang.Object ref = chunkSeq_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      chunkSeq_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
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
    if (!getStrongCheckCodeBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, strongCheckCode_);
    }
    if (!getWeakCheckCodeBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, weakCheckCode_);
    }
    if (!getChunkSeqBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 3, chunkSeq_);
    }
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!getStrongCheckCodeBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, strongCheckCode_);
    }
    if (!getWeakCheckCodeBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, weakCheckCode_);
    }
    if (!getChunkSeqBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(3, chunkSeq_);
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
    if (!(obj instanceof cn.com.unary.initcopy.grpc.entity.DiffFileChunk)) {
      return super.equals(obj);
    }
    cn.com.unary.initcopy.grpc.entity.DiffFileChunk other = (cn.com.unary.initcopy.grpc.entity.DiffFileChunk) obj;

    boolean result = true;
    result = result && getStrongCheckCode()
        .equals(other.getStrongCheckCode());
    result = result && getWeakCheckCode()
        .equals(other.getWeakCheckCode());
    result = result && getChunkSeq()
        .equals(other.getChunkSeq());
    return result;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptorForType().hashCode();
    hash = (37 * hash) + STRONGCHECKCODE_FIELD_NUMBER;
    hash = (53 * hash) + getStrongCheckCode().hashCode();
    hash = (37 * hash) + WEAKCHECKCODE_FIELD_NUMBER;
    hash = (53 * hash) + getWeakCheckCode().hashCode();
    hash = (37 * hash) + CHUNKSEQ_FIELD_NUMBER;
    hash = (53 * hash) + getChunkSeq().hashCode();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static cn.com.unary.initcopy.grpc.entity.DiffFileChunk parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static cn.com.unary.initcopy.grpc.entity.DiffFileChunk parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static cn.com.unary.initcopy.grpc.entity.DiffFileChunk parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static cn.com.unary.initcopy.grpc.entity.DiffFileChunk parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static cn.com.unary.initcopy.grpc.entity.DiffFileChunk parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static cn.com.unary.initcopy.grpc.entity.DiffFileChunk parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static cn.com.unary.initcopy.grpc.entity.DiffFileChunk parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static cn.com.unary.initcopy.grpc.entity.DiffFileChunk parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static cn.com.unary.initcopy.grpc.entity.DiffFileChunk parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static cn.com.unary.initcopy.grpc.entity.DiffFileChunk parseFrom(
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
  public static Builder newBuilder(cn.com.unary.initcopy.grpc.entity.DiffFileChunk prototype) {
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
   * Protobuf type {@code cn.com.unary.initcopy.grpc.entity.DiffFileChunk}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:cn.com.unary.initcopy.grpc.entity.DiffFileChunk)
      cn.com.unary.initcopy.grpc.entity.DiffFileChunkOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
        return cn.com.unary.initcopy.grpc.entity.TC_Entity.internal_static_cn_com_unary_initcopy_grpc_entity_DiffFileChunk_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
        return cn.com.unary.initcopy.grpc.entity.TC_Entity.internal_static_cn_com_unary_initcopy_grpc_entity_DiffFileChunk_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              cn.com.unary.initcopy.grpc.entity.DiffFileChunk.class, cn.com.unary.initcopy.grpc.entity.DiffFileChunk.Builder.class);
    }

    // Construct using cn.com.unary.initcopy.grpc.entity.DiffFileChunk.newBuilder()
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
      }
    }
    public Builder clear() {
      super.clear();
      strongCheckCode_ = "";

      weakCheckCode_ = "";

      chunkSeq_ = "";

      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
        return cn.com.unary.initcopy.grpc.entity.TC_Entity.internal_static_cn_com_unary_initcopy_grpc_entity_DiffFileChunk_descriptor;
    }

    public cn.com.unary.initcopy.grpc.entity.DiffFileChunk getDefaultInstanceForType() {
      return cn.com.unary.initcopy.grpc.entity.DiffFileChunk.getDefaultInstance();
    }

    public cn.com.unary.initcopy.grpc.entity.DiffFileChunk build() {
      cn.com.unary.initcopy.grpc.entity.DiffFileChunk result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public cn.com.unary.initcopy.grpc.entity.DiffFileChunk buildPartial() {
      cn.com.unary.initcopy.grpc.entity.DiffFileChunk result = new cn.com.unary.initcopy.grpc.entity.DiffFileChunk(this);
      result.strongCheckCode_ = strongCheckCode_;
      result.weakCheckCode_ = weakCheckCode_;
      result.chunkSeq_ = chunkSeq_;
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
      if (other instanceof cn.com.unary.initcopy.grpc.entity.DiffFileChunk) {
        return mergeFrom((cn.com.unary.initcopy.grpc.entity.DiffFileChunk)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(cn.com.unary.initcopy.grpc.entity.DiffFileChunk other) {
      if (other == cn.com.unary.initcopy.grpc.entity.DiffFileChunk.getDefaultInstance()) return this;
      if (!other.getStrongCheckCode().isEmpty()) {
        strongCheckCode_ = other.strongCheckCode_;
        onChanged();
      }
      if (!other.getWeakCheckCode().isEmpty()) {
        weakCheckCode_ = other.weakCheckCode_;
        onChanged();
      }
      if (!other.getChunkSeq().isEmpty()) {
        chunkSeq_ = other.chunkSeq_;
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
      cn.com.unary.initcopy.grpc.entity.DiffFileChunk parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (cn.com.unary.initcopy.grpc.entity.DiffFileChunk) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private java.lang.Object strongCheckCode_ = "";
    /**
     * <pre>
     * 强校验码Md5
     * </pre>
     *
     * <code>optional string strongCheckCode = 1;</code>
     */
    public java.lang.String getStrongCheckCode() {
      java.lang.Object ref = strongCheckCode_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        strongCheckCode_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <pre>
     * 强校验码Md5
     * </pre>
     *
     * <code>optional string strongCheckCode = 1;</code>
     */
    public com.google.protobuf.ByteString
        getStrongCheckCodeBytes() {
      java.lang.Object ref = strongCheckCode_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        strongCheckCode_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <pre>
     * 强校验码Md5
     * </pre>
     *
     * <code>optional string strongCheckCode = 1;</code>
     */
    public Builder setStrongCheckCode(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      strongCheckCode_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * 强校验码Md5
     * </pre>
     *
     * <code>optional string strongCheckCode = 1;</code>
     */
    public Builder clearStrongCheckCode() {
      
      strongCheckCode_ = getDefaultInstance().getStrongCheckCode();
      onChanged();
      return this;
    }
    /**
     * <pre>
     * 强校验码Md5
     * </pre>
     *
     * <code>optional string strongCheckCode = 1;</code>
     */
    public Builder setStrongCheckCodeBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      strongCheckCode_ = value;
      onChanged();
      return this;
    }

    private java.lang.Object weakCheckCode_ = "";
    /**
     * <pre>
     * 弱校验码 CRC
     * </pre>
     *
     * <code>optional string weakCheckCode = 2;</code>
     */
    public java.lang.String getWeakCheckCode() {
      java.lang.Object ref = weakCheckCode_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        weakCheckCode_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <pre>
     * 弱校验码 CRC
     * </pre>
     *
     * <code>optional string weakCheckCode = 2;</code>
     */
    public com.google.protobuf.ByteString
        getWeakCheckCodeBytes() {
      java.lang.Object ref = weakCheckCode_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        weakCheckCode_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <pre>
     * 弱校验码 CRC
     * </pre>
     *
     * <code>optional string weakCheckCode = 2;</code>
     */
    public Builder setWeakCheckCode(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      weakCheckCode_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * 弱校验码 CRC
     * </pre>
     *
     * <code>optional string weakCheckCode = 2;</code>
     */
    public Builder clearWeakCheckCode() {
      
      weakCheckCode_ = getDefaultInstance().getWeakCheckCode();
      onChanged();
      return this;
    }
    /**
     * <pre>
     * 弱校验码 CRC
     * </pre>
     *
     * <code>optional string weakCheckCode = 2;</code>
     */
    public Builder setWeakCheckCodeBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      weakCheckCode_ = value;
      onChanged();
      return this;
    }

    private java.lang.Object chunkSeq_ = "";
    /**
     * <pre>
     * 块序号
     * </pre>
     *
     * <code>optional string chunkSeq = 3;</code>
     */
    public java.lang.String getChunkSeq() {
      java.lang.Object ref = chunkSeq_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        chunkSeq_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <pre>
     * 块序号
     * </pre>
     *
     * <code>optional string chunkSeq = 3;</code>
     */
    public com.google.protobuf.ByteString
        getChunkSeqBytes() {
      java.lang.Object ref = chunkSeq_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        chunkSeq_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <pre>
     * 块序号
     * </pre>
     *
     * <code>optional string chunkSeq = 3;</code>
     */
    public Builder setChunkSeq(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      chunkSeq_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * 块序号
     * </pre>
     *
     * <code>optional string chunkSeq = 3;</code>
     */
    public Builder clearChunkSeq() {
      
      chunkSeq_ = getDefaultInstance().getChunkSeq();
      onChanged();
      return this;
    }
    /**
     * <pre>
     * 块序号
     * </pre>
     *
     * <code>optional string chunkSeq = 3;</code>
     */
    public Builder setChunkSeqBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      chunkSeq_ = value;
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


    // @@protoc_insertion_point(builder_scope:cn.com.unary.initcopy.grpc.entity.DiffFileChunk)
  }

  // @@protoc_insertion_point(class_scope:cn.com.unary.initcopy.grpc.entity.DiffFileChunk)
  private static final cn.com.unary.initcopy.grpc.entity.DiffFileChunk DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new cn.com.unary.initcopy.grpc.entity.DiffFileChunk();
  }

  public static cn.com.unary.initcopy.grpc.entity.DiffFileChunk getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<DiffFileChunk>
      PARSER = new com.google.protobuf.AbstractParser<DiffFileChunk>() {
    public DiffFileChunk parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return new DiffFileChunk(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<DiffFileChunk> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<DiffFileChunk> getParserForType() {
    return PARSER;
  }

  public cn.com.unary.initcopy.grpc.entity.DiffFileChunk getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

