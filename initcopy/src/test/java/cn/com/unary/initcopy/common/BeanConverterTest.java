package cn.com.unary.initcopy.common;

import cn.com.unary.initcopy.grpc.entity.ClientInitReq;
import cn.com.unary.initcopy.grpc.entity.DeleteTask;
import cn.com.unary.initcopy.grpc.entity.ExecResult;
import cn.com.unary.initcopy.grpc.entity.TaskState;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

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
        Map<String, Method> map = BeanConverter.getMethod(TaskState.Builder.class,
                BeanConverter.METHOD_TYPE.SETTER, "");
        for (String key : map.keySet()) {
            System.out.println("field:" + key + " and method :" + map.get(key).getName());
        }
        System.out.println(map.keySet().size());
        TaskState.Builder builder = TaskState.newBuilder();
        TaskState state = builder.build();
        ExecResult.Builder builder1 = ExecResult.newBuilder();


    }

    @Test
    public void filterMap() {
        for (Method method : ClientInitReq.class.getDeclaredMethods()) {
            System.out.println(method.getName());
        }
        System.out.println(ClientInitReq.class.getDeclaredMethods().length);
        System.out.println(ClientInitReq.class.getMethods().length);
        ClientInitReq.Builder builder = ClientInitReq.newBuilder();
    }
}