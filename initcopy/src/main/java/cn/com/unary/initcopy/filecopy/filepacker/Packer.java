package cn.com.unary.initcopy.filecopy.filepacker;

import api.UnaryTransferClient;
import cn.com.unary.initcopy.entity.Constants.PackerType;
import cn.com.unary.initcopy.exception.InfoPersistenceException;

import java.io.Closeable;
import java.io.IOException;

/**
 * 全复制文件打包
 * 非线程安全
 *
 * <p>
 * 包格式
 * **********************************
 * *	当前任务Id(4字节)			*
 * *	数据包序号(4字节)  			*
 * **********************************
 * *	解析器种类标识(一个字节)		*
 * **********************************
 * *	        		 			*
 * *								*
 * *	数据包体    					*
 * *								*
 * *								*
 * *								*
 * *								*
 * *								*
 * **********************************
 *
 * @author shark
 */
public interface Packer extends Closeable {
    /**
     * 开始文件读取打包，并向目标端发送数据包
     * 会读取任务的进度继续开始。粒度目前在文件级。
     * 未传完的文件会重新传输
     *
     * @param taskId   任务Id
     * @param transfer 传输模块客户端
     * @throws IOException              IO 异常
     * @throws InfoPersistenceException 相关信息在持久化层发生异常
     */
    void start(Integer taskId, UnaryTransferClient transfer) throws IOException, InfoPersistenceException, InterruptedException;

    /**
     * 返回打包种类
     *
     * @return 打包策略
     */
    PackerType getPackType();

    /**
     * 不会立刻关闭当前任务。此方法只会将关闭标志位置为 {@code true}
     * 待打包进程觉得合适时，保存当前打包进度，并停止打包。
     *
     * @throws IOException 关闭失败则抛出 IO 异常
     */
    @Override
    void close() throws IOException;
}
