package cn.com.unary.initcopy.filecopy.io;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import cn.com.unary.initcopy.exception.UnaryIOException;
import cn.com.unary.initcopy.utils.AbstractLogable;

public abstract class AbstractFileInput extends AbstractLogable implements AutoCloseable {

	private String currentFileName;
	protected FileChannel currentFileChannel ;
	// 判断文件是否读取完毕
	protected boolean finish = false;
	
	public void setFile(String fileName) {
		if (!finish) {
			try {
				currentFileChannel.close();
			} catch (IOException e) {
				throw new UnaryIOException("ERROR CODE 0X06: Resource close exception.", e);
			}
			logger.warn("File {1} was discarded after it was not read.", currentFileName);
		}
		try {
			currentFileChannel = FileChannel.open(
					Paths.get(fileName),
					StandardOpenOption.READ);
			currentFileName = fileName;
			finish = false;
		} catch (IOException e) {
			throw new UnaryIOException("ERROR CODE 0X01: file open error.", e);
		}
	}

	/**
	 * 读取文件，返回能读取到的数据，如果读到文件尾，返回长度为0的字节数组
	 * 为了兼容 NIO 中的零拷贝方式
	 * @param size 期望读取到的个数
	 * @return 返回读到的字节数组
	 */
	public abstract byte[] read(int size);

	@Override
	public void close() throws IOException {
		if (currentFileChannel != null) {
			currentFileChannel.close();
		}
	}

	protected void finished () {
		finish = true;
	}
}
