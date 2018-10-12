package cn.com.unary.initcopy.service.filecopy.fileresolver;

import cn.com.unary.initcopy.entity.Constants.PackerType;
import cn.com.unary.initcopy.exception.InfoPersistenceException;

import java.io.Closeable;
import java.io.IOException;

/**
 * 文件数据包解析器
 *
 * @author shark
 */
public interface Resolver extends Closeable {
    /**
     * 解析文件数据包，如果解析器无法对应，则会抛 IllegalStateException
     *
     * @param data 文件数据包
     * @return 任务是否完成，完成返回 true
     * @throws IOException IO error
     * @throws InfoPersistenceException 持久层异常
     */
    boolean process(byte[] data) throws IOException, InfoPersistenceException;

    /**
     * 获取此解析器对应的打包策略
     *
     * @return 此解析器对应的打包策略
     */
    PackerType getPackType();

    /**
     * 一个任务对应一个解析器，设置该解析器的任务Id作为标识
     *
     * @param taskId 任务Id
     * @return 当前对象
     */
    Resolver setTaskId(String taskId);

    /**
     * 设置目标端备份的路径。当备份时有多个目标路径，则不用设置。
     *
     * @param backUpPath 备份路径
     */
    Resolver setBackUpPath(String backUpPath);
}
