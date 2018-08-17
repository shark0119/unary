package cn.com.unary.initcopy.mock;

public interface UnaryTransferClient {
	void setProcess (UnaryProcess process);
	void start (String ip, int port);
	void sendData (byte[] data);
	int getMaxPackSize ();
}
