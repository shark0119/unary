package cn.com.unary.initcopy.filecopy.filepacker;

import api.UnaryTClient;
import cn.com.unary.initcopy.entity.Constants.PackType;

import java.io.IOException;
import java.util.List;

/**
 * 全复制文件打包
 * 非线程安全
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
     * 返回打包种类
     *
     * @return 打包策略
     */
    PackType getPackType();

    /**
     * 暂停当前打包进程
     */
    Packer pause ();

    Packer setTransfer (UnaryTClient unaryTClient);
}
