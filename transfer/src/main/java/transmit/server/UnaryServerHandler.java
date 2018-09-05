package transmit.server;

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

public class UnaryServerHandler extends SimpleChannelInboundHandler<Message> {

    public static InternalLogger logger = InternalLoggerFactory.getInstance(UnaryServerHandler.class);
    private final UnaryTServer server;

    public UnaryServerHandler(UnaryTServer server) {
        this.server = server;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) {
        // TODO Auto-generated method stubMessage
        ByteBuf content = msg.getContent();
        ByteBuf c = Unpooled.buffer(Common.BUFSIZE);
        int agentid;
        String agentip, agentmac;
        try {
            switch (msg.getMessageType()) {
                case Common.REGISTER:
                    String serverip = Common.byteBufToString(content);
                    int serverport = content.readInt();
                    agentip = Common.byteBufToString(content);
                    agentmac = Common.byteBufToString(content);
                    logger.info("Server received reg serverip: {} serverport:{} agentip:{} mac:{}", serverip, serverport, agentip, agentmac);
                    agentid = UnaryConnetMap.addClientConnection(ctx, agentip, agentmac);
                    c.writeInt(agentid);
                    Common.stringToByteBuf(agentip, c);
                    Common.stringToByteBuf(agentmac, c);
                    MessageHead ackhead = new MessageHead(Common.REGISTER_ACK,
                            c.writerIndex(),
                            Common.NOCOMPRESS,
                            Common.NOENCRYPT,
                            agentid);
                    Message ackmessage = new Message(ackhead, c);
                    ctx.writeAndFlush(ackmessage);
                    break;
                case Common.PING:
                    agentid = content.readInt();
                    agentip = Common.byteBufToString(content);
                    agentmac = Common.byteBufToString(content);
                    int status = content.readInt();
                    logger.debug("Server received ping msg,agentid:{} ip:{} mac:{}", agentid, agentip, agentmac);
                    agentid = UnaryConnetMap.addClientConnection(ctx, agentip, agentmac);
                    c.writeInt(agentid);
                    Common.stringToByteBuf(agentip, c);
                    Common.stringToByteBuf(agentmac, c);
                    c.writeInt(status);
                    Common.stringToByteBuf(new Date().toString(), c);
                    MessageHead ponghead = new MessageHead(Common.PONG,
                            c.writerIndex(),
                            Common.NOCOMPRESS,
                            Common.NOENCRYPT,
                            agentid);
                    Message pongmessage = new Message(ponghead, c);
                    ctx.writeAndFlush(pongmessage);
                    break;
                case Common.DATA:
                    if (server.getDataprocess() != null) {
                        if (content.hasArray()) {
                            server.getDataprocess().handler(content.array());
                        } else {
                            byte[] b = new byte[msg.getSize()];
                            content.readBytes(b);
                            server.getDataprocess().handler(b);
                        }
                    }
                    break;
                default:
                    break;
            }
        } finally {
            ReferenceCountUtil.release(msg);
            ReferenceCountUtil.release(content);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case READER_IDLE:
                    UnaryConnect coninfo = UnaryConnetMap.getClientConnection(ctx);
                    if (coninfo != null) {
                        if (coninfo.getHandshake() < 5) {
                            coninfo.selfAddHandshake();
                            logger.debug("server reader idle :" + coninfo.getHandshake());
                        } else {
                            coninfo.setBreaken();
                            logger.info("server breaken agent :" + coninfo.getAgentip());
                            ctx.close();
                        }
                    } else {
                        ctx.close();
                    }
                    break;
                default:
                    break;
            }
        }
    }

}
