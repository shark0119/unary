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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 文件打包
 * 一个Packer只能处理一个任务的文件
 * 非线程安全
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
 * *	当前任务Id(4字节)			*
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
@Component("SyncAllPacker")
@Scope("prototype")
public class SyncAllPacker extends AbstractLogable implements Packer {

    // 保留多少个字节来表示数据包头部长度。默认5个字节，
    // 4个字节存储了任务Id，4个字节存储了一个整型代表包序号,还有一个字节的解析器种类标识
    public final static int HEAD_LENGTH = 4 + 4 + 1;
    // 文件信息长度。默认4个字节，存储了一个整型
    public final static int FILE_INFO_LENGTH = 4;
    // 每个包的大小
    public final static int PACK_SIZE = UnaryTClient.MAX_PACK_SIZE;

    // ----我来组成分割线，以下是 Spring 容器来管理的实体----
    @Autowired
    @Qualifier("JavaNioInput")
    protected AbstractFileInput input;
    @Autowired
    protected FileManager fm;
    @Autowired
    protected InitCopyContext ctx;

    protected UnaryTClient utc;

    // ----我是另外一只分界线，以下是自定义全局变量----
    private boolean isReady = false;    // 是否可以开始打包解析
    private Iterator<FileInfo> fiIterator;    // 待读取的文件列表
    private List<String> readFileIds = new ArrayList<>();    // 已读取的文件 ID 列表
    private FileInfo currentFileInfo;   // 当前正在读取的文件
    private ByteBuffer fileInfoBuffer; // 文件信息数据
    private ObjectMapper mapper = new ObjectMapper();
    private int packIndex = 0;    // 当前的包序号
    private int taskId = -1; // 任务ID。
    private boolean pause = false;  // 当前打包进程是否暂停

    /**
     * 相关成员参数不能为空，任务Id不能为-1
     *
     * @return 当前打包器是否就绪
     */
    protected boolean isReady() {
        if (isReady) {
            return isReady;
        } else {
            isReady = input == null
                    && fm == null
                    && ctx == null
                    && utc == null
                    && taskId != -1;
            return isReady;
        }
    }

    /**
     * 如果该任务是从暂停状态中恢复过来
     *
     * @param fileIds 文件的UUID
     * @throws IOException
     */
    @Override
    public void start(List<String> fileIds) throws IOException {
        if (isReady() && !pause) {
            List<FileInfo> list = fm.query(fileIds.toArray(new String[fileIds.size()]));
            fiIterator = list.iterator();
            logger.debug("CurrentTask " + Thread.currentThread().getName()
                    + ". Got " + list.size() + " FileInfo with " + fileIds.size() + " FileId");
            byte[] packData = CommonUtils.extractBytes(pack());
            while (packData.length != 0) {
                logger.debug("CurrentTask " + Thread.currentThread().getName()
                        + ". Pass " + packData.length + " bytes to Transfer.");
                utc.sendData(packData);
                packData = CommonUtils.extractBytes(pack());
            }
        } else {
            throw new IllegalStateException("ERROR CODE 0X03:Object is not ready.");
        }
    }

    @Override
    public void restore(int taskId) throws Exception {
        if (isReady() && !pause) {
            List<FileInfo> list = fm.queryByTaskId(taskId);
            List<FileInfo> unSyncFileList = new ArrayList<>();
            for (FileInfo fileInfo : list) {
                if (fileInfo.getStateEnum().equals(FileInfo.STATE.SYNCED)) {
                    continue;
                } else {
                    fileInfo.setState(FileInfo.STATE.WAIT);
                    unSyncFileList.add(fileInfo);
                }
            }
            fiIterator = unSyncFileList.iterator();
            pause = false;
            logger.debug("CurrentTask " + Thread.currentThread().getName()
                    + ". Got " + list.size() + " FileInfo and "
                    + unSyncFileList.size() + " unsync Files");
            byte[] packData = CommonUtils.extractBytes(pack());
            while (packData.length != 0) {
                logger.debug("CurrentTask " + Thread.currentThread().getName()
                        + ". Pass " + packData.length + " bytes to Transfer.");
                utc.sendData(packData);
                packData = CommonUtils.extractBytes(pack());
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
    protected ByteBuffer pack() throws IOException {
        if (pause) {
            try {
                this.close();
            } catch (Exception e) {
                throw new IOException(e);
            }
            return ByteBuffer.allocate(0);
        }
        ByteBuffer buffer;
        if (PACK_SIZE < 256 * 1024) {
            buffer = ByteBuffer.allocate(PACK_SIZE);
        } else {
            buffer = ByteBuffer.allocateDirect(PACK_SIZE);
        }
        // set task id, 4 bytes
        buffer.putInt(taskId);
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
            logger.debug("CurrentTask " + Thread.currentThread().getName()
                    + ". Got File With Id: {2}" + fi.getId());
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
        if (currentFileInfo != null) {
            currentFileInfo.setState(FileInfo.STATE.SYNCED);
            fm.save(currentFileInfo);
            readFileIds.add(currentFileInfo.getId());
        }
        if (fiIterator.hasNext()) {
            currentFileInfo = fiIterator.next();
            input.openFile(currentFileInfo.getFullName());
        }
        currentFileInfo.setState(FileInfo.STATE.SYNCING);

        fm.save(currentFileInfo);
        logger.debug("CurrentTask " + Thread.currentThread().getName()
                + ". Got next file : {2}" + currentFileInfo.getId());
        return currentFileInfo;
    }

    @Override
    public PackType getPackType() {
        return PackType.SYNC_ALL_JAVA;
    }

    @Override
    public void pause() {
        pause = true;
    }

    @Override
    public Packer setTransfer(UnaryTClient unaryTClient) {
        this.utc = unaryTClient;
        return this;
    }

    @Override
    public Packer setTaskId(int taskId) {
        this.taskId = taskId;
        return this;
    }

    @Override
    public void close() throws Exception {
        if (input != null)
            input.close();
    }
}
