package cn.com.unary.initcopy.utils;

import com.google.protobuf.MessageOrBuilder;

/**
 * GRPC 实体和 POJO 的转换。
 * 默认以属性名称匹配
 *
 * @since 1.0
 * @author Shark.Yin
 */
public class GrpcEntityConvert {

    /**
     * 将 GRPC实体转成 POJO
     * @param message GRPC 实体
     * @param container 容纳属性的容器
     * @param <T> POJO实体
     * @return 已填充的POJO实体
     */
    public <T> T convert (MessageOrBuilder message, T container) {

        MessageOrBuilder builder ;

        return container;
    }

    /**
     * 将实体转换成 GRPC 实体
     * @param message GRPC 实体的Class
     * @param entity 普通 POJO
     * @param <B> 应该继承于 {@link MessageOrBuilder}
     * @param <E> 实体属性容器
     * @return 转换后的属性实体
     */
    public <B extends MessageOrBuilder, E> B builder (Class<B> message, E entity) {
        MessageOrBuilder builder = null;

        B grpcEntity = null;

        return grpcEntity;
    }
}
