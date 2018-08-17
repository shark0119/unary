package cn.com.unary.initcopy.filecopy.io;

import java.io.IOException;
import java.nio.ByteBuffer;

import cn.com.unary.initcopy.exception.UnaryIOException;

public class JavaNioFileOutput extends AbstractFileOutput{

	@Override
	int write(byte[] data) {
		ByteBuffer buffer = ByteBuffer.wrap(data); 
		try {
			currentFileChannel.write(buffer);
		} catch (IOException e) {
			// TODO logger exception
			throw new UnaryIOException("ERROR CODE 0X01: file write error.", e);
		}
		return 0;
	}

}
