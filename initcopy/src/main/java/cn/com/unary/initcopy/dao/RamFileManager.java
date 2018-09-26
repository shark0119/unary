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
        for (Map.Entry<String, FileInfoDO> entry: fiMap.entrySet()) {
            if (fiMap.get(entry.getKey()).getTaskId() == taskId) {
                fileInfos.add(entry.getValue());
            }
        }
        logger.info(String.format("%d file in task:%d", fileInfos.size(), taskId));
        return fileInfos;
    }

    @Override
    public List<FileInfoDO> queryUnSyncFileByTaskId(int taskId) {
        List<FileInfoDO> fileInfos = new ArrayList<>();
        for (Map.Entry<String, FileInfoDO> entry: fiMap.entrySet()) {
            if (entry.getValue().getTaskId() == taskId
                && !entry.getValue().getStateEnum().equals(FileInfoDO.STATE.SYNCED)) {
                fileInfos.add(entry.getValue());
            }
        }
        return fileInfos;
    }

    @Override
    public boolean taskFinished(int taskId) {
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
    public SyncTaskDO queryTask(int taskId) {
        return syncTaskMap.get(taskId);
    }

    @Override
    public void deleteTask(int taskId) {
        syncTaskMap.remove(taskId);
        Map.Entry<String, FileInfoDO> entry;
        for (Iterator<Map.Entry <String, FileInfoDO>> iterator = fiMap.entrySet().iterator(); iterator.hasNext();) {
           entry = iterator.next();
           if (entry.getValue().getTaskId() == taskId) {
               iterator.remove();
           }
        }
    }

    @Override
    public SyncTaskDO saveTask(SyncTaskDO taskDO) {
        return syncTaskMap.put(taskDO.getTaskId(), taskDO);
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
}