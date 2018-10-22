package cn.com.unary.initcopy.adapter;

import cn.com.unary.initcopy.common.AbstractLoggable;
import com.una.api.AcceptHandler;
import com.una.api.Conmunication;
import com.una.api.DataHandler;
import com.una.common.TransmitException;
import com.una.impl.TransmitServer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.io.IOException;

/**
 * 传输模块的服务端适配器
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Component("TransmitServerAdapter")
@Scope("singleton")
public class TransmitServerAdapter extends AbstractLoggable implements Closeable {
    private TransmitServer server;
    private Boolean isActive = Boolean.FALSE;

    public void start(int port, final DataHandlerAdapter handler) throws IOException {
        if (isActive) {
            return;
        }
        try {
            synchronized (isActive) {
                if (isActive) {
                    return;
                }
                server = new TransmitServer();
                server.startServer(port);
                server.setAcceptHandler(new AcceptHandler() {
                    @Override
                    public void accept(Conmunication conmunication) {
                        if (conmunication.getRecvDataHandler() == null) {
                            conmunication.setRecvDataHandler(new DataHandler() {
                                @Override
                                public void handler(Conmunication conmunication, byte[] bytes) {
                                    handler.handle(bytes);
                                }
                            });
                        }
                    }
                });
                isActive = true;
            }
        } catch (TransmitException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void close() throws IOException {
        server.stopServer();
    }
}
