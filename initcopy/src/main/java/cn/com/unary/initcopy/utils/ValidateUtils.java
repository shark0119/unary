package cn.com.unary.initcopy.utils;

import java.util.Collection;
import java.util.Objects;

/**
 * 对象验证工具
 * @author shark
 *
 */
public class ValidateUtils {

	private ValidateUtils () {}
	
	/**
	 * 判断是否为字符空串
	 * @param value
	 * @return
	 */
	public static boolean isEmpty (String value) {
		return value == null || value.isEmpty();
	}
	/**
	 * 判断是否为空集合
	 * @param c
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty (Collection c) {
		return c==null || c.isEmpty();
	}
	/**
	 * 判断一个对象是否为空，当对象为数组时，长度为0是也会抛出异常。
	 * @param object
	 */
	public static void requireNotEmpty (Object object) {
		Object [] objArr;
		if (object.getClass().isArray()) {
			objArr = (Object[]) object;
			if (objArr.length == 0)
				throw new NullPointerException();
		} else {
			Objects.requireNonNull(object);
		}
	}
}
