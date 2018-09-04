package api;

import java.io.IOException;

public interface UnaryTServer {
	void startServer(int port) throws IOException;
	void stopServer() throws IOException;
	void setProcess (UnaryProcess process);
	UnaryProcess getProcess ();
}
