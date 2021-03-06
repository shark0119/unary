// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: init_copy_entity.proto

package cn.com.unary.initcopy.grpc.entity;

public interface SyncTaskOrBuilder extends
    // @@protoc_insertion_point(interface_extends:cn.com.unary.initcopy.grpc.entity.SyncTask)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   *任务ID
   * </pre>
   *
   * <code>optional string taskId = 1;</code>
   */
  java.lang.String getTaskId();
  /**
   * <pre>
   *任务ID
   * </pre>
   *
   * <code>optional string taskId = 1;</code>
   */
  com.google.protobuf.ByteString
      getTaskIdBytes();

  /**
   * <pre>
   * 目标端IP及端口信息
   * </pre>
   *
   * <code>optional .cn.com.unary.initcopy.grpc.entity.SyncTarget targetInfo = 2;</code>
   */
  boolean hasTargetInfo();
  /**
   * <pre>
   * 目标端IP及端口信息
   * </pre>
   *
   * <code>optional .cn.com.unary.initcopy.grpc.entity.SyncTarget targetInfo = 2;</code>
   */
  cn.com.unary.initcopy.grpc.entity.SyncTarget getTargetInfo();
  /**
   * <pre>
   * 目标端IP及端口信息
   * </pre>
   *
   * <code>optional .cn.com.unary.initcopy.grpc.entity.SyncTarget targetInfo = 2;</code>
   */
  cn.com.unary.initcopy.grpc.entity.SyncTargetOrBuilder getTargetInfoOrBuilder();

  /**
   * <pre>
   *同步方式
   * </pre>
   *
   * <code>optional .cn.com.unary.initcopy.grpc.constant.SyncType syncType = 3;</code>
   */
  int getSyncTypeValue();
  /**
   * <pre>
   *同步方式
   * </pre>
   *
   * <code>optional .cn.com.unary.initcopy.grpc.constant.SyncType syncType = 3;</code>
   */
  cn.com.unary.initcopy.grpc.constant.SyncType getSyncType();

  /**
   * <pre>
   *压缩方式
   * </pre>
   *
   * <code>optional .cn.com.unary.initcopy.grpc.constant.CompressType compressType = 4;</code>
   */
  int getCompressTypeValue();
  /**
   * <pre>
   *压缩方式
   * </pre>
   *
   * <code>optional .cn.com.unary.initcopy.grpc.constant.CompressType compressType = 4;</code>
   */
  cn.com.unary.initcopy.grpc.constant.CompressType getCompressType();

  /**
   * <pre>
   *加密方式
   * </pre>
   *
   * <code>optional .cn.com.unary.initcopy.grpc.constant.EncryptType encryptType = 5;</code>
   */
  int getEncryptTypeValue();
  /**
   * <pre>
   *加密方式
   * </pre>
   *
   * <code>optional .cn.com.unary.initcopy.grpc.constant.EncryptType encryptType = 5;</code>
   */
  cn.com.unary.initcopy.grpc.constant.EncryptType getEncryptType();

  /**
   * <pre>
   *打包类型
   * </pre>
   *
   * <code>optional .cn.com.unary.initcopy.grpc.constant.PackType packType = 6;</code>
   */
  int getPackTypeValue();
  /**
   * <pre>
   *打包类型
   * </pre>
   *
   * <code>optional .cn.com.unary.initcopy.grpc.constant.PackType packType = 6;</code>
   */
  cn.com.unary.initcopy.grpc.constant.PackType getPackType();

  /**
   * <pre>
   *限速 以M为单位  0为不限速
   * </pre>
   *
   * <code>optional int32 speedLimit = 7;</code>
   */
  int getSpeedLimit();

  /**
   * <pre>
   *要同步的目标目录
   * </pre>
   *
   * <code>repeated string targetDirs = 8;</code>
   */
  java.util.List<java.lang.String>
      getTargetDirsList();
  /**
   * <pre>
   *要同步的目标目录
   * </pre>
   *
   * <code>repeated string targetDirs = 8;</code>
   */
  int getTargetDirsCount();
  /**
   * <pre>
   *要同步的目标目录
   * </pre>
   *
   * <code>repeated string targetDirs = 8;</code>
   */
  java.lang.String getTargetDirs(int index);
  /**
   * <pre>
   *要同步的目标目录
   * </pre>
   *
   * <code>repeated string targetDirs = 8;</code>
   */
  com.google.protobuf.ByteString
      getTargetDirsBytes(int index);

  /**
   * <pre>
   *要同步的源文件
   * </pre>
   *
   * <code>repeated string files = 9;</code>
   */
  java.util.List<java.lang.String>
      getFilesList();
  /**
   * <pre>
   *要同步的源文件
   * </pre>
   *
   * <code>repeated string files = 9;</code>
   */
  int getFilesCount();
  /**
   * <pre>
   *要同步的源文件
   * </pre>
   *
   * <code>repeated string files = 9;</code>
   */
  java.lang.String getFiles(int index);
  /**
   * <pre>
   *要同步的源文件
   * </pre>
   *
   * <code>repeated string files = 9;</code>
   */
  com.google.protobuf.ByteString
      getFilesBytes(int index);
}
