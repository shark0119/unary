package cn.com.unary.initcopy.filecopy.io;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Java 文件读取写入类，采用NIO 在支持的环境下能通过零拷贝来提升效率
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Component("JavaNioFileInput")
@Scope("prototype")
public class JavaNioFileInput extends AbstractFileInput {

    private FileChannel currentFileChannel;
    private String currentFileName;

    @Override
    public boolean read(ByteBuffer buffer) throws IOException {
        int size = currentFileChannel.read(buffer);
        logger.debug("Got " + size + " byte from file " + currentFileName);
        return currentFileChannel.size() > currentFileChannel.position();
    }

    @Override
    public AbstractFileInput position(long position) throws IOException {
        currentFileChannel.position(position);
        return this;
    }

    @Override
    public void openFile(String fileName) throws IOException {
        this.close();
        logger.debug("File change to " + fileName + ".");
        logger.debug("file size:" + new File(fileName).length());
        currentFileChannel = FileChannel.open(
                Paths.get(fileName),
                StandardOpenOption.READ);
        currentFileName = fileName;
        logger.debug("Open new file " + currentFileName);
    }

    @Override
    public void close() throws IOException {
        if (currentFileChannel != null) {
            currentFileChannel.close();
        }
    }
}
