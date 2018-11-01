package cn.com.unary.initcopy.service.filecopy.fileresolver;

import cn.com.unary.initcopy.entity.Constants.PackerType;
import cn.com.unary.initcopy.exception.InfoPersistenceException;
import cn.com.unary.initcopy.grpc.entity.SyncProcess;

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
     * @throws IOException              IO error
     * @throws InfoPersistenceException 持久层异常
     */
    void process(byte[] data) throws IOException, InfoPersistenceException;

    /**
     * 获取此解析器对应的打包策略
     *
     * @return 此解析器对应的打包策略
     */
    PackerType getPackType();

    /**
     * 支持从上次解析的包继续解析的方法
     *
     * @return 同步进度信息
     * @throws IOException 关闭相关资源失败
     */
    SyncProcess pause() throws IOException;

    /**
     * 初始化相关参数
     *
     * @param taskId     当前解析器对应的任务 ID
     * @param backupPath 解析器对应的备份路径
     */
    void init(String taskId, String backupPath);
}
