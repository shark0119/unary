package transmit;

import api.UnaryHandler;
import transmit.server.UnaryTServer;

import java.util.Arrays;

public class SDataTest {

    private static UnaryTServer server;

    public static void main(String[] args) throws Exception {
        byte[] data = new byte[250];
        String ip = "10.10.1.125";

        Arrays.fill(data, (byte) 0xF);

        serverThread thread = new SDataTest().new serverThread();
        thread.start();

        Thread.sleep(10000);

        System.out.println("2222222222222222222");

        for (byte i = 0; i < 100; i++) {
            data[0] = i;
            if (server == null)
                continue;
            server.sendMessage(data, ip);
        }

    }

    class serverThread extends Thread {

        @Override
        public void run() {
            server = new UnaryTServer(8002);
            server.setDataprocess(new UnaryHandler() {
                @Override
                public void handler(byte[] data) {
                    System.out.println(new java.lang.String(data));
                }
            });
            try {
                server.startServer();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
}
