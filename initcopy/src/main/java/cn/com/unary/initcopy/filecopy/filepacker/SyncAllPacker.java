package cn.com.unary.initcopy.filecopy.filepacker;

import api.UnaryTClient;
import cn.com.unary.initcopy.InitCopyContext;
import cn.com.unary.initcopy.dao.FileManager;
import cn.com.unary.initcopy.entity.Constants.PackType;
import cn.com.unary.initcopy.entity.FileInfo;
import cn.com.unary.initcopy.filecopy.io.AbstractFileInput;
import cn.com.unary.initcopy.utils.AbstractLogable;
import cn.com.unary.initcopy.utils.CommonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
@Component("syncAllPacker")
public class SyncAllPacker extends AbstractLogable implements Packer {

    // 保留多少个字节来表示数据包头部长度。默认5个字节，4个字节存储了一个整型代表包序号,还有一个字节的解析器种类标识
    public final static int HEAD_LENGTH = 4 + 1;
    // 文件信息长度。默认4个字节，存储了一个整型
    public final static int FILE_INFO_LENGTH = 4;
    // 每个包的大小
    public final static int PACK_SIZE = UnaryTClient.MAX_PACK_SIZE;

    // ----我来组成分割线，以下是 Spring 容器来管理的实体----
    @Autowired
    protected AbstractFileInput input;
    @Autowired
    protected FileManager ifm;
    @Autowired
    protected InitCopyContext ctx;
    @Autowired
    protected UnaryTClient utc;

    // ----我是另外一只分界线，以下是自定义全局变量
    private boolean isReady = false;    // 是否可以开始打包解析
    private Iterator<FileInfo> fiIterator;    // 待读取的文件列表
    private List<String> readFileIds = new ArrayList<>();    // 已读取的文件 ID 列表
    private ByteBuffer fileInfoBuffer; // 文件信息数据

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
    public void start(List<String> fileIds) throws IOException {
        if (isReady()) {
            List<FileInfo> list = ifm.query(fileIds.toArray(new String[fileIds.size()]));
            fiIterator = list.iterator();
            logger.debug("CurrentTask " + Thread.currentThread().getName()
                    + ". Got " + list.size() + " FileInfo with " + fileIds.size() + " FileId");
            byte[] packData = CommonUtils.extractBytes(pack());
            while (packData.length != 0) {
                logger.debug("CurrentTask {1}. Pass {2} bytes to Transfer.",
                        Thread.currentThread().getName(), packData.length);
                utc.sendData(packData);
                packData = CommonUtils.extractBytes(pack());
            }
            // TODO
        } else {
            throw new IllegalStateException("ERROR CODE 0X03:Object is not ready.");
        }

    }

    /**
     * 将文件数据打包
     *
     * @return 如无数据，返回长度为0 的字节数组
     */
    protected ByteBuffer pack() throws IOException {
        ByteBuffer buffer;
        if (PACK_SIZE < 256 * 1024) {
            buffer = ByteBuffer.allocate(PACK_SIZE);
        } else {
            buffer = ByteBuffer.allocateDirect(PACK_SIZE);
        }
        // set pack index , 4 bytes
        buffer.putInt(++packIndex);
        // set pack type
        buffer.put(this.getPackType().getValue());
        packFileInfo(buffer);
        FileInfo fi;
        fi = nextFile();
        while (buffer.hasRemaining() && fi != null) {
            if (fileInfoBuffer.hasRemaining()) {
                // 尝试丢弃未传输的文件信息
                throw new IllegalStateException("Program Error. Attempt to abandon valid file info.");
            }
            logger.debug("CurrentTask {1}. Got File With Id: {2}",
                    Thread.currentThread().getName(), fi.getId());
            // pack file info
            fileInfoBuffer = serializeFileInfo(fi);
            packFileInfo(buffer);
            // pack file data
            input.read(buffer);
            fi = nextFile();
        }
        logger.debug("CurrentTask " + Thread.currentThread().getName()
                + ". Pack Done. PackIndex: " + packIndex + ", PackSize: " + buffer.position());
        return buffer;
    }

    /**
     * 用于读取文件头数据，如有遗留，则读入 buffer 中。
     *
     * @param buffer 一个可以接受文件头数据的容器
     */
    private void packFileInfo(ByteBuffer buffer) {
        int readSize;
        if (buffer.remaining() >= fileInfoBuffer.remaining()) {
            // 不需要截断
            readSize = fileInfoBuffer.remaining();
        } else {
            // 需要截断
            readSize = buffer.remaining();
        }
        buffer.put(fileInfoBuffer.array(), fileInfoBuffer.position(), readSize);
        logger.debug("CurrentTask " + Thread.currentThread().getName()
                + ". Pack " + readSize + " bytes FileInfo, current position "
                + fileInfoBuffer.position());
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
            ByteBuffer buffer = ByteBuffer.allocate(fileInfoJsonBytes.length + FILE_INFO_LENGTH);
            // set file info size
            buffer.putInt(fileInfoJsonBytes.length);
            // set file info
            buffer.put(fileInfoJsonBytes);
            logger.debug("CurrentTask " + Thread.currentThread().getName() + ". FileID: "
                    + fi.getId() + ", file info json bytes length: " + buffer.capacity() + ".");
            return buffer;
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("ERROR 0x04 : Failed transfer FileInfo to Json", e);
        }
    }

    private FileInfo nextFile() throws IOException {
        FileInfo fi = null;
        if (fiIterator.hasNext()) {
            fi = fiIterator.next();
            input.openFile(fi.getFullName());
            readFileIds.add(fi.getId());
        }
        logger.debug("CurrentTask {1}. Got next file : {2}",
                Thread.currentThread().getName(), fi.getId());
        return fi;
    }

    @Override
    public PackType getPackType() {
        return PackType.SYNC_ALL_JAVA;
    }

    @Override
    public void close() throws Exception {
        if (input != null)
            input.close();
    }
}
