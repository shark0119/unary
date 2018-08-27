package cn.com.unary.initcopy.filecopy.fileresolver;

import cn.com.unary.initcopy.entity.Constants.PackType;

/**
 * 文件数据包解析器
 *
 * @author shark
 */
public interface Resolver extends AutoCloseable {
    /**
     * 解析文件数据包，如果解析器无法对应，则会抛 IllegalStateException
     *
     * @param data 文件数据包
     * @return 任务是否完成
     */
    boolean process(byte[] data);

    /**
     * 获取此解析器对应的打包策略
     *
     * @return 此解析器对应的打包策略
     */
    PackType getPackType();

    /**
     * 一个任务对应一个解析器，设置该解析器的任务Id作为标识
     *
     * @param taskId 任务Id
     * @return 当前对象
     */
    Resolver setTaskId(int taskId);

    /**
     * 设置文件备份的路径
     *
     * @param backupPath 文件备份的路径
     * @return 当前对象
     */
    SyncAllResolver setBackupPath(String backupPath);
}
