package cn.com.unary.initcopy.dao;

import cn.com.unary.initcopy.InitCopyContext;
import cn.com.unary.initcopy.common.AbstractLoggable;
import cn.com.unary.initcopy.entity.FileInfoDO;
import cn.com.unary.initcopy.entity.SyncTaskDO;
import cn.com.unary.initcopy.utils.ValidateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private Map<String, FileInfoDO> fiMap;
    private Map<Integer, SyncTaskDO> syncTaskMap;

    public RamFileManager (){
        fiMap = new ConcurrentHashMap<>(InitCopyContext.TASK_NUMBER);
        syncTaskMap = new ConcurrentHashMap<>(InitCopyContext.TASK_NUMBER);
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
    public List<FileInfoDO> queryByTaskId(int taskId) {
        List<FileInfoDO> fileInfos = new ArrayList<>();
        for (String fileId : fiMap.keySet()) {
            if (fiMap.get(fileId).getTaskId() == taskId) {
                fileInfos.add(fiMap.get(fileId));
            }
        }
        return fileInfos;
    }

    @Override
    public List<FileInfoDO> queryUnSyncFileByTaskId(int taskId) {
        List<FileInfoDO> fileInfos = new ArrayList<>();
        for (String fileId : fiMap.keySet()) {
            if (fiMap.get(fileId).getTaskId() == taskId
                && !fiMap.get(fileId).getStateEnum().equals(FileInfoDO.STATE.SYNCED)) {
                fileInfos.add(fiMap.get(fileId));
            }
        }
        return fileInfos;
    }
    @Override
    public boolean taskFinished(int taskId) {
        for(FileInfoDO fi : queryByTaskId(taskId)) {
            if (!fi.getStateEnum().equals(FileInfoDO.STATE.SYNCED)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public SyncTaskDO queryTask(int taskId) {
        return syncTaskMap.get(taskId);
    }

    @Override
    public void deleteTask(int taskId) {
        syncTaskMap.remove(taskId);
    }

    @Override
    public SyncTaskDO saveTask(SyncTaskDO taskDO) {
        return syncTaskMap.put(taskDO.getTaskId(), taskDO);
    }
}