package cn.com.unary.initcopy.mock;

import api.UnaryChannel;
import api.UnaryHandler;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 打桩使用的类
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Component("TransferChannel")
@Scope("singleton")
public class TransferChannel implements UnaryChannel {

    private UnaryHandler handler;

    @Override
    public void writeData(byte[] data) {
        handler.handler(data);
    }

    @Override
    public void setHandler(UnaryHandler handler) {
        this.handler = handler;
    }

    @Override
    public int sendMessage(byte[] data) {
        return sendMessage(data, 10);
    }

    @Override
    public int sendMessage(byte[] data, int time) {
        // TODO 调用源端的监听程序，向源端发送数据
        return 0;
    }
}
