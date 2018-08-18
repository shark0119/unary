package cn.com.unary.initcopy.filecopy.io;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import cn.com.unary.initcopy.exception.UnaryIOException;
import cn.com.unary.initcopy.utils.AbstractLogable;

/**
 * 向文件写入数据，模式为附加
 * @author shark
 *
 */
public abstract class AbstractFileOutput extends AbstractLogable implements AutoCloseable {

	protected FileChannel currentFileChannel ;
	
	public void setFile(String fileName) {
		try {
			currentFileChannel = FileChannel.open(
					Paths.get(fileName),
					StandardOpenOption.APPEND);
		} catch (IOException e) {
			throw new UnaryIOException("ERROR CODE 0X01: file open error.", e);
		}
	}

	@Override
	public void close () throws IOException{
	    if (currentFileChannel != null) {
            currentFileChannel.close();
        }
    }

	/**
	 * 向文件中写入指定的字节数组
	 * @param data 待写入的数据
	 * @return 返回实际写入的字节个数
	 */
	public abstract int write(byte[] data);// 写入文件，返回写入的字节数

	/**
	 * 向文件中写入指定的字节数组
	 * @param data 待写入的数据
	 * @param offset 偏移量
	 * @param length 长度
	 * @return 返回实际写入的字节个数
	 */
	public abstract int write(byte[] data, int offset, int length);

}
