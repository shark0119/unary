package cn.com.unary.initcopy.filecopy.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 向文件写入数据，模式为写入 {@link StandardOpenOption#WRITE}
 * 内部以 {@link FileChannel} 实现
 *
 * @author Shark.Yin
 * @since 1.0
 */
public class JavaNioFileOutput extends AbstractFileOutput {

    protected String currentFileName;
    protected FileChannel currentFileChannel;

    @Override
    public int write(byte[] data) throws IOException {
        return this.write(data, 0, data.length);
    }

    @Override
    public int write(byte[] data, int offset, int length) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(data, offset, length);
        int size = currentFileChannel.write(buffer);
        currentFileChannel.force(true);
        logger.debug("CurrentTask "+Thread.currentThread().getName()+
                    ". Write "+size+" bytes to "+currentFileName);
        return size;
    }

    /**
     * @see FileChannel#truncate(long)
     */
    public AbstractFileOutput truncate(int size) throws IOException {
        currentFileChannel.truncate(size);
        return this;
    }

    /**
     * @see FileChannel#position(long)
     */
    public AbstractFileOutput position(long position) throws IOException {
        currentFileChannel.position(position);
        return this;
    }

    @Override
    public void close() throws IOException {
        if (currentFileChannel != null) {
            currentFileChannel.close();
        }
    }

    @Override
    public AbstractFileOutput openFile(String fileName) throws IOException {
        close();
        logger.debug("CurrentTask "+Thread.currentThread().getName()
                        +". Change File to " + fileName);
        this.currentFileName = fileName;
        currentFileChannel = FileChannel.open(
                Paths.get(fileName),
                StandardOpenOption.WRITE);
        return this;
    }
}
