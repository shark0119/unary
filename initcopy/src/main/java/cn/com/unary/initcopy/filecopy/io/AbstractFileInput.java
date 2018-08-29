package cn.com.unary.initcopy.filecopy.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import cn.com.unary.initcopy.common.AbstractLogable;

public abstract class AbstractFileInput extends AbstractLogable implements AutoCloseable {

    /**
     * 打开文件，如果上个文件没有读完，关闭资源并做日志记录。
     *
     * @param fileName 要打开的文件名
     * @return 当前对象
     * @throws IOException 发生IO错误
     */
    public abstract AbstractFileInput openFile (String fileName) throws IOException;

    /**
     * 从文件中读取数据
     *
     * @param buffer 文件数据容器，默认读取 {@link ByteBuffer#remaining()}个字节
     * @return 读取到文件尾返回 false，否则返回 true
     * @throws IOException 文件读取发生异常
     */
	public abstract boolean read (ByteBuffer buffer) throws IOException;

    /**
     * @see FileChannel#position(long)
     */
    public abstract AbstractFileInput position(long position) throws IOException;

    /**
     * 读取文件，返回能读取到的数据，如果读到文件尾，返回长度为0的字节数组
     * 为了兼容 NIO 中的零拷贝方式，取决于 JNI 能否实现异常处理和Java 类映射。
     * @param size 期望读取到的个数
     * @return 返回读到的字节数组
     */
    /*public abstract byte[] read(int size);*/
}
