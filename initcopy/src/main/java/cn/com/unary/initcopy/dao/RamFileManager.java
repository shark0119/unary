package cn.com.unary.initcopy.dao;

import cn.com.unary.initcopy.InitCopyContext;
import cn.com.unary.initcopy.common.AbstractLoggable;
import cn.com.unary.initcopy.common.utils.ValidateUtils;
import cn.com.unary.initcopy.entity.FileInfoDO;
import cn.com.unary.initcopy.entity.SyncTaskDO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 文件信息管理，存储在内存中，Java对象维护方式
 *
 * @author shark
 */
public class RamFileManager extends AbstractLoggable implements FileManager {

    /**
     * key : fileId, field: 文件信息实体
     */
    private final Map<String, FileInfoDO> fiMap;
    private final Map<String, SyncTaskDO> taskMap;

    public RamFileManager() {
        fiMap = new ConcurrentHashMap<>(InitCopyContext.TASK_NUMBER);
        taskMap = new ConcurrentHashMap<>(InitCopyContext.TASK_NUMBER);
    }

    @Override
    public FileInfoDO save(FileInfoDO fi) {
        if (ValidateUtils.isEmpty(fi.getFileId())) {
            fi.setFileId(UUID.randomUUID().toString());
        }
        return fiMap.put(fi.getFileId(), fi);
    }

    @Override
    public void save(List<FileInfoDO> fis) {
        for (FileInfoDO fi : fis) {
            this.save(fi);
        }
    }

    @Override
    public void deleteByIds(String... fileIds) {
        for (String fileId : fileIds) {
            fiMap.remove(fileId);
        }
    }

    @Override
    public void deleteFileInfoByTaskId(String taskId) {
        Map.Entry<String, FileInfoDO> entry;
        SyncTaskDO taskDO = taskMap.get(taskId);
        long totalFileNum = 0;
        Iterator<Map.Entry<String, FileInfoDO>> iterator = fiMap.entrySet().iterator();
        while (iterator.hasNext()) {
            entry = iterator.next();
            if (entry.getValue().getTaskId().equals(taskId)) {
                iterator.remove();
                totalFileNum++;
            }
        }
        if (taskDO != null && totalFileNum != 0L) {
            taskDO.setTotalFileNum(totalFileNum);
        }
    }

    @Override
    public List<FileInfoDO> queryByTaskId(String taskId) {
        Objects.requireNonNull(taskId);
        List<FileInfoDO> fileInfos = new ArrayList<>();
        for (Map.Entry<String, FileInfoDO> entry : fiMap.entrySet()) {
            if (fiMap.get(entry.getKey()).getTaskId().equals(taskId)) {
                fileInfos.add(entry.getValue());
            }
        }
        logger.info(String.format("%d file in task:%s", fileInfos.size(), taskId));
        return fileInfos;
    }

    @Override
    public List<FileInfoDO> queryUnSyncFileByTaskId(String taskId) {
        List<FileInfoDO> fileInfoList = new ArrayList<>();
        for (Map.Entry<String, FileInfoDO> entry : fiMap.entrySet()) {
            if (entry.getValue().getTaskId().equals(taskId)
                    && !entry.getValue().getStateEnum().equals(FileInfoDO.STATE.SYNCED)) {
                fileInfoList.add(entry.getValue());
            }
        }
        return fileInfoList;
    }

    @Override
    public boolean taskFinished(String taskId) {
        List<FileInfoDO> fis = queryByTaskId(taskId);
        for (FileInfoDO fi : fis) {
            if (!FileInfoDO.STATE.SYNCED.equals(fi.getStateEnum())) {
                logger.info(String.format("fi:%s", fi.toString()));
                return false;
            }
        }
        return true;
    }

    @Override
    public SyncTaskDO queryTask(String taskId) {
        return taskMap.get(taskId);
    }

    @Override
    public void deleteTask(String taskId) {
        this.deleteFileInfoByTaskId(taskId);
        taskMap.remove(taskId);
    }

    @Override
    public SyncTaskDO saveTask(SyncTaskDO task) {
        return taskMap.put(task.getTaskId(), task);
    }

    @Override
    public List<FileInfoDO> queryByIds(String... fileIds) {
        if (fileIds.length == 0) {
            return null;
        }
        List<FileInfoDO> fis = new ArrayList<>();
        for (String id : fileIds) {
            FileInfoDO fi = fiMap.get(id);
            if (fi == null) {
                logger.warn("No FileInfo With Id " + id);
                continue;
            }
            fis.add(fi);
        }
        return fis;
    }

    @Override
    public void updatePreviousFilesToUnSync(String taskId, String fileId) {
        FileInfoDO fi = fiMap.get(fileId);
        if (!FileInfoDO.STATE.SYNCED.equals(fi.getState())) {
            return;
        }
        for (Map.Entry<String, FileInfoDO> entry : fiMap.entrySet()) {
            if (!taskId.equals(entry.getValue().getTaskId())) {
                continue;
            }
            if (!FileInfoDO.STATE.SYNCED.equals(entry.getValue().getState())) {
                continue;
            }
            if (entry.getValue().getSyncDoneTime() > fi.getSyncDoneTime()) {
                entry.getValue().setState(FileInfoDO.STATE.WAIT);
                entry.getValue().setSyncDoneTime(null);
            }
        }
        fi.setSyncDoneTime(null);
        fi.setState(FileInfoDO.STATE.SYNCING);
    }
}