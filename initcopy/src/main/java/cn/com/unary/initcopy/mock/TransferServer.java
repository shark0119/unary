package cn.com.unary.initcopy.mock;

import api.UnaryProcess;
import api.UnaryTServer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component("TransferServer")
@Scope("singleton")
public class TransferServer implements UnaryTServer {
    private UnaryProcess process;
    @Override
    public void startServer() throws IOException {

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
