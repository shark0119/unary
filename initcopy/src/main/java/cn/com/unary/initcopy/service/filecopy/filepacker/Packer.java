package cn.com.unary.initcopy.service.filecopy.filepacker;

import cn.com.unary.initcopy.entity.Constants.PackerType;
import cn.com.unary.initcopy.exception.InfoPersistenceException;
import cn.com.unary.initcopy.grpc.entity.SyncProcess;

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
public interface Packer {
    /**
     * 开始文件读取打包，并向目标端发送数据包
     * 会读取任务的进度继续开始。
     *
     * @param taskId   任务Id
     * @throws IOException              IO 异常
     * @throws InfoPersistenceException 相关信息在持久化层发生异常
     */
    void init(String taskId) throws IOException, InfoPersistenceException;

    /**
     * 开始文件读取打包，并向目标端发送数据包
     * 会读取任务的进度继续开始。
     *
     * @param taskId   任务Id
     * @param process 复制进度
     * @throws IOException              IO 异常
     * @throws InfoPersistenceException 相关信息在持久化层发生异常
     */
    void init(String taskId, SyncProcess process) throws IOException, InfoPersistenceException;

    /**
     * 获取数据包，如被关闭或打包至文件尾，则返回 null
     *
     * @return 数据包
     * @throws IOException              IO异常
     * @throws InfoPersistenceException 持久层异常
     */
    byte[] pack() throws IOException, InfoPersistenceException;
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
     * @return 当前任务的同步进度
     * @throws IOException 关闭失败则抛出 IO 异常
     */
    SyncProcess close() throws IOException;
}
