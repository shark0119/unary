package transmit.server;

import api.UnaryHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import transmit.utils.Common;
import transmit.utils.Message;
import transmit.utils.MessageHead;

import java.net.InetSocketAddress;

public class UnaryTServer {

    public static InternalLogger logger = InternalLoggerFactory.getInstance(UnaryTServer.class);
    private final int port;
    private ServerBootstrap bootstrap;
    private ChannelFuture f;
    private NioEventLoopGroup bossgroup;
    private NioEventLoopGroup workergroup;
    private UnaryHandler process = null;

    public UnaryTServer(int port) {
        this.port = port;
    }

    public void startServer() throws Exception {
        bossgroup = new NioEventLoopGroup(1);
        workergroup = new NioEventLoopGroup();
        try {
            bootstrap = new ServerBootstrap();
            bootstrap.group(bossgroup, workergroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new UanryServerInitializer(this));

            f = bootstrap.bind().sync();
            logger.info("{} started and listen on {}", UnaryTServer.class.getName(), f.channel().localAddress());
            f.channel().closeFuture().sync();
        } finally {
            bossgroup.shutdownGracefully().sync();
            workergroup.shutdownGracefully().sync();
        }
    }

    public void stopServer() throws Exception {
        if (f == null)
            return;
        f.channel().closeFuture().sync();
        bossgroup.shutdownGracefully().sync();
        workergroup.shutdownGracefully().sync();
    }

    public int sendMessage(byte[] context, String agentip) {
        UnaryConnect info = UnaryConnetMap.getClientConnection(agentip);
        if (info == null || info.getBreaken())
            return -1;
        ByteBuf c = Unpooled.copiedBuffer(context);
        MessageHead head = new MessageHead(Common.DATA,
                c.writerIndex(),
                Common.NOCOMPRESS,
                Common.NOENCRYPT,
                info.getAgentid());
        Message message = new Message(head, c);
        try {
            info.getCtx().writeAndFlush(message);
            return 0;
        } catch (Exception e) {
            return -1;
        }
    }

    public UnaryHandler getDataprocess() {
        return process;
    }

    public void setDataprocess(UnaryHandler process) {
        this.process = process;
    }

}
