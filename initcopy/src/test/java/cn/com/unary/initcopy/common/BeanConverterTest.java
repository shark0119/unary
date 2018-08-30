package cn.com.unary.initcopy.common;

import cn.com.unary.initcopy.grpc.entity.ClientInitReq;
import com.google.protobuf.MessageOrBuilder;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.Assert.*;

public class BeanConverterTest {

    @Test
    public void convert() {
    }

    @Test
    public void setFieldValue() {
    }

    @Test
    public void getFieldValue() {
    }

    @Test
    public void getMethod() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Map<String, Method> map = BeanConverter.getMethod(ClientInitReq.Builder.class,
                BeanConverter.METHOD_TYPE.SETTER, "");
        for (String key : map.keySet()) {
            System.out.println("field:" + key + " and method :" + map.get(key).getName());
        }
    }

    @Test
    public void filterMap() {
    }
}