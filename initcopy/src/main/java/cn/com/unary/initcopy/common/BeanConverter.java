package cn.com.unary.initcopy.common;

import cn.com.unary.initcopy.utils.ValidateUtils;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.ProtocolMessageEnum;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * GRPC 实体和 POJO 的转换。
 * 默认以属性名称匹配
 *
 * @author Shark.Yin
 * @since 1.0
 */
public class BeanConverter extends AbstractLogable {

    private static final Map<String, String> GRPC_EX_METHOD = new HashMap<>(6);
    private static final List<String> GRPC_LIST_SUFFIXES = new ArrayList<>(3);
    private static final String GRPC_LIST_SETTER_PREFIX = "addAll";
    private static final String GRPC_CONSTANT_SUFFIX = "Value";
    private static final String GRPC_STRING_SUFFIX = "Bytes";
    private static final String GRPC_LIST_SUFFIX = "List";
    private static final String GET_PATTERN = "get";
    private static final String SET_PATTERN = "set";

    static {
        GRPC_EX_METHOD.put("getDefaultInstanceForType", "");
        GRPC_EX_METHOD.put("getSerializedSize", "");
        GRPC_EX_METHOD.put("getParserForType", "");
        GRPC_EX_METHOD.put("getUnknownFields", "");
        GRPC_EX_METHOD.put("setUnknownFields", "");
        GRPC_EX_METHOD.put("setRepeatedField", "");
        GRPC_EX_METHOD.put("setField", "");
        GRPC_LIST_SUFFIXES.add("OrBuilder");
        GRPC_LIST_SUFFIXES.add("OrBuilderList");
        GRPC_LIST_SUFFIXES.add("Count");
    }

    private BeanConverter() {
    }

