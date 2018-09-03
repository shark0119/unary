package api;

import java.io.IOException;

public abstract class UnaryTClient {
    public abstract void setSpeedLimit(int limit);

    public abstract void encrypt(String type);

    public abstract void compress(String type);
	public abstract void setProcess (UnaryProcess process);
	public abstract int sendData(byte[] data, int time);
	public abstract int sendData(byte[] data);
	public abstract void startClient() throws IOException;
	public abstract void stopClient() throws IOException;
	public static int MAX_PACK_SIZE = 8*1024*1024;
}
