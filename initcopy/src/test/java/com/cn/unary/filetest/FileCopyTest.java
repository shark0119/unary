package com.cn.unary.filetest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import cn.com.unary.initcopy.utils.CommonUtils;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.xml.bind.SchemaOutputResolver;

public class FileCopyTest {

	public static class FileObj {
		private String name;
		private String path;
		private byte[] content;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public byte[] getContent() {
			return content;
		}

		public void setContent(byte[] content) {
			this.content = content;
		}

		public void append(byte[] content, int size) {
			if (this.content == null) {
				this.content = content;
			} else {
				try {
					this.content = new String(this.content, "ISO-8859-1")
							.concat(new String(content, 0, size, "ISO-8859-1")).getBytes("ISO-8859-1");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		public void transISO () {
			try {
				this.content = new String(this.content, "ISO-8859-1").getBytes("ISO-8859-1");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Test
	public void test() {
		File source = new File("G:\\C++\\Test\\pointer.exe");

		File target = new File("G:\\C++\\Test\\pTarget.exe");

		FileObj fo = new FileObj();
		fo.setName(source.getName());
		fo.setPath(source.getPath());
		try {
			target.createNewFile();
			FileInputStream fis = new FileInputStream(source);
			byte[] b = new byte[100];
			int size, i = 0;
			while ((size = fis.read(b)) != -1) {
				System.out.println("第 " + ++i + "次");
				fo.append(b, size);
			}
			fis.close();
			System.out.println(fo.getContent().length);
			ObjectMapper mapper = new ObjectMapper();
			byte [] transB = mapper.writeValueAsBytes(fo); // uft-8 的字符串字节数组
			System.out.println(transB.length);
			
			FileObj dest = null;
			dest = mapper.readValue(transB, FileObj.class);
			dest.transISO();
			System.out.println(dest.getContent().length);
			FileOutputStream fos = new FileOutputStream(target);
			fos.write(dest.getContent());
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("success");
	}

	@Test
	public void test2() {
		File source = new File("G:\\C++\\Test\\pointer.exe");

		File target = new File("G:\\C++\\Test\\pTarget.exe");

		FileObj fo = new FileObj();
		fo.setName(source.getName());
		fo.setPath(source.getPath());
		try {
			target.createNewFile();
			FileInputStream fis = new FileInputStream(source);
			FileOutputStream fos = new FileOutputStream(target);
			byte[] b = new byte[100];
			int size, i = 0;
			while ((size = fis.read(b)) != -1) {
				System.out.println("第 " + ++i + "次");
				fos.write(b, 0, size);
			}
			fis.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("success");

	}
	
	@Test
	public void test3 () {
		byte []b = {32, 114, 117, 110, 32, 105, 110, 32, 68, 79, 83, 32, 109, 111, 100, 101, 46, 13, 13, 10, 36, 0, 0, 0, 0, 0, 0, 0, 80, 69, 0, 0, 76, 1, 15, 0, -61, -96, 48, 63, 91, 0};
		try {
			System.out.println(b.length);
			byte []a = new String(b, "ISO-8859-1").getBytes("ISO-8859-1");
			System.out.println(a.length);
			for (int i=0; i<a.length; i++) {
				System.out.print(a[i] + ",");
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void test4 () {
		MappedByteBuffer mbb = null;
	}

	@Test
	public void test5 () throws IOException {
		Path path = Paths.get("G:\\temp\\test.txt");
		FileChannel channel = FileChannel.open(path, StandardOpenOption.WRITE);
        byte[] data = {57,58,59,60,61,62,65,66};
		ByteBuffer buffer = ByteBuffer.allocate(8);

        System.out.println("buffer position:" + buffer.position());
        System.out.println("buffer limit:" + buffer.limit());
        System.out.println("buffer remaining:" + buffer.remaining());
        System.out.println("buffer length:" + buffer.array().length);
		buffer.put(data);
        System.out.println();
        System.out.println("buffer position:" + buffer.position());
        System.out.println("buffer limit:" + buffer.limit());
        System.out.println("buffer remaining:" + buffer.remaining());
        System.out.println("buffer length:" + buffer.array().length);
		buffer.flip();
        System.out.println();
        System.out.println("buffer position:" + buffer.position());
        System.out.println("buffer limit:" + buffer.limit());
        System.out.println("buffer remaining:" + buffer.remaining());
        System.out.println("buffer array length:" + buffer.array().length);

        channel.position(10);
		channel.write(buffer);

        System.out.println(channel.position());
        channel.truncate(5);
		channel.force(true);

		channel.close();
	}
	@Test
    public void test6 () throws IOException {
        Path path = Paths.get("G:\\temp\\test.txt");
        FileChannel channel = FileChannel.open(path, StandardOpenOption.WRITE);

        channel.position(2);
        channel.truncate(2);
        channel.close();
    }
}
