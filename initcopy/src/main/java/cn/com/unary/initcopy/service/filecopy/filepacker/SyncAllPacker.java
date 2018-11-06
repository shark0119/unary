package cn.com.unary.initcopy.service.filecopy.filepacker;

import cn.com.unary.initcopy.InitCopyContext;
import cn.com.unary.initcopy.common.AbstractLoggable;
import cn.com.unary.initcopy.common.utils.CommonUtils;
import cn.com.unary.initcopy.dao.FileManager;
import cn.com.unary.initcopy.entity.Constants;
import cn.com.unary.initcopy.entity.Constants.PackerType;
import cn.com.unary.initcopy.entity.FileInfoDO;
import cn.com.unary.initcopy.exception.InfoPersistenceException;
import cn.com.unary.initcopy.grpc.entity.SyncProcess;
import cn.com.unary.initcopy.service.filecopy.io.NioFileInput;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.ByteBuffer;
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
 * ***********************************
 * *当前任务Id(36字节,根据编码格式而异)*
 * *	数据包序号(4字节)  			 *
 * ***********************************
 * *	解析器种类标识(一个字节)		 *
 * ***********************************
 * *	文件数据包		 			 *
 * ***********************************
 * *								 *
 * *	文件数据包					 *
 * *								 *
 * ***********************************
 * *								 *
 * *								 *
 * *	文件数据包					 *
 * *								 *
 * *								 *
 * ***********************************
 *
 * @author shark
 */
@Component("SyncAllPacker")
@Scope("prototype")
public class SyncAllPacker extends AbstractLoggable implements Packer {

    /**
     * 保留 41个字节来表示数据包头部长度。默认 41个字节，
     * 4个字节存储了一个整型代表包序号, 1字节的解析器种类标识,剩余的字节存储了任务Id.
     */
    public final static int HEAD_LENGTH = InitCopyContext.UUID_LEN + 4 + 1;
    /**
     * 文件信息长度。默认4个字节，存储了一个整型
     */
    public final static int FILE_INFO_LENGTH = 4;
    /**
     * 每个包的大小
     */
    public final static int PACK_SIZE = InitCopyContext.MAX_PACK_SIZE;

    /**
     * ----我来组成分割线，以下是 Spring 容器来管理的实体----
     */
    @Autowired
    @Qualifier("NioFileInput")
    private NioFileInput input;
    @Autowired
    @Qualifier("clientFM")
    private FileManager fm;
    /**
     * ----我是另外一只分界线，以下是自定义全局变量----
     * <p>
     * 待读取的文件列表
     */
    private Iterator<FileInfoDO> fiIterator;
    private FileInfoDO currentFileInfo;
    /**
     * 文件信息数据
     */
    private ByteBuffer fileInfoBuffer;
    private byte[] taskId;
    private int packIndex = 0;
    private final Object lock = new Object();
    private volatile boolean close;
    private SyncProcess syncProcess;

    @Override
    public void init(String taskId)
            throws IOException, InfoPersistenceException {
        this.init(taskId, null);
    }

    /**
     * @see Packer#init(String, SyncProcess)
     * 依赖于 FileManager 中存储的文件信息的相关实体
     */
    @Override
    public void init(String taskId, SyncProcess process) throws IOException, InfoPersistenceException {
        close = false;
        List<FileInfoDO> fiList = fm.queryFileByTaskIdAndState(taskId, FileInfoDO.STATE.WAIT);
        if (process == null) {
            this.taskId = taskId.getBytes(InitCopyContext.CHARSET);
        } else {
            packIndex = process.getPackIndex();
            currentFileInfo = fm.queryById(process.getFileId());
            if (currentFileInfo == null) {
                throw new IOException("File not found:" + taskId);
            }
            input.openFile(currentFileInfo.getFullName());
            input.position(process.getFilePos());
            // 不会续传文件元信息数据
            fileInfoBuffer.position(fileInfoBuffer.limit());
            if (0L == process.getFilePos()) {
                // 如果当前文件未同步到文件内容，则抛弃当前文件的同步进度，作为一个新文件开始同步
                currentFileInfo.setState(FileInfoDO.STATE.WAIT);
                fiList.add(0, currentFileInfo);
                currentFileInfo = null;
                fileInfoBuffer = null;
            }
        }
        fiIterator = fiList.iterator();
    }

