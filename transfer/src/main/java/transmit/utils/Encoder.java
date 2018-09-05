package transmit.utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class Encoder extends MessageToByteEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) {
        // TODO Auto-generated method stub
        try {
            out.writeInt(msg.getMessageType());
            out.writeInt(msg.getSize());
            out.writeInt(msg.getCompressType());
            out.writeInt(msg.getEncryptType());
            out.writeInt(msg.getAgentId());
            out.writeBytes(msg.getContent(), msg.getSize());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
