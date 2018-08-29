package cn.com.unary.initcopy.common;

import com.google.protobuf.MessageOrBuilder;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * GRPC 实体和 POJO 的转换。
 * 默认以属性名称匹配
 *
 * @author Shark.Yin
 * @since 1.0
 */
public class BeanConverter {

    private BeanConverter() {
    }

    /**
     * 对于参数中需要复制属性的实体类。
     * 如果是 POJO，应生成对应的 Getter 和 Setter，应提供一个默认无参的构造方法。
     * 如果是 GRPC 实体，应该内置了类型为 {@link MessageOrBuilder} 的 Builder。
     * 该 Builder有相应的 Setter 方法且可以通过该 Builder 的 build() 方法来生成对应实体。
     * 该 GRPC 实体应有属性相对应的 Getter 方法。
     *
     * @param source    实体
     * @param targetCls 容纳属性的容器
     * @param <T>       实体
     * @return 已填充的实体
     */
    public static <T> T convert(Object source, Class<T> targetCls, String... ignoreProperties) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Objects.requireNonNull(targetCls);
        Objects.requireNonNull(source);
        boolean objNotGrpc = true, containerNotGrpc = true;
        if (source instanceof MessageOrBuilder) {
            objNotGrpc = false;
        }
        if (targetCls.isAssignableFrom(MessageOrBuilder.class)) {
            containerNotGrpc = false;
        }
        Constructor<T> constructor = targetCls.getConstructor();
        T target = constructor.newInstance();
        // 如果两个都不是 GRPC 实体，直接使用 Spring 内置的类属性拷贝工具。
        if (objNotGrpc && containerNotGrpc) {
            BeanUtils.copyProperties(source, target, ignoreProperties);
            return target;
        }
        Map<String, Object> fieldValueMap = gainObjFieldValue(source, ignoreProperties);
        T obj = fillObj(targetCls, fieldValueMap);
        return obj;
    }

    /**
     * 将成员及其值填入指定容器中
     * 通过 Setter 填入指定容器
     * 如果实体是 {@link MessageOrBuilder} 的子类，则先构建 Builder 在进行实例生成。
     * 不论访问权限，容器都应该声明一个默认的构造方法。
     *
     * @param container     容器的 Class
     * @param fieldValueMap 成员-值 Map
     */
    private static <T> T fillObj(Class<T> container, Map<String, Object> fieldValueMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        boolean containerIsGrpc = false;
        if (container.isAssignableFrom(MessageOrBuilder.class)) {
            containerIsGrpc = true;
        }
        Object builder;
        // 生成实例
        if (containerIsGrpc) {
            Method method = container.getMethod("newBuilder");
            builder = method.invoke(null);
        } else {
            Constructor<T> constructor = container.getConstructor();
            constructor.setAccessible(true);
            builder = constructor.newInstance();
        }
        // TODO set properties

        // GRPC 需要额外生成
        if (containerIsGrpc) {
            Method method = container.getMethod("build");
            builder = method.invoke(builder);
        }
        return (T) builder;
    }

    /**
     * 从指定的对象中获取 成员-值 Map
     * 通过 Getter 来生成该 Map，如果成员是对象，则继续遍历下去。
     *
     * @param obj 指定的对象
     * @return 成员-值 Map
     */
    private static Map<String, Object> gainObjFieldValue(Object obj, String... ignoreProperties) {
        Map<String, String> ipMap = new HashMap<>(100);
        for (String ip : ignoreProperties) {
            ipMap.put(ip, null);
        }
        if (obj instanceof MessageOrBuilder) {
            ipMap.put("*bytes", null);
        }
        Map<String, Object> fieldValueMap = new HashMap<>(100);
        // TODO gain properties

        return fieldValueMap;
    }
}
