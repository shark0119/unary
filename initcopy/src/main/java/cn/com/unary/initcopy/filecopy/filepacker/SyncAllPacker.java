package cn.com.unary.initcopy.filecopy.filepacker;

import api.UnaryTClient;
import cn.com.unary.initcopy.InitCopyContext;
import cn.com.unary.initcopy.dao.FileManager;
import cn.com.unary.initcopy.entity.Constants.PackType;
import cn.com.unary.initcopy.entity.FileInfo;
import cn.com.unary.initcopy.filecopy.io.AbstractFileInput;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 文件打包与解析，同时包括源端打包和目标端解包
 * <p>
 * 文件数据格式
 * **********************************
 * *	文件信息长度 (4 字节)			*
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
 * <p>
 * 包格式
 * **********************************
 * *								*
 * *	数据包序号(4字节)  			*
 * **********************************
 * *	解析器种类标识(一个字节)		*
 * **********************************
 * *	文件数据包		 			*
 * **********************************
 * *								*
 * *	文件数据包					*
 * *								*
 * **********************************
 * *								*
 * *								*
 * *	文件数据包					*
 * *								*
 * *								*
 * **********************************
 *
 * @author shark
 */
public class SyncAllPacker implements Packer {

    // 保留多少个字节来表示文件信息长度。默认5个字节，4个字节存储了一个整型,还有一个字节的解析器种类标识
    public final static int HEAD_LENGTH = 4 + 1;
    // 文件信息长度。默认4个字节，存储了一个整型
    public final static int FILE_INFO_LENGTH = 4;
    // 每个包的大小
    public final static int PACK_SIZE = UnaryTClient.MAX_PACK_SIZE;

    // ----我来组成分割线，以下是 Spring 容器来管理的实体----
    protected AbstractFileInput input;
    protected FileManager ifm;
    protected InitCopyContext ctx;
    protected UnaryTClient utc;

    // ----我是另外一只分界线，以下是自定义全局变量
    private boolean isReady = false;    // 是否可以开始打包解析
    private Iterator<FileInfo> fiIterator;    // 待读取的文件列表
    private List<String> readFileIds = new ArrayList<>();    // 已读取的文件 ID 列表
    private byte[] fileInfoBytes;    // 文件信息
    private int position;    // 文件信息被截断的位置
    private ObjectMapper mapper = new ObjectMapper();
    private int packIndex = 0;    // 当前的包序号

    protected boolean isReady() {
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
    public void start(List<String> fileIds) {
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
     *
     * @return 如无数据，返回长度为0 的字节数组
     */
    protected byte[] pack() {
        ByteBuffer buffer;
        if (PACK_SIZE < 256 * 1024) {
            buffer = ByteBuffer.allocate(PACK_SIZE);
        } else {
            buffer = ByteBuffer.allocateDirect(PACK_SIZE);
        }
        // set pack index
        buffer.putInt(++packIndex);
        // set pack type
        buffer.put(this.getPackType().getValue());
        packFileInfo(buffer);
        FileInfo fi;
        while (buffer.hasRemaining()) {
            fi = nextFile();
            // 文件全部读取完毕
            if (fi == null) {
                break;
            } else if (position < fileInfoBytes.length) {
                throw new IllegalStateException("Program Error.");
            }
            // pack file info
            fileInfoBytes = serializeFileInfo(fi).array();
            position = 0;
            packFileInfo(buffer);
            // pack file data
            buffer.put(input.read(buffer.remaining()));
        }
        return buffer.array();
    }

    /**
     * 用于读取文件头数据，如有遗留，则读入 buffer 中。并以position记录当前读取位置
     *
     * @param buffer 一个可以接受文件头数据的容器
     */
    private void packFileInfo(ByteBuffer buffer) {
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
     * 序列化头部信息，将文件基础信息序列化成 JSON 字串并包装到 ByteBuffer
     *
     * @param fi 文件信息实体
     * @return 包装了文件信息的实体
     */
    private ByteBuffer serializeFileInfo(FileInfo fi) {
        try {
            byte[] fileInfoJsonBytes = mapper.writeValueAsBytes(fi);
            ByteBuffer buffer = ByteBuffer.allocate(fileInfoJsonBytes.length);
            // set file info size
            buffer.putInt(fileInfoJsonBytes.length);
            // set file info
            buffer.put(fileInfoJsonBytes);
            return buffer;
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("ERROR 0x04 : Failed transfer FileInfo to Json", e);
        }
    }

    private FileInfo nextFile() {
        FileInfo fi = null;
        if (fiIterator.hasNext()) {
            fi = fiIterator.next();
            readFileIds.add(fi.getId());
        }
        return fi;
    }

    public void setInput(AbstractFileInput input) {
        this.input = input;
    }

    public void setIfm(FileManager ifm) {
        this.ifm = ifm;
    }

    public void setContext(InitCopyContext context) {
        this.ctx = context;
    }

    public void setUtc(UnaryTClient utc) {
        this.utc = utc;
    }

    @Override
    public PackType getPackType() {
        return PackType.SYNC_ALL_JAVA;
    }

    @Override
    public void close() throws IOException {
        if (input != null)
            input.close();
    }
}
