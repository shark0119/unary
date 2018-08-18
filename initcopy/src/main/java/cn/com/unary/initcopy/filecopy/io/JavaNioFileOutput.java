package cn.com.unary.initcopy.filecopy.io;

import java.io.IOException;
import java.nio.ByteBuffer;

import cn.com.unary.initcopy.exception.UnaryIOException;

public class JavaNioFileOutput extends AbstractFileOutput{

	@Override
	public int write(byte[] data) {
		ByteBuffer buffer = ByteBuffer.wrap(data); 
		try {
			currentFileChannel.write(buffer);
		} catch (IOException e) {
			logger.error("ERROR CODE 0X01: file open error.", e);
			throw new UnaryIOException("ERROR CODE 0X01: file open error.", e);
			// TODO 是否算是重复日志
		}
		return 0;
	}

	@Override
	public int write(byte[] data, int offset, int length) {
		return 0;
	}

}
