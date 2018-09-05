package transmit.utils;

import io.netty.buffer.ByteBuf;

public class Message {
    private MessageHead messageHead;
    private ByteBuf content;

    public Message(MessageHead messageHead, ByteBuf content) {
        super();
        this.messageHead = messageHead;
        this.content = content;
    }

    public MessageHead getMessageHead() {
        return messageHead;
    }

    public void setMessageHead(MessageHead messageHead) {
        this.messageHead = messageHead;
    }

    public ByteBuf getContent() {
        return content;
    }

    public void setContent(ByteBuf content) {
        this.content = content;
    }

    public void setMessageType(int messageType) {
        messageHead.setMessageType(messageType);
    }

    public int getMessageType() {
        return messageHead.getMessageType();
    }

    public int getCompressType() {
        return messageHead.getCompressType();
    }

    public int getSize() {
        return messageHead.getSize();
    }

    public int getEncryptType() {
        return messageHead.getEncryptType();
    }

    public int getAgentId() {
        return messageHead.getAgentId();
    }

    @Override
    public String toString() {
        return "Message [messageHead=" + messageHead + ", content=" + content + "]";
    }

}
