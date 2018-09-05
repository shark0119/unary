package cn.com.unary.initcopy.filecopy.filepacker;

import api.UnaryTransferClient;
import cn.com.unary.initcopy.common.AbstractLoggable;
import cn.com.unary.initcopy.dao.FileManager;
import cn.com.unary.initcopy.entity.Constants.PackerType;
import cn.com.unary.initcopy.entity.FileInfoDO;
import cn.com.unary.initcopy.exception.InfoPersistenceException;
import cn.com.unary.initcopy.filecopy.io.AbstractFileInput;
import cn.com.unary.initcopy.utils.CommonUtils;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
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
public class SyncAllPacker extends AbstractLoggable implements Packer {

    /**
     * 保留 9个字节来表示数据包头部长度。默认 9个字节，
     * 4个字节存储了任务Id，4个字节存储了一个整型代表包序号,还有一个字节的解析器种类标识
     */
    public final static int HEAD_LENGTH = 4 + 4 + 1;
    /**
     * 文件信息长度。默认4个字节，存储了一个整型
     */
    public final static int FILE_INFO_LENGTH = 4;
    /**
     * 每个包的大小
     */
    public final static int PACK_SIZE = UnaryTransferClient.MAX_PACK_SIZE;
    private final static int BUFFER_DIRECT_LIMIT = 16 * 1024 * 1024;

    /**
     * ----我来组成分割线，以下是 Spring 容器来管理的实体----
     */
    @Autowired
    @Qualifier("JavaNioFileInput")
    private AbstractFileInput input;
    @Autowired
    @Qualifier("clientFM")
    private FileManager fm;
    private UnaryTransferClient transfer;

    /**
     * ----我是另外一只分界线，以下是自定义全局变量----
     */
    /**
     * 待读取的文件列表
     */
    private Iterator<FileInfoDO> fiIterator;
    /**
     * 当前正在读取的文件
     */
    private FileInfoDO currentFileInfo;
    /**
     * 文件信息数据
     */
    private ByteBuffer fileInfoBuffer;
    /**
     * 当前的包序号
     */
    private int packIndex = 0;
    /**
     * 任务ID。
     */
    private Integer taskId;
    /**
     * 当前打包进程是否暂停
     */
    private volatile boolean pause;

    public SyncAllPacker() {
        pause = false;
    }

    @Override
    public void start(Integer taskId, UnaryTransferClient transfer) throws IOException, InfoPersistenceException {
        this.taskId = taskId;
        this.transfer = transfer;
        List<FileInfoDO> list = fm.queryUnSyncFileByTaskId(taskId);
        fiIterator = list.iterator();
        byte[] packData;
        try {
            packData = CommonUtils.extractBytes(pack());
        } catch (ClosedChannelException e) {
            logger.warn("packer stop because channel close.");
            return;
        }
        while (packData.length > HEAD_LENGTH) {
            logger.debug("Pass " + packData.length + " bytes to Transfer.");
            this.transfer.sendData(packData);
            try {
                packData = CommonUtils.extractBytes(pack());
            } catch (ClosedChannelException e) {
                logger.warn("packer stop because channel close.");
                return;
            }
        }
    }

    /**
     * 将文件数据打包
     *
     * @return 如无数据，返回长度为0 的字节数组
     */
    private ByteBuffer pack() throws IOException, InfoPersistenceException {
        if (pause) {
            return ByteBuffer.allocate(0);
        }
        ByteBuffer buffer;
        if (PACK_SIZE < BUFFER_DIRECT_LIMIT) {
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

        FileInfoDO fi;
        // 第一次打包，需要先打开一个文件
        if (fileInfoBuffer == null) {
            fi = nextFile();
            if (null != fi) {
                fileInfoBuffer = serializeFileInfo(fi);
                packFileInfo(buffer);
            } else {
                // 若无未读文件，直接返回
                logger.debug("Pack Done. PackIndex: " + packIndex + ", PackSize: " + buffer.position());
                return buffer;
            }
        }
        // 上次打包还有残留
        if (fileInfoBuffer.hasRemaining()) {
            packFileInfo(buffer);
        }
        boolean fileNotDone;
        // 一直读取直到包满
        while (buffer.hasRemaining()) {
            // 包未满时，文件头信息应该已被读完
            if (fileInfoBuffer.hasRemaining()) {
                throw new IllegalStateException("Program error");
            } else {
                fileNotDone = input.read(buffer);
                if (!fileNotDone) {
                    // 如果读到文件尾，则打开一个新文件
                    fi = nextFile();
                    if (null != fi) {
                        fileInfoBuffer = serializeFileInfo(fi);
                        packFileInfo(buffer);
                    } else {
                        // 若无未读文件，直接返回
                        break;
                    }
                } else {
                    // 当文件未读到文件尾部时，包应该是满的
                    if (buffer.hasRemaining()) {
                        throw new IllegalStateException("Program error");
                    }
                }
            }
        }

        logger.debug("Pack Done. PackIndex: " + packIndex + ", PackSize: " + buffer.position());
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
        if (readSize == 0) {
            return;
        }
        buffer.put(fileInfoBuffer.array(), fileInfoBuffer.position(), readSize);
        fileInfoBuffer.position(fileInfoBuffer.position() + readSize);
        logger.debug("Pack " + readSize + " bytes FileInfo, current position "
                + fileInfoBuffer.position());
    }

    /**
     * 序列化头部信息，将文件基础信息序列化成 JSON 字串并包装到 ByteBuffer
     *
     * @param fi 文件信息实体
     * @return 包装了文件信息的实体
     */
    private ByteBuffer serializeFileInfo(FileInfoDO fi) {
        logger.debug("Ser to json.File id:" + fi.getFileId());
        try {
            byte[] fileInfoJsonBytes = JSON.toJSONBytes(fi);
            ByteBuffer buffer = ByteBuffer.allocate(fileInfoJsonBytes.length + FILE_INFO_LENGTH);
            // set file info size
            buffer.putInt(fileInfoJsonBytes.length);
            // set file info
            buffer.put(fileInfoJsonBytes);
            logger.debug("FileID: " + fi.getFileId() + ", file info json bytes length: " + buffer.capacity() + ".");
            buffer.flip();
            return buffer;
        } catch (Exception e) {
            throw new IllegalStateException("ERROR 0x04 : Failed transfer FileInfo to Json", e);
        }
    }

    private FileInfoDO nextFile() throws IOException, InfoPersistenceException {
        if (currentFileInfo != null) {
            currentFileInfo.setState(FileInfoDO.STATE.SYNCED);
            fm.save(currentFileInfo);
        }
        if (fiIterator.hasNext()) {
            currentFileInfo = fiIterator.next();
            input.openFile(currentFileInfo.getFullName());
            currentFileInfo.setState(FileInfoDO.STATE.SYNCING);
            fm.save(currentFileInfo);
            logger.debug("Got next file : " + currentFileInfo.getFileId());
            return currentFileInfo;
        }
        return null;
    }

    @Override
    public PackerType getPackType() {
        return PackerType.SYNC_ALL_JAVA;
    }

    @Override
    public void close() throws IOException {
        if (pause) {
            return;
        }
        if (input != null) {
            input.close();
        }
        transfer.stopClient();
        pause = true;
    }
}