    /**
     * 对于参数中需要复制属性的实体类。
     * 如果是 POJO，应生成对应的 Getter 和 Setter，应提供一个默认无参的构造方法。
     * 如果是 GRPC 实体，应该内置了类型为 {@link MessageOrBuilder} 的 Builder。
     * 该 Builder 有相应的 Setter 方法且可以通过该 Builder 的 build() 方法来生成对应实体。
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
        } else if (!(source instanceof Entity)) {
            throw new UnsupportedOperationException("must implements Entity Or MessageOrBuilder!");
        }
        if (MessageOrBuilder.class.isAssignableFrom(targetCls)) {
            containerNotGrpc = false;
        } else if (!(Entity.class.isAssignableFrom(targetCls))) {
            throw new UnsupportedOperationException("must implements Entity Or MessageOrBuilder!");
        }
        Constructor<T> constructor = targetCls.getConstructor();
        T target = constructor.newInstance();
        // 如果两个都不是 GRPC 实体，直接使用 Spring 内置的类属性拷贝工具。
        if (objNotGrpc && containerNotGrpc) {
            BeanUtils.copyProperties(source, target, ignoreProperties);
            return target;
        }
        Map<String, Object> fieldValueMap = getFieldValue(source, ignoreProperties);
        T obj = setFieldValue(targetCls, fieldValueMap);
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
    public static <T> T setFieldValue(Class<T> container, Map<String, Object> fieldValueMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        boolean containerIsGrpc = MessageOrBuilder.class.isAssignableFrom(container);
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
        Map<String, Method> setters = getMethod(builder.getClass(), METHOD_TYPE.SETTER, "");
        Method method;
        for (String fieldName : setters.keySet()) {
            if (fieldValueMap.get(fieldName) != null) {
                method = setters.get(fieldName);
                if (fieldName.contains(".")) {
                    // 表示嵌套了对象
                    Object obj = setFieldValue(method.getParameterTypes()[0],
                            filterMap(fieldValueMap, fieldName.substring(0, fieldName.indexOf(".") + 1)));
                    method.invoke(builder, obj);
                } else {
                    method.invoke(builder, fieldValueMap.get(fieldName));
                }
            } else {
                logger.debug("Field " + fieldName + " ignore.");
            }
        }
        // GRPC 需要额外生成
        if (containerIsGrpc) {
            method = container.getMethod("build");
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
    public static Map<String, Object> getFieldValue(Object obj, String... ignoreProperties) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> ipMap = new HashMap<>(10);
        for (String ip : ignoreProperties) {
            ipMap.put(ip, null);
        }
        boolean isGrpcEntity = false;
        if (obj instanceof MessageOrBuilder) {
            isGrpcEntity = true;
        }
        Map<String, Object> fieldValueMap = new HashMap<>(100);
        Map<String, Method> getter = getMethod(obj.getClass(), METHOD_TYPE.GETTER, "");
        for (String fieldName : getter.keySet()) {
            // 忽略
            if (ipMap.containsKey(fieldName)) {
                continue;
            }
            //  GRPC 实体
            if (isGrpcEntity) {
                // 无效的方法
                if (fieldName.endsWith("bytes") &&
                        fieldValueMap.containsKey(fieldName.substring(0, fieldName.length() - 5))) {
                    continue;
                }
            }
            fieldValueMap.put(fieldName, getter.get(fieldName).invoke(obj));
        }
        return fieldValueMap;
    }

    /**
     * 根据指定的模式来获取方法对象，用于获取 Getter 和 Setter 并将权限设置为 public
     * 如果是 GRPC 实体，则会自动过滤掉无效的方法。如果属性为
     * {@code Entity} 或者 {@code MessageOrBuilder} 对象，则递归获取。
     *
     * @param cls        类对象
     * @param methodType 指定是获取 Setter 还是 Getter
     * @param <T>        泛型
     * @return 属性名与方法的 Map
     */
    public static <T> Map<String, Method> getMethod(Class<T> cls, METHOD_TYPE methodType, String prefix) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        boolean isGrpcEntity = MessageOrBuilder.class.isAssignableFrom(cls), grpcListExists;
        String pattern = METHOD_TYPE.GETTER.equals(methodType) ? GET_PATTERN : SET_PATTERN;
        prefix = ValidateUtils.isEmpty(prefix) ? "" : prefix + ".";
        Map<String, Method> fieldMethodMap = new HashMap<>(10);
        String fieldName = null, methodName, memListName;
        Class<?> returnType;
        for (Method method : cls.getMethods()) {
            methodName = method.getName();
            if (methodName.startsWith(pattern)) {
                method.setAccessible(true);
                fieldName = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
                returnType = method.getReturnType();
                if (Entity.class.isAssignableFrom(returnType)
                        || MessageOrBuilder.class.isAssignableFrom(returnType)) {
                    // 如果返回值是 GRPC 或 Entity 实体
                    if (methodType.equals(METHOD_TYPE.SETTER)
                            && MessageOrBuilder.class.isAssignableFrom(returnType)) {
                        // 如果是 Setter 且返回值为 GRPC 实体，那么需要另外构建一个该实体的 Builder
                        fieldMethodMap.putAll(
                                getMethod(
                                        method.getParameterTypes()[0].getMethod("newBuilder").invoke(null).getClass(),
                                        methodType, fieldName));
                    } else {
                        fieldMethodMap.putAll(getMethod(returnType, methodType, fieldName));
                    }
                } else if (isGrpcEntity) {
                    // 如果是 GRPC 实体
                    if (GRPC_EX_METHOD.containsKey(methodName)) {
                        // 如果是 GRPC 实体特有的方法，则跳过本次循环
                        continue;
                    } else if (fieldName.endsWith(GRPC_STRING_SUFFIX)
                            && fieldMethodMap.containsKey(fieldName.substring(0, GRPC_STRING_SUFFIX.length()))) {
                        // 如果以 Bytes 结尾，且已存在该成员
                        continue;
                    } else {
                        if (fieldMethodMap.containsKey(fieldName + GRPC_STRING_SUFFIX)) {
                            // 如果已经将 xxxBytes 放入 Map，则移除该元素
                            fieldMethodMap.remove(fieldName + GRPC_STRING_SUFFIX);
                        } else if (fieldName.endsWith(GRPC_LIST_SUFFIX)) {
                            // 如果含有 List 后缀且返回值为 List，认定其为集合成员变量
                            if (Collection.class.isAssignableFrom(returnType)) {
                                fieldName = fieldName.substring(0, fieldName.length() - GRPC_LIST_SUFFIX.length());
                            }
                        } else if (ProtocolMessageEnum.class.isAssignableFrom(returnType)) {
                            fieldMethodMap.remove(fieldName + GRPC_CONSTANT_SUFFIX);
                        } else if (fieldName.endsWith(GRPC_CONSTANT_SUFFIX)) {
                            // 如果以 Value 结尾，且确认为 Enum 则略过本次循环
                            if (fieldMethodMap.containsKey(fieldName.substring(0, fieldName.length() - GRPC_CONSTANT_SUFFIX.length()))) {
                                continue;
                            }
                        } else {
                            // 其他情况，包括普通成员变量，带有其他 list 后缀的 getter
                            grpcListExists = false;
                            for (String suffix : GRPC_LIST_SUFFIXES) {
                                memListName = fieldName.substring(0, fieldName.length() - suffix.length());
                                // 如果符合 GRPC 的 List 的特色方法特征的话，结束本次循环
                                if (fieldName.endsWith(suffix) && fieldMethodMap.containsKey(memListName)) {
                                    grpcListExists = true;
                                    break;
                                }
                                if (fieldMethodMap.containsKey(fieldName + suffix)) {
                                    fieldMethodMap.remove(fieldName + suffix);
                                    break;
                                }
                            }
                            if (grpcListExists) {
                                continue;
                            }
                        }
                    }
                } else {
                    // 如果不是 GRPC 实体
                    fieldName += prefix;
                }
            } else {
                if (isGrpcEntity && methodType.equals(METHOD_TYPE.SETTER)) {
                    if (method.getName().startsWith(GRPC_LIST_SETTER_PREFIX)) {
                        fieldName = methodName.substring(GRPC_LIST_SETTER_PREFIX.length());
                        if (fieldMethodMap.containsKey(fieldName)) {
                            fieldMethodMap.remove(fieldName);
                        }
                    }
                } else {
                    continue;
                }
            }
            if (fieldMethodMap.containsKey(fieldName)) {
                continue;
            }
            fieldMethodMap.put(fieldName, method);
        }
        fieldMethodMap.remove(null);
        return fieldMethodMap;
    }

    /**
     * 获取某个成员变量的所有属性-值 Map
     * 并从给定容器中移除这些键值对
     *
     * @param fieldValueMap 属性-值 Map
     * @param memVarPrefix  成员变量名
     * @return 符合的属性-值 Map
     */
    public static Map<String, Object> filterMap(Map<String, Object> fieldValueMap, String memVarPrefix) {
        Set<String> keySet = new HashSet<>(fieldValueMap.keySet());
        Map<String, Object> map = new HashMap<>(10);
        for (String key : keySet) {
            if (key.startsWith(memVarPrefix)) {
                map.put(key, fieldValueMap.get(key));
                fieldValueMap.remove(key);
            }
        }
        return map;
    }

    /**
     * 用于获取方法时，指定获取 Getter 还是 Setter
     */
    public enum METHOD_TYPE {
        // Getter 方法
        GETTER,
        // Setter 方法
        SETTER,
    }
}
