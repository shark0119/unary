package cn.com.unary.initcopy.filecopy.io;

import cn.com.unary.initcopy.common.AbstractLoggable;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 文件读取类
 *
 * @author Shark.Yin
 * @since 1.0
 */
public abstract class AbstractFileInput extends AbstractLoggable implements AutoCloseable {

    /**
     * 打开文件，如果上个文件没有读完，关闭资源并做日志记录。
     *
     * @param fileName 要打开的文件名
     * @return 当前对象
     * @throws IOException 发生IO错误
     */
    public abstract AbstractFileInput openFile(String fileName) throws IOException;

    /**
     * 从文件中读取数据
     *
     * @param buffer 文件数据容器，默认读取 {@link ByteBuffer#remaining()}个字节
     * @return 读取到文件尾返回 false，否则返回 true
     * @throws IOException 文件读取发生异常
     */
    public abstract boolean read(ByteBuffer buffer) throws IOException;

    /**
     * Sets this channel's file position.
     *
     * @param position 位置
     * @return 当前对象
     * @throws IOException IO异常
     * @see FileChannel#position(long)
     */
    public abstract AbstractFileInput position(long position) throws IOException;

}
