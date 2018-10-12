package cn.com.unary.initcopy.mock;

import api.UnaryHandler;
import api.UnaryProcess;
import api.UnaryTransferServer;
import cn.com.unary.initcopy.common.AbstractLoggable;
import cn.com.unary.initcopy.common.utils.CommonUtils;
import cn.com.unary.initcopy.exception.TaskFailException;
import cn.com.unary.initcopy.service.filecopy.ServerFileCopy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import transmit.server.UnaryTServer;

import java.io.IOException;

/**
 * 打桩使用的传输模块服务端
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Component("TransferServer")
@Scope("singleton")
public class TransferServer extends AbstractLoggable implements UnaryTransferServer {
    @Autowired
    private ServerFileCopy fileCopy;
    private UnaryTServer server;
    @Autowired
    private UnaryProcess process;
    @Override
    public void startServer(int port) throws IOException {
        server = new UnaryTServer(port);
        try {
            server.setDataprocess(new UnaryHandlerImpl());
            server.startServer();
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public void stopServer() throws IOException {
        try {
            server.stopServer();
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public void setProcess(UnaryProcess process) {
        this.process = process;
    }

    @Override
    public UnaryProcess getProcess() {
        return this.process;
    }

    private class UnaryHandlerImpl implements UnaryHandler {
        @Override
        public void handler(byte[] data) {
            // TODO 调用解包程序，写入文件
            try {
                logger.info(String.format("transfer server got pack %d.", CommonUtils.byteArrayToInt(data, 4)));
                fileCopy.resolverPack(data);
            } catch (TaskFailException e) {
                e.printStackTrace();
            }
        }
    }
}
