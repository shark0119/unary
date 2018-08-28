package com.cn.unary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import cn.com.unary.initcopy.filecopy.filepacker.SyncAllPacker;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import cn.com.unary.initcopy.config.BeanConfig;
import cn.com.unary.initcopy.utils.MD5FileUtil;

@Component
public class TestTest {
	@Test
	public void test () {
		Date d = new Date ();
		System.out.println(d);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			d = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(d.toString());
			System.out.println(d);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
/*	@Test
	public void test1 () {
		Function<Te, String> f = Te::fun;
		// Function<Te, String> f2 = Te::fun2;
		Function<Te, Void> f1 = Te::nonShow;
		Supplier<String> s = Te::staticFun;
	}*/
	@Test
	public void test3 () {
		File file = new File("G:\\temp\\奸商.java");
		try {
			file.createNewFile();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		try {
			FileInputStream fis = new FileInputStream(file);
			byte [] b = new byte[100];
			fis.read(b);
			System.out.println(file.getName());
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	@Test
	public void test4 () {
		String [] fileIds = {"id1", "id2", "id3"};
		StringBuilder sb = new StringBuilder("select * from FILE_INFO WHERE FILE_ID IN ('");
		for (String fileId : fileIds) {
			sb.append(fileId + "','");
		}
		sb.delete(sb.length()-2, sb.length());
		sb.append(")");
		System.out.println(sb.toString());
	}
	@Test
	public void test5 () {
		try {
			String md5 = MD5FileUtil.getFileMD5(new File("E:\\Shark_File\\Download\\nexus-3.13.0-01-win64.zip"));
			System.out.println(md5.equals("aad5dc5503c7a508e56cb8192f503f38"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static String str;
	
	@Autowired
	@Qualifier("str2")
	public void setStr(String str) {
		TestTest.str = str;
	}
	public static void main (String [] args) {
		try {
			AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(BeanConfig.class);
			/*FileInfo f = ac.getBean(FileInfo.class);
			FileInfo f1 = ac.getBean(FileInfo.class);
			System.out.println(f.equals(f1));*/
			System.out.println(TestTest.str);
			ac.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	@Test
	public void test7() {
		int length = UUID.randomUUID().toString().length();
		System.out.println(length);
	}
	@Test
	public void test8 () {
		long size = 2075974L;
		long packSize = size /(991);
        System.out.println(packSize);

        int i = SyncAllPacker.BUFFER_DIRECT_LIMIT;
        System.out.println("buffer:" + i);
        System.out.println(10/100);
	}

}
