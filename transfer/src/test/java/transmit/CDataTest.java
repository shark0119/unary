package transmit;

import transmit.client.UnaryTClient;

import java.util.Arrays;

public class CDataTest {

    private static UnaryTClient client;

    public static void main(String[] args) throws Exception {
        byte[] data = new byte[1024 * 1024];
        Arrays.fill(data, (byte) 0xF);
        clientThread thread = new CDataTest().new clientThread();
        thread.start();
        Thread.sleep(5000);
        for (byte i = 0; i < 100; i++) {
            data[0] = i;
            if (client == null)
                continue;
            client.sendMessage(data);
        }
    }

    class clientThread extends Thread {

        @Override
        public void run() {
            client = new UnaryTClient("127.0.0.1", 8002, "10.10.1.125", "1-1-1-1-1-1-1");
            client.setprocess(new PTest());
            try {
                client.startClient();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
