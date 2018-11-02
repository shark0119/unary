package cn.com.unary.initcopy.service.taskupdater;

import cn.com.unary.initcopy.common.AbstractLoggable;
import cn.com.unary.initcopy.common.utils.ValidateUtils;
import cn.com.unary.initcopy.dao.FileManager;
import cn.com.unary.initcopy.entity.FileInfoDO;
import cn.com.unary.initcopy.entity.SyncTaskDO;
import cn.com.unary.initcopy.exception.TaskFailException;
import cn.com.unary.initcopy.grpc.entity.DeleteTask;
import cn.com.unary.initcopy.grpc.entity.ProgressInfo;
import cn.com.unary.initcopy.grpc.entity.SyncProcess;
import cn.com.unary.initcopy.grpc.entity.TaskState;
import cn.com.unary.initcopy.service.filecopy.ServerFileCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 目标端的任务修改器
 * 线程安全
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Component("ServerTaskUpdater")
public class ServerTaskUpdater extends AbstractLoggable {

    private static final String ZERO_STR = "0";
    private static final BigDecimal ZERO = new BigDecimal(ZERO_STR);
    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");
    @Autowired
    @Qualifier("serverFM")
    private FileManager fm;
    @Autowired
    private ServerFileCopy fileCopy;

    /**
     * 删除任务
     *
     * @param task 任务相关信息实体
     */
    public void delete(DeleteTask task) throws TaskFailException {
        // 暂停当前任务
        try {
            fileCopy.deleteTask(task.getTaskId());
        } catch (IOException e) {
            throw new TaskFailException(e);
        }
        if (task.getDeleteFile()) {
            // TODO delete server files.
        }
        // 删除任务相关信息
        fm.deleteFileInfoByTaskId(task.getTaskId());
    }

    /**
     * 修改任务相关信息实体
     *
     * @param taskId 任务 ID
     */
    public SyncProcess resume(String taskId) {
        return fileCopy.resume(taskId);
    }

    public TaskState query(String taskId) throws TaskFailException {
        List<FileInfoDO> fis = fm.queryByTaskId(taskId);
        SyncTaskDO task = fm.queryTask(taskId);
        if (task == null) {
            throw new TaskFailException(String.format("Task %s not ready.", taskId));
        }
        long syncedFileNum = 0L;
        BigDecimal syncedFileSize = new BigDecimal(ZERO_STR);
        String syncingFileName = "";
        ProgressInfo.Builder progress = ProgressInfo.newBuilder();
        if (fis.isEmpty()) {
            progress
                    .setProgress(100)
                    .setSyncingFileName("")
                    .setTotalFileNum(task.getTotalFileNum())
                    .setSyncedFileNum(task.getTotalFileNum())
                    .setSyncedFileSize(task.getTotalSize().toString())
                    .setTotalFileSize(task.getTotalSize().toString());
        } else {
            for (FileInfoDO infoDO : fis) {
                switch (infoDO.getStateEnum()) {
                    case WAIT:
                        break;
                    case SYNCED:
                        syncedFileNum++;
                        syncedFileSize = syncedFileSize.add(new BigDecimal(infoDO.getFileSize().toString()));
                        break;
                    case SYNCING:
                    default:
                        if (!ValidateUtils.isEmpty(syncingFileName)) {
                            throw new TaskFailException("error state, more than one file in syncing state.");
                        }
                        syncingFileName = infoDO.getFullName();
                        break;
                }
            }
            if (!syncedFileSize.equals(ZERO)) {
                progress.setProgress(syncedFileSize.divide(task.getTotalSize(), 2, RoundingMode.CEILING).multiply(ONE_HUNDRED).intValue());
            } else {
                progress.setProgress(0);
            }
            progress.setTotalFileNum(fis.size())
                    .setSyncedFileNum(syncedFileNum).setSyncedFileSize(syncedFileSize.toString())
                    .setSyncingFileName(syncingFileName).setTotalFileSize(task.getTotalSize().toString());
            task.setSyncedSize(syncedFileSize);
        }
        fm.saveTask(task);

        return TaskState.newBuilder().setProgressInfo(progress).build();
    }
}
