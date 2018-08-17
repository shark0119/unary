package cn.com.unary.initcopy.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import cn.com.unary.initcopy.entity.FileInfo;
import cn.com.unary.initcopy.utils.AbstractLogable;
import cn.com.unary.initcopy.utils.ValidateUtils;

/**
 * 文件信息管理，存储在内存中，Java对象维护方式
 * @author shark
 *
 */
public class RamFileManager extends AbstractLogable implements FileManager {

	private static Map<String, FileInfo> fiMap = new ConcurrentHashMap<>();

	@Override
	public List<FileInfo> query(String...fileIds) {
		if (fileIds.length == 0)
			return null;
		List<FileInfo> fis = new ArrayList<>();
		for (String id : fileIds) {
			FileInfo fi = fiMap.get(id); 
			if (fi == null) {
				logger.warn("No FileInfo With Id {1}", id);
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
	public void delete(String...fileIds) {
		for (String fileId : fileIds) {
			this.delete(fileId);
		}
	}
}