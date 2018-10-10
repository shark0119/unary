package cn.com.unary.initcopy.common;

public class BeanConverterTest {

    /*@Test
    public void test1 (){
        ProgressInfoDO infoDO = new ProgressInfoDO();
        infoDO.setStage(0);
        infoDO.setProgress(0);
        infoDO.setTotalFileNum(12);
        infoDO.setTotalFileSize(0);
        infoDO.setSyncedFileNum(0);
        infoDO.setSyncedFileSize(0);
        ExecResultDO resultDO = new ExecResultDO(true, 0,"");
        TaskStateDO stateDO = new TaskStateDO(1, resultDO, infoDO);
        TaskState state = BeanConverter.convert(stateDO, TaskState.class, true);
        System.out.println(state);
    }
    @Test
    public void convert() {
        System.out.println("************grpc2pojo******************");
        ExecResult.Builder builder = ExecResult.newBuilder();
        builder.setMsg("msg").setIsHealthy(true).setCode(3);
        ExecResultDO resultDO = BeanConverter.convert(builder.build(), ExecResultDO.class, true);
        System.out.println(resultDO);
        TaskState.Builder builder1 = TaskState.newBuilder();
        builder1.setExecResult(builder).setTaskId(2);

        TaskStateDO stateDO = BeanConverter.convert(builder1.build(), TaskStateDO.class, true);
        System.out.println(stateDO);

        SyncTask.Builder builder2 = SyncTask.newBuilder();
        builder2.addFiles("file1").addFiles("3");
        SyncTaskDO task = BeanConverter.convert(builder2.build(), SyncTaskDO.class, true);
        System.out.println(task);

        ClientInitReq.Builder builder3 = ClientInitReq.newBuilder();
        builder3.addBaseFileInfos(BaseFileInfo.newBuilder().setModifyTime(1000).build());
        ClientInitReqDO reqDO = BeanConverter.convert(builder3.build(), ClientInitReqDO.class, true);
        System.out.println(reqDO);
        System.out.println("************pojo2grpc******************");
        BaseFileInfoDO infoDO = new BaseFileInfoDO();
        infoDO.setFileId("fileId");
        infoDO.setFileSize(10000L);
        infoDO.setFullName("i modify just now..");
        BaseFileInfo info = BeanConverter.convert(infoDO, BaseFileInfo.class, true);
        System.out.println(info);
        System.out.println("************pojo2pojo******************");
        infoDO = BeanConverter.convert(infoDO, BaseFileInfoDO.class, true);
        System.out.println(infoDO);
        System.out.println("************grpc2grpc******************");
        info = BeanConverter.convert(info, BaseFileInfo.class, true);
        System.out.println(info);
        System.out.println(info.getModifyTime());
    }

    @Test
    public void test2() {
        ServerInitRespDO respDO = new ServerInitRespDO();
        ServerInitResp resp = BeanConverter.convert(respDO, ServerInitResp.class, true);
        System.out.println(resp);
        System.out.println(resp.getTaskId());
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
        *//*for (String key : getter.keySet()) {
            System.out.println("" + key + "\t" + getter.get(key).getName());
        }*//*
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
    }*/

}