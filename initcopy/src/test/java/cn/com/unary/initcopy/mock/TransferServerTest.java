package cn.com.unary.initcopy.mock;


import cn.com.unary.initcopy.service.transmit.DataHandlerAdapter;
import cn.com.unary.initcopy.service.transmit.TransmitServerAdapter;

import java.io.IOException;

public class TransferServerTest {

    public static void main(String[] args) throws IOException {
        Thread.currentThread().setName("main thread");
        TransmitServerAdapter server = new TransmitServerAdapter();
        server.start(10234, new DataHandlerAdapter() {
            @Override
            public void handle(byte[] data) {
                System.out.println("task");
            }
        });
        System.out.println("donedone");
    }
}