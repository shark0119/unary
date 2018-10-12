package cn.com.unary.initcopy.service.filecopy.io;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 向文件写入数据，模式为写入 {@link StandardOpenOption#WRITE}
 * 内部以 {@link FileChannel} 实现
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Component("JavaNioFileOutput")
@Scope("prototype")
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
        logger.debug("Write " + size + " bytes to " + currentFileName);
        return size;
    }

    @Override
    public AbstractFileOutput truncate(int size) throws IOException {
        currentFileChannel.truncate(size);
        return this;
    }

    @Override
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

    /**
     * 打开某文件，如不存在，则先创建，只接受文本和二进制文件
     *
     * @param fileName 文件名
     * @return 当前对象
     * @throws IOException 发生IO异常
     */
    @Override
    public AbstractFileOutput openFile(String fileName) throws IOException {
        close();
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
        Path path = Paths.get(fileName);
        currentFileChannel = FileChannel.open(
                path,
                StandardOpenOption.WRITE);
        return this;
    }
}
