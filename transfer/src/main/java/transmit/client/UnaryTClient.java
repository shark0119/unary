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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class UnaryTClient {
    private final String serverIp;
    private final int serverPort;
    private final String agentIp;
    private final String agentMac;
    private Channel channel;
    private Bootstrap bootstrap;
    private EventLoopGroup workGroup;
    private int id;
    private Process process = null;
    private CountDownLatch latch;
    private boolean isReady;

    public UnaryTClient(String serverIp, int port, String agentIp, String agentMac) {
        this (serverIp, port, agentIp, agentMac, null);
    }
    public UnaryTClient(String serverIp, int port, String agentIp, String agentMac, CountDownLatch latch) {
        this.serverIp = serverIp;
        this.serverPort = port;
        this.agentIp = agentIp;
        this.agentMac = agentMac;
        this.id = 0;
        this.latch = latch;
    }
    public void startClient() throws Exception {
        workGroup = new NioEventLoopGroup();
        try {
            bootstrap = new Bootstrap();
            bootstrap.group(workGroup)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(serverIp, serverPort))
                    .handler(new UanryClientInitializer(this));

            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            doConnect();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void stopClient() throws Exception {
        if (channel == null) {
            return;
        }
        channel.close();
        workGroup.shutdownGracefully().sync();
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
                    isReady = true;
                } else {
                    isReady = false;
                }
                latch.countDown();
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
        final String agentIp = args[2];
        final String agentMac = args[3];
        new UnaryTClient(host, port, agentIp, agentMac).startClient();
    }

    public String getServerIp() {
        return serverIp;
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getAgentIp() {
        return agentIp;
    }

    public String getAgentMac() {
        return agentMac;
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

    public boolean isReady() {
        return isReady;
    }
}
