package cn.com.unary.initcopy.filecopy.filepacker;

import api.UnaryTClient;
import cn.com.unary.initcopy.entity.Constants.PackType;

import java.io.IOException;
import java.util.List;

/**
 * 全复制文件打包
 * 非线程安全
 *
 * <p>
 * 包格式
 * **********************************
 * *	当前任务Id()					*
 * *	数据包序号(4字节)  			*
 * **********************************
 * *	解析器种类标识(一个字节)		*
 * **********************************
 * *	文件数据包		 			*
 * **********************************
 * *								*
 * *	文件数据包					*
 * *								*
 * **********************************
 * *								*
 * *								*
 * *	文件数据包					*
 * *								*
 * *								*
 * **********************************
 *
 * @author shark
 */
public interface Packer extends AutoCloseable {
    /**
     * 开始文件读取打包，并向目标端发送数据包
     *
     * @param fileIds 文件的UUID
     */
    void start(List<String> fileIds) throws IOException;

    /**
     * 将某个暂停的任务恢复过来，继续同步
     *
     * @param taskId 任务Id
     * @throws Exception 无法从暂停状态中将其恢复
     */
    void restore (int taskId) throws Exception;
    /**
     * 返回打包种类
     *
     * @return 打包策略
     */
    PackType getPackType();

    /**
     * 暂停当前打包进程
     */
    void pause();

    Packer setTransfer(UnaryTClient unaryTClient);

    Packer setTaskId(int taskId);
}
