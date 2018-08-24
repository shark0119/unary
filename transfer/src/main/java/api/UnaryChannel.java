package api;

public interface UnaryChannel {
    void writeData(byte[] data);

    void setHandler(UnaryHandler handler);

    int sendMessage(byte[] data);

    int sendMessage(byte[] data, int time);
}
