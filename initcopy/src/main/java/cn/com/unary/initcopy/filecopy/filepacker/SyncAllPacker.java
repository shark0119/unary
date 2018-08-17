package cn.com.unary.initcopy.filecopy.filepacker;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.com.unary.initcopy.InitCopyContext;
import cn.com.unary.initcopy.dao.FileManager;
import cn.com.unary.initcopy.entity.Constants.PackType;
import cn.com.unary.initcopy.entity.FileInfo;
import cn.com.unary.initcopy.filecopy.io.AbstractFileInput;
import cn.com.unary.initcopy.mock.UnaryTransferClient;

/**
 * 文件打包与解析，同时包括源端打包和目标端解包
 * 
 * 文件数据格式
 * **********************************
 * *	文件信息长度 (4 字节)				*
 * **********************************
 * *								*
 * *	文件信息						*
 * *								*
 * **********************************
 * *								*
 * *	文件数据						*
 * *								*
 * *								*
 * *								*
 * **********************************
 * 
 * 包格式
 * **********************************
 * *	解析器种类标识(一个字节)			*
 * **********************************
 * *	文件数据包		 				*
 * **********************************
 * *								*
 * *	文件数据包						*
 * *								*
 * **********************************
 * *								*
 * *								*
 * *	文件数据包						*
 * *								*
 * *								*
 * **********************************
 * 
 * @author shark
 */
public class SyncAllPacker implements Packer {
	
	// Spring 容器来管理的实体
	protected AbstractFileInput input;
	protected FileManager ifm;
	protected InitCopyContext ctx;
	protected UnaryTransferClient utc;
	
	private static int PACK_SIZE ;	// 每个包的大小
	private boolean isReady = false;	// 是否可以开始打包解析
	
	private Iterator<FileInfo> fiIterator;	// 待读取的文件列表
	private List<String> readedFile = new ArrayList<>();	// 已读取的文件 ID 列表
	
	private byte[] fileInfoBytes;	// 文件信息
	private int position;	// 文件信息被截断的位置
	
	private ObjectMapper mapper = new ObjectMapper();
	
	public final static int HEAD_LENGTH_BYTES = 4;	// 保留多少个字节来表示文件信息长度。默认4个字节，和Java int 一样
	
	private int packIndex = 0;	// 当前的包序号

	protected boolean isReady () {
		if (isReady) {
			return isReady;
		} else {
			isReady = input == null 
					&& ifm == null 
					&& ctx == null
					&& utc == null;
			return isReady;
		}
	}
	
	@Override
	public void start(List<String> fileIds){
		if (isReady()) {
			fiIterator = ifm.query(fileIds.toArray(new String[fileIds.size()])).iterator();
			byte[] packData = pack();
			while (packData.length != 0) {
				utc.sendData(packData);
				packData = pack();
			}
		} else {
			throw new IllegalStateException("ERROR CODE 0X03:Object is not ready.");
		}
		
	}

	/**
	 * 将文件数据打包
	 * @param fileData
	 * @param fi
	 * @return 如无数据，返回长度为0 的字节数组
	 */
	protected byte[] pack () {
		ByteBuffer buffer ;
		if (PACK_SIZE < 256*1024)
			buffer = ByteBuffer.allocate(PACK_SIZE);
		else 
			buffer = ByteBuffer.allocateDirect(PACK_SIZE);
		buffer.putInt(++packIndex);
		packFileInfo (buffer);
		FileInfo fi ;
		while (buffer.hasRemaining()) {
			fi = nextFile();
			if (fi == null) {
				// 文件全部读取完毕
				break;
			}
			// pack file info
			serializeFileInfo(fi, buffer.remaining());
			packFileInfo (buffer);
			// pack file data
			buffer.put(input.read(buffer.remaining()));
		}
		return buffer.array();
	}
	/**
	 * 用于读取文件头数据，如有遗留，则读入 buffer 中。并以position记录当前读取位置
	 * @param buffer
	 */
	private void packFileInfo (ByteBuffer buffer) {
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
	/**
	 * 序列化头部信息
	 * @param fi 文件信息实体
	 * @param remaingPackSize 当前包剩余的空间大小
	 */
	private void serializeFileInfo (FileInfo fi, int remaingPackSize) {
		try {
			byte[] fibyte = mapper.writeValueAsBytes(fi);
			ByteBuffer buffer = ByteBuffer.allocate(fibyte.length);
			buffer.putInt(fibyte.length);
			buffer.put(fibyte);
			fileInfoBytes = buffer.array();
			position = 0;
		} catch (JsonProcessingException e) {
			throw new IllegalStateException("ERROR 0x04 : Failed transfer to Json", e);
		}
		
	}
	private FileInfo nextFile () {
		if (fiIterator.hasNext()) {
			FileInfo fi = fiIterator.next();
			readedFile.add(fi.getId());
			return fi;
		} else {
			return null;
		}
	}
	public void setInput(AbstractFileInput input) {		this.input = input;	}
	public void setIfm(FileManager ifm) {		this.ifm = ifm;	}
	public void setContext(InitCopyContext context) {		this.ctx = context;	}
	public void setUtc(UnaryTransferClient utc) {		
		this.utc = utc;	
		PACK_SIZE = utc.getMaxPackSize();
	}

	@Override
	public PackType getPackType() {
		return PackType.SYNC_ALL_JAVA;
	}
}
