package cn.com.unary.initcopy.entity;

/**
 * 任务删除的相关信息
 *
 * @author Shark.Yin
 * @since 1.0
 */
public class DeleteTaskDO {
    private Integer taskId;
    private Boolean deleteFile;

    @Override
    public String toString() {
        return "DeleteTaskDO{" +
                "taskId=" + taskId +
                ", deleteFile=" + deleteFile +
                '}';
    }

    public boolean isDeleteFile() {
        return deleteFile;
    }

    public void setDeleteFile(boolean deleteFile) {
        this.deleteFile = deleteFile;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}
