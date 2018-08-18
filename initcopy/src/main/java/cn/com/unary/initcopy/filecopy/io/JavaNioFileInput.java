package cn.com.unary.initcopy.filecopy.io;

import java.io.IOException;
import java.nio.ByteBuffer;

import cn.com.unary.initcopy.exception.UnaryIOException;

/**
 * Java 文件读取写入类，采用NIO 在支持的环境下能通过零拷贝来提升效率
 * @author shark
 *
 */
public class JavaNioFileInput extends AbstractFileInput {

	@Override
	public byte[] read(int size) {
		ByteBuffer buffer ;
		if (size < 256*1024)
			buffer = ByteBuffer.allocate(size);
		else 
			buffer = ByteBuffer.allocateDirect(size);
		try {
			currentFileChannel.read(buffer);
			if (buffer.hasRemaining()) 
				this.finished();
		} catch (IOException e) {
			logger.error("ERROR CODE 0X01: file open error.", e);
			throw new UnaryIOException("ERROR CODE 0X01: file open error.", e);
			// TODO 是否算是重复日志
		}
		return buffer.array();
	}

}
