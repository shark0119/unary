package cn.com.unary.initcopy.mock;

import java.io.IOException;

public class TransferClientTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        TransferClient client = new TransferClient();
        client.startClient("127.0.0.1", 23456);
        client.sendData("f word in you dream".getBytes());
        client.stopClient();
    }
}