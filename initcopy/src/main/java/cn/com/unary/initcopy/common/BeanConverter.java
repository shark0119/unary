package cn.com.unary.initcopy.common;

import cn.com.unary.initcopy.utils.ValidateUtils;
import com.google.protobuf.ByteString;
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

    public static final String OR_BUILDER_LIST = "OrBuilderList";
    public static final String BUILDER_LIST = "BuilderList";
    private static final Map<String, String> GRPC_EX_METHOD = new HashMap<>(15);
    private static final List<String> GRPC_LIST_SUFFIXES = new ArrayList<>(3);
    private static final String GRPC_MEMBER_GETTER_SUFFIX = "OrBuilder";
    private static final String GRPC_LIST_SETTER_PREFIX = "addAll";
    private static final String GRPC_CONSTANT_SUFFIX = "Value";
    private static final String GRPC_STRING_SUFFIX = "Bytes";
    private static final String GRPC_LIST_SUFFIX = "List";
    private static final String GET_PATTERN = "get";
    private static final String SET_PATTERN = "set";

    static {
        GRPC_EX_METHOD.put("getField", "");
        GRPC_EX_METHOD.put("getClass", "");
        GRPC_EX_METHOD.put("getAllFields", "");
        GRPC_EX_METHOD.put("getDescriptor", "");
        GRPC_EX_METHOD.put("getFieldBuilder", "");
        GRPC_EX_METHOD.put("getParserForType", "");
        GRPC_EX_METHOD.put("getUnknownFields", "");
        GRPC_EX_METHOD.put("getRepeatedField", "");
        GRPC_EX_METHOD.put("getSerializedSize", "");
        GRPC_EX_METHOD.put("getDescriptorForType", "");
        GRPC_EX_METHOD.put("getRepeatedFieldCount", "");
        GRPC_EX_METHOD.put("getOneofFieldDescriptor", "");
        GRPC_EX_METHOD.put("getRepeatedFieldBuilder", "");
        GRPC_EX_METHOD.put("getDefaultInstanceForType", "");
        GRPC_EX_METHOD.put("getInitializationErrorString", "");

        GRPC_EX_METHOD.put("setUnknownFields", "");
        GRPC_EX_METHOD.put("setRepeatedField", "");
        GRPC_EX_METHOD.put("setField", "");

        GRPC_LIST_SUFFIXES.add("Count");
        GRPC_LIST_SUFFIXES.add("BuilderList");
        GRPC_LIST_SUFFIXES.add("OrBuilderList");
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
    public static <T> T convert(Object source, Class<T> targetCls, String... ignoreProperties)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
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
    public static <T> T setFieldValue(Class<T> container, Map<String, Object> fieldValueMap)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
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
    public static Map<String, Object> getFieldValue(Object obj, String... ignoreProperties)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
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
     * 根据指定的模式来获取方法对象，用于获取 Getter 和 Setter
     * 如果是 GRPC 实体，则会自动过滤掉无效的方法。如果属性为
     * {@code Entity} 或者 {@code MessageOrBuilder} 对象，则递归获取。
     *
     * @param cls        类对象
     * @param methodType 指定是获取 Setter 还是 Getter
     * @param <T>        泛型
     * @return 属性名与方法的 Map
     */
    public static <T> Map<String, Method> getMethod(Class<T> cls, METHOD_TYPE methodType, String prefix)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        boolean isGrpcEntity = MessageOrBuilder.class.isAssignableFrom(cls);
        String pattern = METHOD_TYPE.GETTER.equals(methodType) ? GET_PATTERN : SET_PATTERN;
        prefix = ValidateUtils.isEmpty(prefix) ? "" : prefix + ".";
        Map<String, Method> fieldMethodMap = new HashMap<>(10);
        String fieldName = null, methodName;
        Class<?> memType;
        for (Method method : cls.getMethods()) {
            methodName = method.getName();
            if (methodName.startsWith(pattern)) {
                // Getter 的参数为空， Grpc 的 Setter 的返回值必须为当前对象或者子类
                if (methodType.equals(METHOD_TYPE.GETTER)) {
                    if (method.getParameterTypes().length != 0) {
                        continue;
                    }
                    memType = method.getReturnType();
                } else {
                    final boolean illegalSetter = (isGrpcEntity
                            && !cls.isAssignableFrom(method.getReturnType()))
                            || method.getParameterTypes().length != 1;
                    if (illegalSetter) {
                        continue;
                    }
                    memType = method.getParameterTypes()[0];
                }
                fieldName = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
                if (MessageOrBuilder.class.isAssignableFrom(memType)) {
                    // GRPC 实体
                    if (fieldMethodMap.containsKey(fieldName)) {
                        continue;
                    } else if (methodType.equals(METHOD_TYPE.SETTER)) {
                        // SETTER
                        if (fieldMethodMap.containsKey(fieldName)) {
                            continue;
                        }
                        try {
                            Class<?> clz = memType.getMethod("newBuilder").invoke(null).getClass();
                            fieldMethodMap.putAll(getMethod(clz, methodType, fieldName));
                        } catch (Exception e) {
                            fieldMethodMap.putAll(
                                    getMethod(memType, methodType, fieldName));
                        }
                    } else {
                        // GETTER
                        if (fieldName.endsWith(GRPC_MEMBER_GETTER_SUFFIX)) {
                            continue;
                        }
                        fieldMethodMap.putAll(
                                getMethod(memType.getMethod("newBuilder").invoke(null).getClass(),
                                        methodType, fieldName));
                    }
                } else if (Entity.class.isAssignableFrom(memType)) {
                    // POJO 实体
                    fieldMethodMap.putAll(getMethod(memType, methodType, fieldName));
                } else if (isGrpcEntity) {
                    fieldName = loadGrpcFieldName(fieldMethodMap, fieldName, methodName, memType);
                    if (fieldName == null) {
                        continue;
                    }
                }
            } else {
                if (isGrpcEntity && methodType.equals(METHOD_TYPE.SETTER)) {
                    if (method.getName().startsWith(GRPC_LIST_SETTER_PREFIX)) {
                        fieldName = Character.toLowerCase(methodName.charAt(6)) + methodName.substring(7);
                        if (fieldMethodMap.containsKey(fieldName)) {
                            fieldMethodMap.remove(fieldName);
                        }
                    }
                } else {
                    continue;
                }
            }
            fieldName = prefix + fieldName;
            if (fieldMethodMap.containsKey(fieldName)) {
                continue;
            }
            fieldMethodMap.put(fieldName, method);
        }
        fieldMethodMap.remove(null);
        fieldMethodMap.remove("null");
        return fieldMethodMap;
    }

    /**
     * 如果符合 Getter 和 Setter 的声明标准，则返回对应的成员名称。
     * 只针对于 GRPC
     *
     * @param fieldMethodMap 成员方法 Map
     * @param fieldName      成员名
     * @param methodName     方法名
     * @param memType        成员类型
     * @return 成员名称
     */
    private static String loadGrpcFieldName(Map<String, Method> fieldMethodMap, String fieldName,
                                            String methodName, Class<?> memType) {
        boolean grpcListExists;
        String memListName;
        // 忽略 GRPC 实体特有的方法
        final boolean grpcSpecificMtd = GRPC_EX_METHOD.containsKey(methodName);
        // 如果以 Bytes 结尾，符合 String 成员特点
        final boolean isStringFieldMtd = fieldName.endsWith(GRPC_STRING_SUFFIX)
                && ByteString.class.isAssignableFrom(memType);
        // 如果以 Value 结尾，且确认为 Enum 则略过本次循环
        final boolean isConstant = fieldName.endsWith(GRPC_CONSTANT_SUFFIX)
                && fieldMethodMap.containsKey(fieldName.substring(0, fieldName.length() - GRPC_CONSTANT_SUFFIX.length()));

        if (grpcSpecificMtd || isStringFieldMtd) {
            return null;
        } else {
            if (ProtocolMessageEnum.class.isAssignableFrom(memType)) {
                // 成员为 Enum 时
                fieldMethodMap.remove(fieldName + GRPC_CONSTANT_SUFFIX);
            } else if (isConstant) {
                return null;
            } else {
                if (fieldName.endsWith(GRPC_LIST_SUFFIX)
                        && List.class.isAssignableFrom(memType)) {
                    // 如果含有 List 后缀且返回值为 List，认定其为集合成员变量
                    if (fieldName.endsWith(OR_BUILDER_LIST)
                            || fieldName.endsWith(BUILDER_LIST)) {
                        return null;
                    }
                    fieldName = fieldName.substring(0, fieldName.length() - GRPC_LIST_SUFFIX.length());
                    for (String suffix : GRPC_LIST_SUFFIXES) {
                        if (fieldMethodMap.containsKey(fieldName + suffix)) {
                            fieldMethodMap.remove(fieldName + suffix);
                        }
                    }
                } else {
                    // 其他情况，包括普通成员变量，带有其他 list 后缀的 getter
                    grpcListExists = false;
                    for (String suffix : GRPC_LIST_SUFFIXES) {
                        if (fieldName.endsWith(suffix)) {
                            memListName = fieldName.substring(0, fieldName.length() - suffix.length());
                            // 如果符合 GRPC 的 List 的特色方法特征的话，结束本次循环，且集合中已经有了该成员
                            if (fieldMethodMap.containsKey(memListName)) {
                                grpcListExists = true;
                                break;
                            }
                        }
                    }
                    if (grpcListExists) {
                        return null;
                    }
                }
            }
        }
        return fieldName;
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
