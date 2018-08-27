package cn.com.unary.initcopy.filecopy.fileresolver;

import cn.com.unary.initcopy.entity.Constants;
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
	public boolean process(byte[] data) {
		return false;
	}

	@Override
	public Constants.PackType getPackType() {
		return Constants.PackType.RSYNC_JAVA;
	}

	@Override
	public Resolver setTaskId(int taskId) {
		return this;
	}

	@Override
	public SyncAllResolver setBackupPath(String backupPath) {
		return null;
	}

	@Override
	public void close() throws IOException {

	}
}
