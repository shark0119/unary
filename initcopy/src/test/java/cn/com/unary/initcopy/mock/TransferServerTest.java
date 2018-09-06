package cn.com.unary.initcopy.mock;

import java.io.IOException;

public class TransferServerTest {

    public static void main(String[] args) throws IOException {
        TransferServer server = new TransferServer();
        server.startServer(23456);
    }
}