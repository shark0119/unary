package cn.com.unary.initcopy.mock;

import api.UnaryChannel;
import api.UnaryProcess;
import api.UnaryTransferClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import transmit.client.UnaryTClient;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 打桩使用的类
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Component("TransferClient")
@Scope("prototype")
public class TransferClient extends UnaryTransferClient {

    private TransferServer server;
    @Autowired
    private UnaryChannel channel;
    private UnaryTClient client;
    private CountDownLatch latch;
    public TransferClient () {
        latch = new CountDownLatch(1);
    }

    @Override
    public void setSpeedLimit(int limit) {
    }

    @Override
    public void setEncryptType(Object type) {
    }

    @Override
    public void setCompressType(Object type) {

    }

    @Override
    public void setProcess(UnaryProcess process) {
    }

    @Override
    public int sendData(byte[] data, int time) {
        // process 会设置 channel 里的 handler
/*        UnaryProcess process = server.getProcess();
        process.process(channel);
        channel.writeData(data);*/
        return client.sendMessage(data);
    }

    @Override
    public int sendData(byte[] data) {
        return this.sendData(data, 1);
    }

    @Override
    public void startClient(String ip, int port) throws IOException {
        client = new UnaryTClient(ip, port, "10.10.1.125", "1-1-1-1-1-1-1", latch);
        try {
            client.startClient();
            this.latch.await();
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public boolean isReady () {
        return client.isReady();
    }
    @Override
    public void stopClient() throws IOException {
        try {
            client.stopClient();
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}
