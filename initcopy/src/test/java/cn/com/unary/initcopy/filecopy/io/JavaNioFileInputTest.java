package cn.com.unary.initcopy.filecopy.io;

import cn.com.unary.initcopy.service.filecopy.io.JavaNioFileInput;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;

public class JavaNioFileInputTest {

    JavaNioFileInput input = new JavaNioFileInput();
    @Test
    public void read() throws IOException {
        input.openFile("C:\\Users\\shark\\Desktop\\文件\\Shadowsocks-4.0.10.zip");
        int index = 0;
        while (true) {
            System.out.println("index:" +index++);
            ByteBuffer buffer = ByteBuffer.allocate(991);
            if (!input.read(buffer)) {
                System.out.println(buffer.position());
                break;
            } else
                System.out.println(buffer.position());
        }
        input.read(ByteBuffer.allocate(20));
    }

    @Test
    public void openFile() {

    }
}