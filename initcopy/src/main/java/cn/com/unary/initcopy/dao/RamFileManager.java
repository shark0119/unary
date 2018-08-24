package cn.com.unary.initcopy.dao;

import cn.com.unary.initcopy.entity.FileInfo;
import cn.com.unary.initcopy.utils.AbstractLogable;
import cn.com.unary.initcopy.utils.ValidateUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
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
@Repository("RamFileManager")
public class RamFileManager extends AbstractLogable implements FileManager {

    // key : fileId, field: 文件信息实体
    private static Map<String, FileInfo> fiMap = new ConcurrentHashMap<>();

    @Override
    public List<FileInfo> query(String... fileIds) {
        if (fileIds.length == 0)
            return null;
        List<FileInfo> fis = new ArrayList<>();
        for (String id : fileIds) {
            FileInfo fi = fiMap.get(id);
            if (fi == null) {
                logger.warn("No FileInfo With Id " + id);
                continue;
            }
            fis.add(fi);
        }
        return fis;
    }

    @Override
    public void save(FileInfo fi) {
        if (ValidateUtils.isEmpty(fi.getId())) {
            fi.setId(UUID.randomUUID().toString());
        }
        fiMap.put(fi.getId(), fi);
    }

    @Override
    public void save(List<FileInfo> fis) {
        for (FileInfo fi : fis) {
            this.save(fi);
        }
    }

    @Override
    public void delete(String... fileIds) {
        for (String fileId : fileIds) {
            this.delete(fileId);
        }
    }

    @Override
    public List<FileInfo> queryByTaskId(int taskId) {
        List<FileInfo> fileInfos = new ArrayList<>();
        for (String fileId : fiMap.keySet()) {
            if (fiMap.get(fileId).getTaskId() == taskId) {
                fileInfos.add(fiMap.get(fileId));
            }
        }
        return fileInfos;
    }
}