package cn.com.unary.initcopy.mock;

import api.UnaryProcess;
import api.UnaryTServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 打桩使用的传输模块服务端
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Component("TransferServer")
@Scope("singleton")
public class TransferServer implements UnaryTServer {

    @Autowired
    private UnaryProcess process;
    @Override
    public void startServer(int port) throws IOException {

    }

    @Override
    public void stopServer() throws IOException {

    }

    @Override
    public void setProcess(UnaryProcess process) {
        this.process = process;
    }

    @Override
    public UnaryProcess getProcess() {
        return this.process;
    }

}
