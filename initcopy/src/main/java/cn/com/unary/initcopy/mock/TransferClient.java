package cn.com.unary.initcopy.mock;

import api.UnaryChannel;
import api.UnaryProcess;
import api.UnaryTClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component("TransferClient")
@Scope("prototype")
public class TransferClient extends UnaryTClient {

    @Autowired
    private TransferServer server;
    @Autowired
    private UnaryChannel channel;

    @Override
    public void setSpeedLimit(int limit) {

    }

    @Override
    public void encrypt(String type) {

    }

    @Override
    public void compress(String type) {

    }

    @Override
    public void setProcess(UnaryProcess process) {

    }

    @Override
    public int sendData(byte[] data, int time) {
        UnaryProcess process = server.getProcess();
        // process 会设置 channel 里的 handler
        process.process(channel);
        channel.writeData(data);
        return data.length;
    }

    @Override
    public int sendData(byte[] data) {
        return sendData(data, 10);
    }

    @Override
    public void startClient() throws IOException {

    }

    @Override
    public void stopClient() throws IOException {

    }
}
