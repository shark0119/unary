package cn.com.unary.initcopy.filecopy.io;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import cn.com.unary.initcopy.exception.UnaryIOException;

/**
 * 向文件写入数据，模式为附加
 * @author shark
 *
 */
public abstract class AbstractFileOutput{

	protected FileChannel currentFileChannel ;
	
	public void setFile(String fileName) {
		try {
			currentFileChannel = FileChannel.open(
					Paths.get(fileName),
					StandardOpenOption.APPEND);
		} catch (IOException e) {
			// TODO logger Exception
			throw new UnaryIOException("ERROR CODE 0X01: file open error.", e);
		}
	}

	/**
	 * 向文件中写入指定的字节数组
	 * @param data 待写入的数据
	 * @return 返回实际写入的字节个数
	 */
	abstract int write(byte[] data);// 写入文件，返回写入的字节数
}
