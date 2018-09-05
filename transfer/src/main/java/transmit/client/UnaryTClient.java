package transmit.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import transmit.utils.Common;
import transmit.utils.Message;
import transmit.utils.MessageHead;
import transmit.utils.Process;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class UnaryTClient {
    private final String serverip;
    private final int serverport;
    private final String agentip;
    private final String agentmac;
    private Channel channel = null;
    private Bootstrap bootstrap;
    private EventLoopGroup workgroup;
    private int id;
    private Process process = null;

    public UnaryTClient(String serverip, int port, String agentip, String agentmac) {
        this.serverip = serverip;
        this.serverport = port;
        this.agentip = agentip;
        this.agentmac = agentmac;
        this.id = 0;
    }

    public void startClient() throws Exception {
        workgroup = new NioEventLoopGroup();
        try {
            bootstrap = new Bootstrap();
            bootstrap.group(workgroup)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(serverip, serverport))
                    .handler(new UanryClientInitializer(this));

            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            doConnect();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void stopClient() throws Exception {
        if (channel == null)
            return;
        channel.close();
        workgroup.shutdownGracefully().sync();
    }

    protected void doConnect() {
        if (channel != null && channel.isActive()) {
            return;
        }

        ChannelFuture future = bootstrap.connect();

        future.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture futureListener) throws Exception {
                if (futureListener.isSuccess()) {
                    channel = futureListener.channel();
                } else {
                    futureListener.channel().eventLoop().schedule(new Runnable() {
                        @Override
                        public void run() {
                            doConnect();
                        }
                    }, 10, TimeUnit.SECONDS);
                }
            }
        });

    }

    public int sendMessage(byte[] content) {
        if (channel == null || !channel.isActive()) {
            return -1;
        }

        ByteBuf c = Unpooled.copiedBuffer(content);
        MessageHead head = new MessageHead(Common.DATA,
                c.writerIndex(),
                Common.NOCOMPRESS,
                Common.NOENCRYPT,
                id);
        Message message = new Message(head, c);
        try {
            channel.writeAndFlush(message);
            return content.length;
        } catch (Exception e) {
            return -1;
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 4) {
            return;
        }
        final String host = args[0];
        final int port = Integer.parseInt(args[1]);
        final String agentip = args[2];
        final String agentmac = args[3];
        new UnaryTClient(host, port, agentip, agentmac).startClient();
    }

    public String getServerip() {
        return serverip;
    }

    public int getServerport() {
        return serverport;
    }

    public String getAgentip() {
        return agentip;
    }

    public String getAgentmac() {
        return agentmac;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Process getprocess() {
        return process;
    }

    public void setprocess(Process process) {
        this.process = process;
    }

}
