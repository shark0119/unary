package cn.com.unary.initcopy.service.filecopy.filepacker;

import cn.com.unary.initcopy.entity.Constants.PackerType;
import cn.com.unary.initcopy.exception.InfoPersistenceException;
import cn.com.unary.initcopy.grpc.entity.SyncProcess;
import cn.com.unary.initcopy.service.transmit.TransmitClientAdapter;

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
     * 会读取任务的进度继续开始。
     *
     * @param taskId   任务Id
     * @param transfer 传输模块客户端
     * @throws IOException              IO 异常
     * @throws InfoPersistenceException 相关信息在持久化层发生异常
     */
    void start(String taskId, TransmitClientAdapter transfer) throws IOException, InfoPersistenceException;

    /**
     * 返回打包种类
     *
     * @return 打包策略
     */
    PackerType getPackType();

    /**
     * 不会立刻关闭当前任务。此方法只会将关闭标志位置为 true
     * 待时机合适时，保存当前打包进度，并停止打包。
     *
     * @throws IOException 关闭失败则抛出 IO 异常
     */
    @Override
    void close() throws IOException;

    /**
     * 用于暂停后重新打包，从 SERVER 的进度开始。
     *
     * @param serverSyncProcess SERVER Resolver 的进度
     */
    void setServerSyncProcess(SyncProcess serverSyncProcess);
}
