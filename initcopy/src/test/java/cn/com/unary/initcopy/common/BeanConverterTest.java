package cn.com.unary.initcopy.common;

import cn.com.unary.initcopy.entity.BaseFileInfoDO;
import cn.com.unary.initcopy.entity.ClientInitReqDO;
import cn.com.unary.initcopy.entity.ExecResultDO;
import cn.com.unary.initcopy.entity.SyncTaskDO;
import cn.com.unary.initcopy.entity.TaskStateDO;
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
        builder.setMsg("msg").setIsHealthy(true).setCode(3);
        /*ExecResultDO resultDO = BeanConverter.convert(builder.build(), ExecResultDO.class);
        System.out.println(resultDO);*/
        TaskState.Builder builder1 = TaskState.newBuilder();
        builder1.setExecResult(builder).setTaskId(2);

        TaskStateDO stateDO = BeanConverter.convert(builder1.build(), TaskStateDO.class);
        System.out.println(stateDO);

        SyncTask.Builder builder2 = SyncTask.newBuilder();
        builder2.addFile("file1").addFile("3");
        SyncTaskDO task = BeanConverter.convert(builder2.build(), SyncTaskDO.class);
        System.out.println(task);

        ClientInitReq.Builder builder3 = ClientInitReq.newBuilder();
        builder3.addFileBaseInfos(FileBaseInfo.newBuilder().setModifyTime(1000).build());
        ClientInitReqDO reqDO = BeanConverter.convert(builder3.build(), ClientInitReqDO.class);
        System.out.println(reqDO);
        BaseFileInfoDO infoDO = reqDO.getFileBaseInfos().get(0);
        System.out.println(infoDO);
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
        TaskState.Builder builder = TaskState.newBuilder();
        builder.setTaskId(1).setExecResult(ExecResult.newBuilder().setCode(1).setIsHealthy(true).setMsg("exec").build())
                .setProgressInfo(ProgressInfo.newBuilder().setSyncedFileNum(10).build());
        Map<String, Method> getter = BeanConverter.getMethod(TaskState.class,
                BeanConverter.METHOD_TYPE.GETTER, "");
        /*for (String key : getter.keySet()) {
            System.out.println("" + key + "\t" + getter.get(key).getName());
        }*/
        Map<String, Object> fieldValue = BeanConverter.getFieldValue(builder.build(), getter);
        for (String key : fieldValue.keySet()) {
            System.out.println("key:" + key + "; value:" + fieldValue.get(key));
        }
    }

    @Test
    public void getMethod() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Map<String, Method> map = BeanConverter.getMethod(TaskState.class,
                BeanConverter.METHOD_TYPE.GETTER, "");
        for (String key : map.keySet()) {
            System.out.println("" + key + "\n\t\t" + map.get(key).getName());
        }
        System.out.println(map.keySet().size());
        map = BeanConverter.filterMap(map, "progressInfo");
        for (String key : map.keySet()) {
            System.out.println("" + key + "\n\t\t" + map.get(key).getName());
        }
        System.out.println(map.keySet().size());
    }

}