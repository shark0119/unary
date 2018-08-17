package com.cn.unary;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

import org.junit.Test;

public class FileChannelTest {
	final Path copy_from = Paths.get("E:\\Shark_File\\Download/CentOS-6.0-x86_64-bin-DVD1.iso");
	final Path copy_to = Paths.get("C:/centos.iso");
	long startTime, elapsedTime;
	int bufferSizeKB = 4; // also tested for 16, 32, 64, 128, 256 and 1024
	int bufferSize = bufferSizeKB * 1024;

	File inFileStr = copy_from.toFile();
	File outFileStr = copy_to.toFile();
	
	public static void deleteCopied(Path path) {
		try {
			Files.deleteIfExists(path);
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}

	// FileChannel and non-direct buffer
	@Test
	public void test1 () {
		System.out.println("Using FileChannel and non-direct buffer ...");
		try (FileChannel fileChannel_from = (FileChannel.open(copy_from, EnumSet.of(StandardOpenOption.READ)));
				FileChannel fileChannel_to = (FileChannel.open(copy_to,
						EnumSet.of(StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)))) {

			startTime = System.nanoTime();

			// Allocate a non-direct ByteBuffer
			ByteBuffer bytebuffer = ByteBuffer.allocate(bufferSize);

			// Read data from file into ByteBuffer
			int bytesCount;
			while ((bytesCount = fileChannel_from.read(bytebuffer)) > 0) {
				// flip the buffer which set the limit to current position, and position to 0
				bytebuffer.flip();
				// write data from ByteBuffer to file
				fileChannel_to.write(bytebuffer);
				// for the next read
				bytebuffer.clear();
			}

			elapsedTime = System.nanoTime() - startTime;
			System.out.println("Elapsed Time is " + (elapsedTime / 1000000000.0) + " seconds");
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
	// FileChannel and direct buffer
	@Test
	public void test2 () {
		System.out.println("Using FileChannel and direct buffer ...");
		try (FileChannel fileChannel_from = (FileChannel.open(copy_from, EnumSet.of(StandardOpenOption.READ)));
				FileChannel fileChannel_to = (FileChannel.open(copy_to,
						EnumSet.of(StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)))) {

			startTime = System.nanoTime();

			// Allocate a direct ByteBuffer
			ByteBuffer bytebuffer = ByteBuffer.allocateDirect(bufferSize);

			// Read data from file into ByteBuffer
			int bytesCount;
			while ((bytesCount = fileChannel_from.read(bytebuffer)) > 0) {
				// flip the buffer which set the limit to current position, and position to 0
				bytebuffer.flip();
				// write data from ByteBuffer to file
				fileChannel_to.write(bytebuffer);
				// for the next read
				bytebuffer.clear();
			}

			elapsedTime = System.nanoTime() - startTime;
			System.out.println("Elapsed Time is " + (elapsedTime / 1000000000.0) + " seconds");
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
	// FileChannel.transferTo()
	@Test
	public void test3 () {
		System.out.println("Using FileChannel.transferTo method ...");
		try (FileChannel fileChannel_from = (FileChannel.open(copy_from, EnumSet.of(StandardOpenOption.READ)));
				FileChannel fileChannel_to = (FileChannel.open(copy_to,
						EnumSet.of(StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)))) {

			startTime = System.nanoTime();

			fileChannel_from.transferTo(0L, fileChannel_from.size(), fileChannel_to);

			elapsedTime = System.nanoTime() - startTime;
			System.out.println("Elapsed Time is " + (elapsedTime / 1000000000.0) + " seconds");
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
	// FileChannel.transferFrom()
	@Test
	public void test4 () {
		System.out.println("Using FileChannel.transferFrom method ...");
		try (FileChannel fileChannel_from = (FileChannel.open(copy_from, EnumSet.of(StandardOpenOption.READ)));
				FileChannel fileChannel_to = (FileChannel.open(copy_to,
						EnumSet.of(StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)))) {

			startTime = System.nanoTime();

			fileChannel_to.transferFrom(fileChannel_from, 0L, (int) fileChannel_from.size());

			elapsedTime = System.nanoTime() - startTime;
			System.out.println("Elapsed Time is " + (elapsedTime / 1000000000.0) + " seconds");
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
	// FileChannel.map
	@Test
	public void test5() {
		System.out.println("Using FileChannel.map method ...");
		try (FileChannel fileChannel_from = (FileChannel.open(copy_from, EnumSet.of(StandardOpenOption.READ)));
				FileChannel fileChannel_to = (FileChannel.open(copy_to,
						EnumSet.of(StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)))) {

			startTime = System.nanoTime();
			MappedByteBuffer buffer = fileChannel_from.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel_from.size());

			fileChannel_to.write(buffer);
			buffer.clear();

			elapsedTime = System.nanoTime() - startTime;
			System.out.println("Elapsed Time is " + (elapsedTime / 1000000000.0) + " seconds");
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
	// Buffered Stream I/O
	@Test
	public void test6() {
		System.out.println("Using buffered streams and byte array ...");
		try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(inFileStr));
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outFileStr))) {

			startTime = System.nanoTime();

			byte[] byteArray = new byte[bufferSize];
			int bytesCount;
			while ((bytesCount = in.read(byteArray)) != -1) {
				out.write(byteArray, 0, bytesCount);
			}

			elapsedTime = System.nanoTime() - startTime;
			System.out.println("Elapsed Time is " + (elapsedTime / 1000000000.0) + " seconds");
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}
	@Test
	public void test7() {
		System.out.println("Using un-buffered streams and byte array ...");
		try (FileInputStream in = new FileInputStream(inFileStr);
				FileOutputStream out = new FileOutputStream(outFileStr)) {

			startTime = System.nanoTime();

			byte[] byteArray = new byte[bufferSize];
			int bytesCount;
			while ((bytesCount = in.read(byteArray)) != -1) {
				out.write(byteArray, 0, bytesCount);
			}

			elapsedTime = System.nanoTime() - startTime;
			System.out.println("Elapsed Time is " + (elapsedTime / 1000000000.0) + " seconds");
		} catch (IOException ex) {
			System.err.println(ex);
		}

		deleteCopied(copy_to);
	}
	@Test
	public void test8() {
		System.out.println("Using Files.copy (Path to Path) method ...");
		try {
			startTime = System.nanoTime();
			Files.copy(copy_from, copy_to, NOFOLLOW_LINKS);
			elapsedTime = System.nanoTime() - startTime;
			System.out.println("Elapsed Time is " + (elapsedTime / 1000000000.0) + " seconds");
		} catch (IOException e) {
			System.err.println(e);
		}

	}
	@Test
	public void test9 () {
		System.out.println("Using Files.copy (InputStream to Path) ...");
		try (InputStream is = new FileInputStream(copy_from.toFile())) {

			startTime = System.nanoTime();

			Files.copy(is, copy_to);

			elapsedTime = System.nanoTime() - startTime;
			System.out.println("Elapsed Time is " + (elapsedTime / 1000000000.0) + " seconds");
		} catch (IOException e) {
			System.err.println(e);
		}

		deleteCopied(copy_to);
	}
	@Test
	public void test10() {
		System.out.println("Using Files.copy (Path to OutputStream) ...");
		try (OutputStream os = new FileOutputStream(copy_to.toFile())) {

			startTime = System.nanoTime();

			Files.copy(copy_from, os);

			elapsedTime = System.nanoTime() - startTime;
			System.out.println("Elapsed Time is " + (elapsedTime / 1000000000.0) + " seconds");
		} catch (IOException e) {
			System.err.println(e);
		}
	}
}
