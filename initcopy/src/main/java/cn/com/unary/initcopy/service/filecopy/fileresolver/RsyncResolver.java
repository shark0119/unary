package cn.com.unary.initcopy.service.filecopy.fileresolver;

import cn.com.unary.initcopy.entity.Constants;
import cn.com.unary.initcopy.grpc.entity.SyncProcess;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 差异复制时使用的文件解析器，标识为
 * PackType.RSYNC_JAVA
 * @author shark
 *
 */
@Component("RsyncResolver")
@Scope("prototype")
public class RsyncResolver implements Resolver{

	@Override
	public SyncProcess pause() throws IOException {
		return null;
	}

	@Override
	public void resume() throws IOException {

	}

	@Override
	public void process(byte[] data) {

	}

	@Override
	public Constants.PackerType getPackType() {
		return Constants.PackerType.RSYNC_JAVA;
	}

	@Override
	public void init(String taskId, String backupPath) {

	}

	@Override
	public void close() throws IOException {

	}
}
