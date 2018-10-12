package cn.com.unary.initcopy.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.MessageOrBuilder;

import java.nio.ByteBuffer;

/**
 * 常用的一些工具
 *
 * @author Shark.Yin
 * @since 1.0
 */
public class CommonUtils {
    private CommonUtils(){}

    /**
     * 将字节数组转成整型，只取前四个字节
     *
     * @param b 字节数组
     * @return 整型
     */
    public static int byteArrayToInt(byte[] b, int offset) {
        return   b[offset + 3] & 0xFF |
                (b[offset + 2] & 0xFF) << 8 |
                (b[offset + 1] & 0xFF) << 16 |
                (b[offset] & 0xFF) << 24;
    }

    /**
     * 从 buffer 中提取出有效数据，从0到 {@link ByteBuffer#position()}
     *
     * @param buffer 一个数据buffer
     * @return 有效数据的字节数组
     */
    public static byte[] extractBytes (ByteBuffer buffer) {
        byte[] data = new byte[buffer.position()];
        // TODO 可做的优化，修改传输模块代码，将其改为 ByteBuffer
        System.arraycopy(buffer.array(), 0, data, 0, buffer.position());
        return data;
    }

    public static byte[] serToJson(Object obj) {
        return JSONObject.toJSONBytes(obj);
    }

    public static <T> T deSerFromJson(byte[] value, Class<T> clz) {
        return JSONObject.parseObject(value, clz);
    }

    public static String formatGrpcEntity(MessageOrBuilder builder) {
        return builder.toString().replaceAll("\\n", "");
    }
}
