package cn.com.unary.initcopy.service.filecopy.io;

import cn.com.unary.initcopy.common.AbstractLoggable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Java 文件读取写入类，采用NIO 在支持的环境下能通过零拷贝来提升效率
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Component("NioFileInput")
@Scope("prototype")
public class NioFileInput extends AbstractLoggable implements Closeable {

    private FileChannel currentFileChannel;
    private String currentFileName;

    /**
     * 从文件中读取数据
     *
     * @param buffer 文件数据容器，默认读取 {@link ByteBuffer#remaining()}个字节
     * @return 读取到文件尾返回 false，否则返回 true
     * @throws IOException 文件读取发生异常
     */
    public boolean read(ByteBuffer buffer) throws IOException {
        int size = currentFileChannel.read(buffer);
        logger.debug("Got " + size + " byte from file " + currentFileName);
        return currentFileChannel.size() > currentFileChannel.position();
    }

    /**
     * Sets this channel's file position.
     *
     * @param position 位置
     * @throws IOException IO异常
     * @see FileChannel#position(long)
     */
    public void position(long position) throws IOException {
        currentFileChannel.position(position);
    }

    public long position() throws IOException {
        return currentFileChannel.position();
    }

    /**
     * 打开文件，如果上个文件没有读完，关闭资源并做日志记录。
     *
     * @param fileName 要打开的文件名
     * @throws IOException 发生IO错误
     */
    public void openFile(String fileName) throws IOException {
        this.openFile(fileName, StandardOpenOption.READ);
    }

    public void openFile(String fileName, OpenOption openOption) throws IOException {
        this.close();
        logger.debug(String.format("File change to %s.", fileName));
        logger.debug("file size:" + new File(fileName).length());
        currentFileChannel = FileChannel.open(Paths.get(fileName), openOption);
        currentFileName = fileName;
        logger.debug(String.format("File:%s open with mode:%s.", fileName, openOption.toString()));
    }

    @Override
    public void close() throws IOException {
        if (currentFileChannel != null) {
            currentFileChannel.close();
        }
    }
}
