package cn.com.unary.initcopy.filecopy.fileresolver;

import cn.com.unary.initcopy.entity.Constants;

import java.io.IOException;

/**
 * 差异复制时使用的文件解析器，标识为
 * PackType.RSYNC_JAVA
 * @author shark
 *
 */
public class RsyncResolver implements Resolver{

	@Override
	public void process(byte[] data) {
		
	}

	@Override
	public Constants.PackType getPackType() {
		return null;
	}

	@Override
	public void close() throws IOException {

	}
}
