package cn.com.unary.initcopy.entity;

import org.junit.Test;

public class DeleteTaskDOTest {
    @Test
    public void test1 () {
        DeleteTaskDO taskDO = new DeleteTaskDO();
        taskDO.setDeleteFile(true);
        taskDO.setTaskId(1223);
        System.out.println(taskDO);
    }

}