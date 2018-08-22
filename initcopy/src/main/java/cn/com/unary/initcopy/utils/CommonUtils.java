package cn.com.unary.initcopy.utils;

import java.nio.ByteBuffer;

public class CommonUtils {
    private CommonUtils(){}

    /**
     * 将字节数组转成整型，只取前四个字节
     *
     * @param b 字节数组
     * @return 整型
     */
    public static int byteArrayToInt(byte[] b) {
        return   b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }

    /**
     * 从 buffer 中提取出有效数据，从0到 {@link ByteBuffer#position()}
     *
     * @param buffer 一个数据buffer
     * @return 有效数据的字节数组
     */
    public static byte[] extractBytes (ByteBuffer buffer) {
        byte[] data = new byte[buffer.position()];
        System.arraycopy(buffer.array(), 0, data, 0, buffer.position());
        return data;
    }
}
