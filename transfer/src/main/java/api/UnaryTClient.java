package api;

import java.io.IOException;

public interface UnaryTClient {
	void setProcess (UnaryProcess process);
	int sendData (byte[] data, int time);
	int sendData (byte[] data);
	void startClient () throws IOException;
	void stopClient () throws IOException;
}
