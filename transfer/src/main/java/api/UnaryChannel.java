package api;

public interface UnaryChannel {
	void setHandler (UnaryHandler hanler);
	int sendMessage (byte[] data);
	int sendMessage (byte[] data, int time);
}
