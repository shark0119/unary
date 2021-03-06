package api;

import java.io.IOException;

public abstract class UnaryTransferClient {
    public static int MAX_PACK_SIZE = 1024 * 1024;

    public abstract void setSpeedLimit(int limit);

    public abstract void setEncryptType(Object type);

    public abstract void setCompressType(Object type);

    public abstract void setProcess(UnaryProcess process);

    public abstract int sendData(byte[] data, int time);

    public abstract int sendData(byte[] data);

    public abstract void startClient(String serverIp, int port) throws IOException;

    public abstract void stopClient() throws IOException;
}
