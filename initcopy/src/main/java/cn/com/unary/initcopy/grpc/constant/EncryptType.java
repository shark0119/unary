// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: init_copy_constant.proto

package cn.com.unary.initcopy.grpc.constant;

/**
 * <pre>
 *加密类型
 * </pre>
 *
 * Protobuf enum {@code cn.com.unary.initcopy.grpc.constant.EncryptType}
 */
public enum EncryptType
    implements com.google.protobuf.ProtocolMessageEnum {
  /**
   * <pre>
   *不加密
   * </pre>
   *
   * <code>ENCRYPT_NONE = 0;</code>
   */
  ENCRYPT_NONE(0),
  /**
   * <pre>
   *使用AES加密
   * </pre>
   *
   * <code>ENCRYPT_AES = 1;</code>
   */
  ENCRYPT_AES(1),
  UNRECOGNIZED(-1),
  ;

  /**
   * <pre>
   *不加密
   * </pre>
   *
   * <code>ENCRYPT_NONE = 0;</code>
   */
  public static final int ENCRYPT_NONE_VALUE = 0;
  /**
   * <pre>
   *使用AES加密
   * </pre>
   *
   * <code>ENCRYPT_AES = 1;</code>
   */
  public static final int ENCRYPT_AES_VALUE = 1;


  public final int getNumber() {
    if (this == UNRECOGNIZED) {
      throw new java.lang.IllegalArgumentException(
          "Can't get the number of an unknown enum value.");
    }
    return value;
  }

  /**
   * @deprecated Use {@link #forNumber(int)} instead.
   */
  @java.lang.Deprecated
  public static EncryptType valueOf(int value) {
    return forNumber(value);
  }

  public static EncryptType forNumber(int value) {
    switch (value) {
      case 0: return ENCRYPT_NONE;
      case 1: return ENCRYPT_AES;
      default: return null;
    }
  }

  public static com.google.protobuf.Internal.EnumLiteMap<EncryptType>
      internalGetValueMap() {
    return internalValueMap;
  }
  private static final com.google.protobuf.Internal.EnumLiteMap<
      EncryptType> internalValueMap =
        new com.google.protobuf.Internal.EnumLiteMap<EncryptType>() {
          public EncryptType findValueByNumber(int number) {
            return EncryptType.forNumber(number);
          }
        };

  public final com.google.protobuf.Descriptors.EnumValueDescriptor
      getValueDescriptor() {
    return getDescriptor().getValues().get(ordinal());
  }
  public final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptorForType() {
    return getDescriptor();
  }
  public static final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptor() {
    return cn.com.unary.initcopy.grpc.constant.IC_Constant.getDescriptor()
        .getEnumTypes().get(2);
  }

  private static final EncryptType[] VALUES = values();

  public static EncryptType valueOf(
      com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
    if (desc.getType() != getDescriptor()) {
      throw new java.lang.IllegalArgumentException(
        "EnumValueDescriptor is not for this type.");
    }
    if (desc.getIndex() == -1) {
      return UNRECOGNIZED;
    }
    return VALUES[desc.getIndex()];
  }

  private final int value;

  private EncryptType(int value) {
    this.value = value;
  }

  // @@protoc_insertion_point(enum_scope:cn.com.unary.initcopy.grpc.constant.EncryptType)
}

