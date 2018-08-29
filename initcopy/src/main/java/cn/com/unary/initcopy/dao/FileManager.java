package cn.com.unary.initcopy.dao;

import cn.com.unary.initcopy.entity.FileInfo;

import java.util.List;

/**
 * 用于持久化文件的相关信息
 *
 * @author shark
 */
public interface FileManager {
    /**
     * 根据指定的文件 ID 集合查询出文件信息集合
     *
     * @param fileIds 文件ID
     * @return Id为空时返回空集合
     */
    List<FileInfo> query(String... fileIds);

    /**
     * 保存文件实体信息
     *
     * @param fi 文件信息实体
     */
    void save(FileInfo fi);

    /**
     * 保存传进来的数据，如无文件Id，则做新增操作，自动生成Id。
     * 如有文件Id，则做保存操作。
     *
     * @param fis 文件信息实体
     */
    void save(List<FileInfo> fis);

    /**
     * 根据文件Id集合删除文件实体信息
     *
     * @param fileIds 文件ID
     */
    void delete(String... fileIds);

    /**
     * 根据任务 Id 来查询文件信息实体
     *
     * @param taskId 任务 Id
     * @return 文件信息实体集合
     */
    List<FileInfo> queryByTaskId(int taskId);

    /**
     * 如果完成返回 true
     *
     * @param taskId 任务Id
     * @return 完成返回 true，未完成返回 false
     */
    boolean taskFinished(int taskId);
}