    @Override
    public byte[] pack() throws IOException, InfoPersistenceException {
        if (close) {
            logger.info(String.format("Packer already closed."));
            return null;
        }
        ByteBuffer buffer;
        buffer = ByteBuffer.allocate(PACK_SIZE);
        // Set TaskId 4 bytes, PackIndex 4 bytes, PackType 1 byte.
        buffer.put(taskId);
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
        while (buffer.hasRemaining() && null != currentFileInfo) {
            if (!(Constants.FileType.REGULAR_FILE.equals(currentFileInfo.getFileType()))
                    || !input.read(buffer)) {
                // 如果读到文件尾或者是个非常规文件，则打开一个新文件
                takeToNextFile(buffer);
            }
        }
        if (buffer.position() <= HEAD_LENGTH) {
            logger.info(String.format("Pack %d ignore, cause it's empty, size %d."
                    , packIndex, buffer.position()));
            buffer.clear();
            return null;
        } else {
            logger.info(String.format("Pack Done. PackIndex:%d, PackSize:%d.", packIndex, buffer.position()));
            return CommonUtils.extractBytes(buffer);
        }
    }

    /**
     * 当前文件无有效数据可读
     *
     * @param buffer buffer 容器
     * @throws IOException              IO 异常
     * @throws InfoPersistenceException 持久层异常
     */
    private void takeToNextFile(ByteBuffer buffer) throws IOException, InfoPersistenceException {
        if (currentFileInfo != null) {
            currentFileInfo.setState(FileInfoDO.STATE.SYNCED);
            currentFileInfo.setSyncDoneTime(System.currentTimeMillis());
            fm.save(currentFileInfo);
        }
        if (fiIterator.hasNext()) {
            currentFileInfo = fiIterator.next();
            if (currentFileInfo.getFileType().equals(Constants.FileType.REGULAR_FILE)) {
                input.openFile(currentFileInfo.getFullName());
            }
            currentFileInfo.setState(FileInfoDO.STATE.SYNCING);
            fm.save(currentFileInfo);
            logger.debug(String.format("Got next file:%s. Ser to json.", currentFileInfo.getFileId()));
            try {
                byte[] fileInfoJsonBytes = JSON.toJSONBytes(currentFileInfo);
                fileInfoBuffer = ByteBuffer.allocate(fileInfoJsonBytes.length + FILE_INFO_LENGTH);
                // set file info size and file info
                fileInfoBuffer.putInt(fileInfoJsonBytes.length);
                fileInfoBuffer.put(fileInfoJsonBytes);
                logger.debug(String.format("FileId:%s, fileInfo json bytes length:%d."
                        , currentFileInfo.getFileId(), fileInfoBuffer.capacity()));
                fileInfoBuffer.flip();
            } catch (Exception e) {
                throw new IllegalStateException("ERROR 0x04 : Failed transfer FileInfo to Json", e);
            }
            packFileInfo(buffer);
        } else {
            currentFileInfo = null;
            logger.info("We have no file anymore.");
            fileInfoBuffer = ByteBuffer.allocate(0);
        }
    }

    private void packFileInfo(ByteBuffer buffer) {
        if (currentFileInfo == null || !buffer.hasRemaining()) {
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
        logger.debug(String.format("Pack %d bytes FileInfo, current position %d."
                , readSize, fileInfoBuffer.position()));
    }

    @Override
    public PackerType getPackType() {
        return PackerType.SYNC_ALL_JAVA;
    }

    @Override
    public SyncProcess close() throws IOException {
        if (!close) {
            close = true;
            if (input != null) {
                if (currentFileInfo != null) {
                    syncProcess = SyncProcess.newBuilder()
                            .setFileId(currentFileInfo.getFileId())
                            .setPackIndex(packIndex)
                            .setFilePos(input.position()).build();
                }
                input.close();
            }
        }
        return this.syncProcess;
    }

}
