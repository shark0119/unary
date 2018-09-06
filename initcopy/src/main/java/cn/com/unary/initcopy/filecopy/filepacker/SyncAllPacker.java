package cn.com.unary.initcopy.filecopy.filepacker;

import api.UnaryTransferClient;
import cn.com.unary.initcopy.common.AbstractLoggable;
import cn.com.unary.initcopy.common.utils.CommonUtils;
import cn.com.unary.initcopy.dao.FileManager;
import cn.com.unary.initcopy.entity.Constants;
import cn.com.unary.initcopy.entity.Constants.PackerType;
import cn.com.unary.initcopy.entity.FileInfoDO;
import cn.com.unary.initcopy.entity.SyncTaskDO;
import cn.com.unary.initcopy.exception.InfoPersistenceException;
import cn.com.unary.initcopy.filecopy.io.AbstractFileInput;
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
    public void start(Integer taskId, UnaryTransferClient transfer)
            throws IOException, InfoPersistenceException {
        this.taskId = taskId;
        this.transfer = transfer;
        List<FileInfoDO> list = fm.queryUnSyncFileByTaskId(taskId);
        fiIterator = list.iterator();
        byte[] packData;
        SyncTaskDO taskDO = fm.queryTask(taskId);
        transfer.startClient(taskDO.getTargetInfo().getIp(), taskDO.getTargetInfo().getTransferPort());
        while (true) {
            try {
                packData = CommonUtils.extractBytes(pack());
            } catch (ClosedChannelException e) {
                logger.warn("packer stop because channel close.");
                return;
            }
            if (packData.length <= HEAD_LENGTH) {
                logger.warn("pack " + packIndex + " ignore, cause it's empty, size:" + packData.length);
                break;
            }
            logger.debug("Pass " + packData.length + " bytes to Transfer.");
            this.transfer.sendData(packData);
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
        // Set TaskId 4 bytes, PackIndex 4 bytes, PackType 1 byte.
        buffer.putInt(taskId);
        buffer.putInt(++packIndex);
        buffer.put(this.getPackType().getValue());

        // 第一次打包，需要先打开一个文件
        if (fileInfoBuffer == null) {
            takeToNextFile(buffer);
        }
        if (fileInfoBuffer.hasRemaining()) {
            // 上次打包还有残留文件信息
            packFileInfo(buffer);
        }
        // 一直读取直到包满，或者无文件
        while (null != currentFileInfo && buffer.hasRemaining()) {
            if (!(Constants.FileType.REGULAR_FILE.equals(currentFileInfo.getFileType()))
                || !input.read(buffer)) {
                // 如果读到文件尾，则打开一个新文件
                takeToNextFile(buffer);
            }
        }
        logger.debug("Pack Done. PackIndex: " + packIndex + ", PackSize: " + buffer.position());
        return buffer;
    }

    /**
     * 当前文件无有效数据可读
     *
     * @param buffer buffer 容器
     * @throws IOException IO 异常
     * @throws InfoPersistenceException 持久层异常
     */
    private void takeToNextFile(ByteBuffer buffer) throws IOException, InfoPersistenceException {
        if (currentFileInfo != null) {
            currentFileInfo.setState(FileInfoDO.STATE.SYNCED);
            fm.save(currentFileInfo);
        }
        if (fiIterator.hasNext()) {
            currentFileInfo = fiIterator.next();
            if (currentFileInfo.getFileType().equals(Constants.FileType.REGULAR_FILE))
                input.openFile(currentFileInfo.getFullName());
            currentFileInfo.setState(FileInfoDO.STATE.SYNCING);
            fm.save(currentFileInfo);
            logger.debug("Got next file:" + currentFileInfo.getFileId() + ". Ser to json.");
            try {
                byte[] fileInfoJsonBytes = JSON.toJSONBytes(currentFileInfo);
                fileInfoBuffer = ByteBuffer.allocate(fileInfoJsonBytes.length + FILE_INFO_LENGTH);
                // set file info size and file info
                fileInfoBuffer.putInt(fileInfoJsonBytes.length);
                fileInfoBuffer.put(fileInfoJsonBytes);
                logger.debug("FileID: " + currentFileInfo.getFileId() +
                        ", file info json bytes length: " + fileInfoBuffer.capacity() + ".");
                fileInfoBuffer.flip();
            } catch (Exception e) {
                throw new IllegalStateException("ERROR 0x04 : Failed transfer FileInfo to Json", e);
            }
            packFileInfo(buffer);
        } else {
            currentFileInfo = null;
            logger.info("we have no file anymore.");
            fileInfoBuffer = ByteBuffer.allocate(0);
        }
    }

    private void packFileInfo(ByteBuffer buffer) {
        if (currentFileInfo == null) {
            return;
        }
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
