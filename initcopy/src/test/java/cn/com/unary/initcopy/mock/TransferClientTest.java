package cn.com.unary.initcopy.mock;

import com.una.api.TransmitUtil;

public class TransferClientTest {

    public static void main(String[] args) {
        /*TransferClient client = new TransferClient();
        client.startClient("127.0.0.1", 23456);
        client.sendData("f word in you dream".getBytes());
        client.stopClient();*/
        TransmitUtil.start();
        TransmitUtil.connect("127.0.0.1", 20100);
        TransmitUtil.sendData("hello server".getBytes());
    }
}