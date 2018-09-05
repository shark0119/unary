package cn.com.unary.initcopy.mock;

import org.junit.Test;

import java.io.IOException;

public class TransferServerTest {

    @Test
    public void startServer() {
    }

    @Test
    public void stopServer() {
    }

    @Test
    public void setProcess() {
    }

    @Test
    public void getProcess() {
    }

    public static void main(String[] args) throws IOException {
        TransferServer server = new TransferServer();
        server.startServer(23456);
    }
}