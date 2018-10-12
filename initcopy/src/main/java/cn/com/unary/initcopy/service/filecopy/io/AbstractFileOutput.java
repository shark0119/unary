package cn.com.unary.initcopy.service.filecopy.io;

import cn.com.unary.initcopy.common.AbstractLoggable;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * 向文件写入数据
 *
 * @author Shark.Yin
 * @since 1.0
 */
public abstract class AbstractFileOutput extends AbstractLoggable implements Closeable {

    /**
     * 设置当前写的文件
     * 会自动关闭上一个开启写入的文件
     *
     * @param fileName 文件名
     * @return 当前对象
     * @throws IOException IO 异常
     */
    public abstract AbstractFileOutput openFile(String fileName) throws IOException;

    /**
     * 向文件中写入指定的字节数组
     *
     * @param data 待写入的数据
     * @return 返回实际写入的字节个数
     * @throws IOException 文件写数据错误
     */
    public abstract int write(byte[] data) throws IOException;// 写入文件，返回写入的字节数

    /**
     * 向文件中写入指定的字节数组
     *
     * @param data   待写入的数据
     * @param offset 字节数组偏移量
     * @param length 写入的数据长度
     * @return 返回实际写入的字节个数
     * @throws IOException 文件写数据错误
     */
    public abstract int write(byte[] data, int offset, int length) throws IOException;

    /**
     * Truncates this channel's file to the given size.
     *
     * @param size @see FileChannel#truncate(long)
     * @return 当前对象
     * @throws IOException IO 异常
     * @see FileChannel#truncate(long)
     */

    public abstract AbstractFileOutput truncate(int size) throws IOException;

    /**
     * Sets this channel's file position.
     *
     * @param position @see FileChannel#position(long)
     * @return 当前对象
     * @throws IOException IO异常
     * @see FileChannel#position(long)
     */
    public abstract AbstractFileOutput position(long position) throws IOException;
}
