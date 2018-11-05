package cn.com.unary.initcopy.mock;


import cn.com.unary.initcopy.entity.TransmitParams;
import cn.com.unary.initcopy.service.transmit.TransmitClientAdapter;

import java.io.IOException;

public class TransferClientTest {

    public static void main(String[] args) throws IOException {
        TransmitClientAdapter client = new TransmitClientAdapter();
        TransmitParams params = new TransmitParams();
        params.setIp("localhost");
        params.setPort(10234);
        client.start(params);
        client.close();
    }
}