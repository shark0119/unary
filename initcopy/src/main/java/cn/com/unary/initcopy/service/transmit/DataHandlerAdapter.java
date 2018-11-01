package cn.com.unary.initcopy.service.transmit;

/**
 * 针对于传输模块的数据处理适配器
 *
 * @author Shark.Yin
 * @since 1.0
 */
public interface DataHandlerAdapter {
    /**
     * 处理数据
     *
     * @param data 待处理的数据
     */
    void handle (byte[] data);
}
