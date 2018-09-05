package cn.com.unary.initcopy.mock;

import org.junit.Test;

import java.io.IOException;

public class TransferClientTest {

    @Test
    public void startClient() throws IOException {
        TransferClient client = new TransferClient();
        client.startClient("127.0.0.1", 23456);
        client.sendData("f word in you dream".getBytes());
    }
}