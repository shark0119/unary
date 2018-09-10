package cn.com.unary.initcopy.mock;

import com.una.api.Conmunication;
import com.una.api.TransmitListener;
import com.una.api.TransmitUtil;
import com.una.common.TransmitException;
import com.una.model.message.TransmitMessage;
import com.una.model.params.TransmitServerParams;

import java.net.InetSocketAddress;

public class TransferServerTest {

    public static void main(String[] args) throws TransmitException {
        /*TransferServer server = new TransferServer();
        server.startServer(23456);*/
        TransmitUtil.start();
        InetSocketAddress serverParam = new InetSocketAddress(20100);
        TransmitServerParams params = new TransmitServerParams(serverParam);
        params.setOpenSSL(false);
        TransmitUtil.listen(params);
        TransmitUtil.addRecvDataHandler(new TransmitListener() {
            @Override
            public void onMessage(Conmunication conmunication, TransmitMessage msg) {
                System.out.println(new String((byte[])((byte[])msg.getContent())));
            }
        });
    }
}