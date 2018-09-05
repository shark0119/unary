package transmit.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import transmit.utils.Decoder;
import transmit.utils.Encoder;

public class UanryClientInitializer extends ChannelInitializer<SocketChannel> {

    private final UnaryTClient client;

    public UanryClientInitializer(UnaryTClient client) {
        this.client = client;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        // TODO Auto-generated method stub
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast("logging", new LoggingHandler(LogLevel.DEBUG));
        pipeline.addLast("idle", new IdleStateHandler(0, 5, 0));
        pipeline.addLast("encoder", new Encoder());
        pipeline.addLast("decoder", new Decoder());
        pipeline.addLast("handle", new UnaryClientHandler(client));
    }
}
