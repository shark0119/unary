package cn.com.unary.initcopy.service.transmitadapter;

import cn.com.unary.initcopy.common.AbstractLoggable;
import cn.com.unary.initcopy.entity.TransmitParams;
import com.una.api.Conmunication;
import com.una.api.DataHandler;
import com.una.common.CompressType;
import com.una.common.EncryptType;
import com.una.common.TransmitException;
import com.una.impl.TransmitClient;
import com.una.model.params.TransmitClientParams;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.io.IOException;

/**
 * 根据由 xqs 实现的传输模块来进行适配
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Component
@Scope("prototype")
public class TransmitClientAdapter extends AbstractLoggable implements Closeable {
    private final Object lock = new Object();
    private TransmitClient client;
    private boolean isActive = Boolean.FALSE;
    private Conmunication communication;

    public void start(TransmitParams params) throws IOException {
        if (isActive) {
            return;
        }
        synchronized (lock) {
            if (isActive) {
                return;
            }
            client = new TransmitClient();
            TransmitClientParams tempParams = new TransmitClientParams(params.getIp(), params.getPort());
            if (params.getCompressType() != null) {
                switch (params.getCompressType()) {
                    case COMPRESS_ZLIB:
                        tempParams.setCompressType(CompressType.ZLIB);
                        break;
                    case COMPRESS_NONE:
                    case UNRECOGNIZED:
                        default:
                            break;
                }
            }
            if (params.getEncryptType() != null) {
                switch (params.getEncryptType()) {
                    case ENCRYPT_AES:
                        tempParams.setEncryptType(EncryptType.AES);
                    case UNRECOGNIZED:
                    case ENCRYPT_NONE:
                        default:
                            break;
                }
            }
            if (params.getSpeedLimit() != null) {
                params.setSpeedLimit(params.getSpeedLimit());
            }
            communication = client.connect(tempParams);
            if (communication == null) {
                throw new IOException("Transfer client connect error.");
            }
            isActive = true;
        }
    }

    public void sendData(byte[] data) throws TransmitException {
        communication.sendData(data);
    }

    public void setDataHandler(final DataHandlerAdapter dataHandlerAdapter) {
        communication.setRecvDataHandler(new DataHandler() {
            @Override
            public void handler(Conmunication conmunication, byte[] data) {
                dataHandlerAdapter.handle(data);
            }
        });
    }

    @Override
    public void close() {
        if (isActive) {
            synchronized (lock) {
                if (!isActive) {
                    return;
                }
                if (communication != null) {
                    communication.close();
                    logger.info("Transfer Closed!");
                }
                isActive = false;
            }
        }
    }
}
