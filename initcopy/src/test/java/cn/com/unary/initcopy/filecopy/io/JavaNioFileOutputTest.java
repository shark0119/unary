package cn.com.unary.initcopy.filecopy.io;

import cn.com.unary.initcopy.service.filecopy.io.AbstractFileOutput;
import cn.com.unary.initcopy.service.filecopy.io.JavaNioFileOutput;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class JavaNioFileOutputTest {

    protected AbstractFileOutput afo = new JavaNioFileOutput();
    protected byte[] data = {64,65,66,67,68,69,70};

    @Before
    public void setUp() throws Exception {
        afo = new JavaNioFileOutput();
        afo.openFile("G:\\temp\\test.txt");
    }

    @After
    public void tearDown() throws Exception {
        afo.close();
    }

    @Test
    public void write() throws IOException {
        afo.write(data);
    }

    @Test
    public void write1() throws IOException {
        afo.write(data, 3, 2);
    }

    @Test
    public void test1 () throws IOException {
        FileChannel fc = FileChannel.open(Paths.get("G:\\temp\\test.txt"), StandardOpenOption.WRITE);
        ByteBuffer buffer  = ByteBuffer.wrap(data);
        buffer.flip();
        int size = fc.write(buffer);
        fc.force(true);
        System.out.println(size);
    }
}