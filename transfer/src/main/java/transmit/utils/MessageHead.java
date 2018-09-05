package transmit.utils;

public class MessageHead {
    private int messageType;
    private int size;
    private int compressType;
    private int encryptType;
    private int agentId;

    public MessageHead(int messageType, int size, int compressType, int encryptType, int agentId) {
        super();
        this.messageType = messageType;
        this.size = size;
        this.compressType = compressType;
        this.encryptType = encryptType;
        this.agentId = agentId;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public int getCompressType() {
        return compressType;
    }

    public void setCompressType(int compressType) {
        this.compressType = compressType;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getEncryptType() {
        return encryptType;
    }

    public void setEncryptType(int encryptType) {
        this.encryptType = encryptType;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    @Override
    public String toString() {
        return "MessageHead [messageType=" + messageType + ", size=" + size + ", compressType=" + compressType
                + ", encryptType=" + encryptType + ", agentId=" + agentId + "]";
    }

}
