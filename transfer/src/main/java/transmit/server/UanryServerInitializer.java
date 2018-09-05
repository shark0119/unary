package transmit.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import transmit.utils.Decoder;
import transmit.utils.Encoder;

public class UanryServerInitializer extends ChannelInitializer<SocketChannel> {

    private final UnaryTServer server;

    public UanryServerInitializer(UnaryTServer server) {
        this.server = server;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        final EventExecutorGroup group = new DefaultEventExecutorGroup(16);
        // TODO Auto-generated method stub
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast("logging", new LoggingHandler(LogLevel.DEBUG));
        pipeline.addLast("idle", new IdleStateHandler(10, 0, 0));
        pipeline.addLast("decoder", new Decoder());
        pipeline.addLast("encoder", new Encoder());
        pipeline.addLast(group, "handle", new UnaryServerHandler(server));
    }

}
