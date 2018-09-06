package cn.com.unary.initcopy.common.utils;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * 对象验证工具
 *
 * @author shark
 */
public class ValidateUtils {

    private ValidateUtils() {
    }

    /**
     * 判断是否为字符空串
     *
     * @param value 待校验值
     * @return 空返回 True，非空返回 False
     */
    public static boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }

    /**
     * 判断是否为空集合
     *
     * @param c 待校验值
     * @return 空返回 True，非空返回 False
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Collection c) {
        return c == null || c.isEmpty();
    }

    /**
     * 判断一个数组是否为空
     *
     * @param object 待校验值
     * @throws NullPointerException null或者length 为0时，抛出异常
     */
    public static void requireNotEmpty(Object object) {
        Objects.requireNonNull(object);
        Object[] objArr;
        if (object.getClass().isArray()) {
            objArr = (Object[]) object;
            if (objArr.length == 0) {
                throw new NullPointerException();
            }
        }
    }

    /**
     * @param obj 待校验值
     * @throws NullPointerException null或者length 为0时，抛出异常
     */
    public static void requireNotEmpty(String obj) {
        if (obj == null || obj.isEmpty()) {
            throw new NullPointerException();
        }
    }

    /**
     * @param obj 待校验值
     * @throws NullPointerException null或者length 为0时，抛出异常
     */
    public static void requireNotEmpty(Map obj) {
        if (obj == null || obj.isEmpty()) {
            throw new NullPointerException();
        }
    }

    /**
     * @param obj 待校验值
     * @throws NullPointerException null或者length 为0时，抛出异常
     */
    public static void requireNotEmpty(Collection obj) {
        if (obj == null || obj.isEmpty()) {
            throw new NullPointerException();
        }
    }
}
