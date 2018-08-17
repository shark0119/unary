package com.cn.unary.initcopy.logger;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ByteBufferTest {
	private static byte[] fileInfoBytes;
	private static int position;
	
	@Test
	public void test1 () {
		ByteBuffer buffer = ByteBuffer.allocate(3);
		byte[] bytes = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		fileInfoBytes = bytes;
		position = 0;
		while (position < fileInfoBytes.length) {
			packFileInfo (buffer);
			
			System.out.println("position is " + position + " and buffer is " + s(buffer.array(), buffer.position()));
			buffer = ByteBuffer.allocate(3);
		}
		System.out.println("current position is " + position);
	}
	static String s (byte[]bs, int position) {
		String s = "";
		for (int i=0; i<position; i++) {
			s += bs[i] + " ,";
		}
		return s;
	}
	static void packFileInfo (ByteBuffer buffer) {
		int readSize;
		if (buffer.remaining() >= fileInfoBytes.length - position) {
			// 不需要截断
			readSize = fileInfoBytes.length - position;
		} else {
			// 需要截断
			readSize = buffer.remaining();
		}
		buffer.put(fileInfoBytes, position, readSize);
		position += readSize;
	}
	
	
	@Test
	public void test2 () throws JsonProcessingException {
		List<Integer> is = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writeValueAsBytes(is).length);
		is.add(2);
		System.out.println(mapper.writeValueAsBytes(is).length);
		is.add(2);
		System.out.println(mapper.writeValueAsBytes(is).length);
		is.add(2);
		System.out.println(mapper.writeValueAsBytes(is).length);
		is.add(2147483647);
		System.out.println(mapper.writeValueAsBytes(is).length);
		is.add(2147483647);
		System.out.println(mapper.writeValueAsBytes(is).length);
	}
}
