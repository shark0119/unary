package transmit.utils;

import io.netty.buffer.ByteBuf;

public final class Common {

    public static final int REGISTER = 1;
    public static final int REGISTER_ACK = 2;
    public static final int PING = 3;
    public static final int PONG = 4;
    public static final int DATA = 5;

    public static final int NOCOMPRESS = 0;

    public static final int NOENCRYPT = 0;

    public static final int HEADSIZE = 20;
    public static final int BUFSIZE = 256;

    public static String byteBufToString(ByteBuf buffer) {
        int len = buffer.readInt();
        byte[] byteArray = new byte[len];
        buffer.readBytes(byteArray, 0, len);
        return new String(byteArray);
    }

    public static void stringToByteBuf(String msg, ByteBuf buffer) {
        buffer.writeInt(msg.length());
        buffer.writeBytes(msg.getBytes());
    }
}
