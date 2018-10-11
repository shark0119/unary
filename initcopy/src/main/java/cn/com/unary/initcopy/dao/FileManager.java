package cn.com.unary.initcopy.dao;

import cn.com.unary.initcopy.entity.FileInfoDO;
import cn.com.unary.initcopy.exception.InfoPersistenceException;
import cn.com.unary.initcopy.grpc.entity.SyncTask;

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
     * @throws InfoPersistenceException 持久层异常
     */
    List<FileInfoDO> queryByIds(String... fileIds) throws InfoPersistenceException;

    /**
     * 保存文件实体信息
     *
     * @param fi 文件信息实体
     * @return 文件信息实体
     * @throws InfoPersistenceException 持久层异常
     */
    FileInfoDO save(FileInfoDO fi) throws InfoPersistenceException;

    /**
     * 保存传进来的数据，如无文件Id，则做新增操作，自动生成Id。
     * 如有文件Id，则做保存操作。
     *
     * @param fis 文件信息实体
     * @throws InfoPersistenceException 持久层异常
     */
    void save(List<FileInfoDO> fis) throws InfoPersistenceException;

    /**
     * 根据文件Id集合删除文件实体信息
     *
     * @param fileIds 文件ID
     * @throws InfoPersistenceException 持久层异常
     */
    void deleteByIds(String... fileIds) throws InfoPersistenceException;

    /**
     * 根据任务 Id 来查询文件信息实体
     *
     * @param taskId 任务 Id
     * @return 文件信息实体集合
     */
    List<FileInfoDO> queryByTaskId(String taskId);

    /**
     * 查询某个任务中未同步的文件信息集合
     *
     * @param taskId 任务Id
     * @return 实体集合
     */
    List<FileInfoDO> queryUnSyncFileByTaskId(String taskId);

    /**
     * 如果完成返回 true
     *
     * @param taskId 任务Id
     * @return 完成返回 true，未完成返回 false
     */
    boolean taskFinished(String taskId);

    /**
     * 通过任务 Id 查询任务信息
     *
     * @param taskId 任务Id
     * @return 同步任务的相关信息
     */
    SyncTask queryTask(String taskId);

    /**
     * 根据任务Id 删除该任务的相关配置信息，及
     *
     * @param taskId 任务Id
     */
    void deleteTask(String taskId);

    /**
     * 保存同步任务的相关信息
     *
     * @param task 同步任务的相关信息
     * @return 返回持久化后的对象
     */
    SyncTask saveTask(SyncTask task);
}
