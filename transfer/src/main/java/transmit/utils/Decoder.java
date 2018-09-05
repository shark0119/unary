package transmit.utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class Decoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // TODO Auto-generated method stub
        if (in.readableBytes() < Common.HEADSIZE) {
            return;
        }

        in.markReaderIndex();

        int type = in.readInt();
        int size = in.readInt();
        int compress = in.readInt();
        int encrypt = in.readInt();
        int agentid = in.readInt();

        if (in.readableBytes() < size) {
//            System.out.println("readable message size:"+in.readableBytes()+" less than data size:"+ size);
            in.resetReaderIndex();
            return;
        }

        MessageHead head = new MessageHead(type, size, compress, encrypt, agentid);
        ByteBuf c = in.readBytes(size);
        Message message = new Message(head, c);
        out.add(message);
    }

}
