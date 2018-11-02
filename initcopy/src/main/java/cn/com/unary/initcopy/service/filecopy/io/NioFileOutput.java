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
import java.util.Objects;

/**
 * 向文件写入数据，模式为写入 {@link StandardOpenOption#WRITE}
 * 内部以 {@link FileChannel} 实现
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Component("NioFileOutput")
@Scope("prototype")
public class NioFileOutput extends AbstractLoggable implements Closeable {

    private String currentFileName;
    private FileChannel currentFileChannel;

    /**
     * 向文件中写入指定的字节数组
     *
     * @param data 待写入的数据
     * @return 返回实际写入的字节个数
     * @throws IOException 文件写数据错误
     */
    public int write(byte[] data) throws IOException {
        return this.write(data, 0, data.length);
    }

    /**
     * 向文件中写入指定的字节数组
     *
     * @param data   待写入的数据
     * @param offset 字节数组偏移量
     * @param length 写入的数据长度
     * @return 返回实际写入的字节个数
     * @throws IOException 文件写数据错误
     */
    public int write(byte[] data, int offset, int length) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(data, offset, length);
        int size = currentFileChannel.write(buffer);
        currentFileChannel.force(true);
        logger.debug("Write " + size + " bytes to " + currentFileName);
        return size;
    }

    /**
     * Truncates this channel's file to the given size.
     *
     * @param size @see FileChannel#truncate(long)
     * @throws IOException IO 异常
     * @see FileChannel#truncate(long)
     */
    public void truncate(int size) throws IOException {
        currentFileChannel.truncate(size);
    }

    /**
     * Sets this channel's file position.
     *
     * @param position @see FileChannel#position(long)
     * @throws IOException IO异常
     * @see FileChannel#position(long)
     */
    public void position(long position) throws IOException {
        currentFileChannel.position(position);
    }

    @Override
    public void close() throws IOException {
        if (currentFileChannel != null) {
            currentFileChannel.close();
        }
    }

    /**
     * 打开某文件，如不存在，则先创建，只接受文本和二进制文件
     *
     * @param fileName 文件名
     * @throws IOException 发生IO异常
     */
    public void openFile(String fileName) throws IOException {
        this.openFile(fileName, StandardOpenOption.WRITE);
    }

    public void openFile(String fileName, OpenOption openOption) throws IOException {
        Objects.requireNonNull(fileName);
        Objects.requireNonNull(openOption);
        this.close();
        logger.debug("Change File to " + fileName);
        this.currentFileName = fileName;
        // 递归创建目录
        File file = new File(fileName.substring(0, fileName.lastIndexOf("/")));
        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new IOException("create dir fail");
            }
        }
        file = new File(fileName);
        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new IOException("create file fail");
            }
        }
        currentFileChannel = FileChannel.open(Paths.get(fileName), openOption);
    }
}
