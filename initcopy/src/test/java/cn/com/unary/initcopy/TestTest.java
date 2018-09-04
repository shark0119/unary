package cn.com.unary.initcopy;

import org.junit.Test;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Properties;

public class TestTest {
    @Test
    public void test1() {
        try (FileInputStream fis = new FileInputStream("G:\\java\\initcopy\\src\\main\\proto\\init_copy.proto")) {
            FileChannel channel = fis.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(10);
            channel.read(buffer);
            System.out.println(new String(buffer.array()));
            buffer.clear();
            channel.close();
            channel.read(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {
        try (
                C c = new C()
        ) {
            c.t();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void test3 () {
        Properties prop = new Properties();
        InputStream input = null;
        try {

            String filename = "log4j.properties";
            input = Test.class.getClassLoader().getResourceAsStream(filename);
            System.out.println(this.getClass().getClassLoader().getResource(filename));
            if(input==null){
                System.out.println("Sorry, unable to find " + filename);
                return;
            }

            //load a properties file from class path, inside static method
            prop.load(input);

            //get the property value and print it out
            System.out.println(prop.getProperty("log4j.appender.console.Target"));
            System.out.println(prop.getProperty("log4j.rootLogger"));
            System.out.println(prop.getProperty("log4j.appender.console"));

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally{
            if(input!=null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void test4() {
        loop:
        for (int i = 0; i < 3; i++) {
            System.out.println(i);
            for (int j = 0; j < 4; j++) {
                continue loop;
            }
            System.out.println("i: " + i);
        }
    }
    private class C implements Closeable {
        public void t () {
            System.out.println("TTTTTTT");
        }
        @Override
        public void close() throws IOException {
            System.out.println("close close");
        }
    }
}
