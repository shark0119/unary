package cn.com.unary.initcopy.utils;

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.*;

public class CommonUtilsTest {

    @Test
    public void byteArrayToInt() {
        int i = 34567;
        byte[] data = new byte[4];
        data[3] = (byte) (i & 0xFF);
        data[2] = (byte) ((byte) (i >> 8) & 0xFF);
        data[1] = (byte) ((byte) (i >> 16) & 0xFF);
        data[0] = (byte) ((byte) (i >> 24) & 0xFF);
        i = CommonUtils.byteArrayToInt(data);
        System.out.println(i);
    }

    @Test
    public void extractBytes() {
        byte[] data = {1,2,3,4,5};
        ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.put(data);
        data = CommonUtils.extractBytes(buffer);
        for (byte b:data) {
            System.out.println(b);
        }
    }
}