package cn.com.unary.initcopy.dao;

import cn.com.unary.initcopy.InitCopyContext;
import cn.com.unary.initcopy.common.AbstractLoggable;
import cn.com.unary.initcopy.common.utils.ValidateUtils;
import cn.com.unary.initcopy.entity.FileInfoDO;
import cn.com.unary.initcopy.grpc.entity.SyncTask;

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
    private Map<String, SyncTask> syncTaskMap;

    public RamFileManager() {
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
    public List<FileInfoDO> queryByTaskId(String taskId) {
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
        List<FileInfoDO> fileInfos = new ArrayList<>();
        for (Map.Entry<String, FileInfoDO> entry : fiMap.entrySet()) {
            if (entry.getValue().getTaskId().equals(taskId)
                    && !entry.getValue().getStateEnum().equals(FileInfoDO.STATE.SYNCED)) {
                fileInfos.add(entry.getValue());
            }
        }
        return fileInfos;
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
    public SyncTask queryTask(String taskId) {
        return syncTaskMap.get(taskId);
    }

    @Override
    public void deleteTask(String taskId) {
        syncTaskMap.remove(taskId);
        Map.Entry<String, FileInfoDO> entry;
        for (Iterator<Map.Entry<String, FileInfoDO>> iterator = fiMap.entrySet().iterator(); iterator.hasNext(); ) {
            entry = iterator.next();
            if (entry.getValue().getTaskId().equals(taskId)) {
                iterator.remove();
            }
        }
    }

    @Override
    public SyncTask saveTask(SyncTask task) {
        return syncTaskMap.put(task.getTaskId(), task);
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