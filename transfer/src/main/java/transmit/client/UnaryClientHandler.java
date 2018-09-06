package transmit.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import transmit.utils.Common;
import transmit.utils.Message;
import transmit.utils.MessageHead;

import java.util.Date;

public class UnaryClientHandler extends SimpleChannelInboundHandler<Message> {

    private final UnaryTClient client;
    public static InternalLogger logger = InternalLoggerFactory.getInstance(UnaryTClient.class);

    public UnaryClientHandler(UnaryTClient client) {
        this.client = client;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ByteBuf c = Unpooled.buffer(Common.BUFSIZE);
        Common.stringToByteBuf(client.getServerIp(), c);
        c.writeInt(client.getServerPort());
        Common.stringToByteBuf(client.getAgentIp(), c);
        Common.stringToByteBuf(client.getAgentMac(), c);
        MessageHead head = new MessageHead(Common.REGISTER,
                c.writerIndex(),
                Common.NOCOMPRESS,
                Common.NOENCRYPT,
                client.getId());
        Message message = new Message(head, c);
        ctx.writeAndFlush(message);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Message in) {
        ByteBuf content = in.getContent();
        int agentid;
        try {
            switch (in.getMessageType()) {
                case Common.REGISTER_ACK:
                    agentid = content.readInt();
                    client.setId(agentid);
                    break;
                case Common.PONG:
                    agentid = content.readInt();
                    client.setId(agentid);
                    break;
                case Common.DATA:
                    if (client.getprocess() != null) {
                        if (content.hasArray()) {
                            client.getprocess().process(content.array());
                        } else {
                            byte[] b = new byte[in.getSize()];
                            content.readBytes(b);
                            client.getprocess().process(b);
                        }
                    }
                    break;
            }
        } finally {
            ReferenceCountUtil.release(in);
            ReferenceCountUtil.release(content);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        client.doConnect();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // TODO Auto-generated method stub
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case WRITER_IDLE:
                    ByteBuf c = Unpooled.buffer(Common.BUFSIZE);
                    c.writeInt(client.getId());
                    Common.stringToByteBuf(client.getAgentIp(), c);
                    Common.stringToByteBuf(client.getAgentMac(), c);
                    c.writeInt(1);
                    Common.stringToByteBuf(new Date().toString(), c);
                    MessageHead head = new MessageHead(Common.PING,
                            c.writerIndex(),
                            Common.NOCOMPRESS,
                            Common.NOENCRYPT,
                            client.getId());
                    Message message = new Message(head, c);
                    ctx.writeAndFlush(message);
                    logger.debug("send ping message to server----------");
                    break;
                default:
                    break;
            }
        }
    }
}
