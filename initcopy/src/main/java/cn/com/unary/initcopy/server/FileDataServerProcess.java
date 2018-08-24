package cn.com.unary.initcopy.server;

import api.UnaryChannel;
import api.UnaryHandler;
import api.UnaryProcess;
import cn.com.unary.initcopy.filecopy.ServerFileCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 目标端处理文件数据包
 * 线程安全
 *
 * @author shark
 *
 */
@Component("FileDataServerProcess")
@Scope("singleton")
public class FileDataServerProcess implements UnaryProcess{

    @Autowired
    private ServerFileCopy fileCopy;
	@Override
	public void process(UnaryChannel channel) {
		channel.setHandler(new UnaryHandlerImpl());
	}

	private class UnaryHandlerImpl implements UnaryHandler {
        @Override
        public void handler(byte[] data) {
            // TODO 调用解包程序，写入文件
            fileCopy.resolverPack(data);
        }
    }
}
