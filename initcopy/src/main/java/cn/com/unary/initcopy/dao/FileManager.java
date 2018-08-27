package cn.com.unary.initcopy.dao;

import java.io.IOException;
import java.util.List;

import cn.com.unary.initcopy.entity.FileInfo;

/**
 * 用于持久化文件的相关信息
 * @author shark
 *
 */
public interface FileManager {
	/**
	 * 根据指定的文件 ID 集合查询出文件信息集合
	 * @param fileIds 文件ID
	 * @return Id为空时返回空集合
	 */
	List<FileInfo> query (String...fileIds);
	/**
	 * 保存文件实体信息
	 * @param fi 文件信息实体
	 */
	void save (FileInfo fi);
	/**
	 * 保存传进来的数据，如无文件Id，则做新增操作，自动生成Id。
	 * 如有文件Id，则做保存操作。
	 * @param fis 文件信息实体
	 */
	void save (List<FileInfo> fis);
	/**
	 * 根据文件Id集合删除文件实体信息
	 * @param fileIds 文件ID
	 */
	void delete (String...fileIds);

	List<FileInfo> queryByTaskId(int taskId);

	boolean taskFinished(int taskId);
}
