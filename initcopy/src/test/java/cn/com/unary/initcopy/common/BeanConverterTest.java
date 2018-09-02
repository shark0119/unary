package cn.com.unary.initcopy.common;

import cn.com.unary.initcopy.entity.ExecResultDO;
import cn.com.unary.initcopy.grpc.constant.SyncType;
import cn.com.unary.initcopy.grpc.entity.*;
import javafx.concurrent.Task;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BeanConverterTest {

    @Test
    public void convert() throws Exception {
        ExecResult.Builder builder = ExecResult.newBuilder();
        builder.setMsg("msg").setIsHealthy(false).setCode(3);
        ExecResultDO resultDO = BeanConverter.convert(builder.build(), ExecResultDO.class);
        System.out.println(resultDO);
    }

    @Test
    public void setFieldValue() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        ExecResult.Builder builder = ExecResult.newBuilder();
        builder.setMsg("msg").setIsHealthy(false).setCode(3);
        Map<String, Method> getter = BeanConverter.getMethod(ExecResult.class,
                BeanConverter.METHOD_TYPE.GETTER, "");
        Map<String, Object> fieldValue = BeanConverter.getFieldValue(builder.build(), getter);
        ExecResultDO execResultDO = BeanConverter.setFieldValue(ExecResultDO.class, fieldValue);
        System.out.println(execResultDO);
    }

    @Test
    public void getFieldValue() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        FileBaseInfo.Builder builder1 = FileBaseInfo.newBuilder();
        FileBaseInfo.Builder builder2 = FileBaseInfo.newBuilder();
        builder1.setFileId("fileid").setTaskId(11).setCheckSum("checkSum");
        builder2.mergeFrom(builder1.build());
        List<FileBaseInfo> fbis = new ArrayList<>();
        fbis.add(builder1.build());
        ClientInitReq.Builder builder = ClientInitReq.newBuilder();
        builder.setTaskId(1).setTargetDir("any").setTotalSize(1034L).addAllFileBaseInfos(fbis).setSyncType(SyncType.SYNC_DIFF);
        Map<String, Method> getter = BeanConverter.getMethod(ClientInitReq.class,
                BeanConverter.METHOD_TYPE.GETTER, "");
        for (String key : getter.keySet()) {
            System.out.println("" + key + "\t" + getter.get(key).getName());
        }
        Map<String, Object> fieldValue = BeanConverter.getFieldValue(builder.build(), getter);
        for (String key : fieldValue.keySet()) {
            System.out.println(fieldValue.get(key));
        }
    }

    @Test
    public void getMethod() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Map<String, Method> map = BeanConverter.getMethod(ExecResultDO.class,
                BeanConverter.METHOD_TYPE.SETTER, "");
        for (String key : map.keySet()) {
            System.out.println("" + key + "\n\t\t" + map.get(key).getName());
        }
        System.out.println(map.keySet().size());
    }

}