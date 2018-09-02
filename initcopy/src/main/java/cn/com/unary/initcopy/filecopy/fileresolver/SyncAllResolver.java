package cn.com.unary.initcopy.filecopy.fileresolver;

import cn.com.unary.initcopy.dao.FileManager;
import cn.com.unary.initcopy.entity.Constants.PackType;
import cn.com.unary.initcopy.entity.FileInfoDO;
import cn.com.unary.initcopy.exception.UnaryIoException;
import cn.com.unary.initcopy.filecopy.filepacker.SyncAllPacker;
import cn.com.unary.initcopy.filecopy.io.AbstractFileOutput;
import cn.com.unary.initcopy.common.AbstractLogable;
import cn.com.unary.initcopy.utils.CommonUtils;
import cn.com.unary.initcopy.utils.PathMapperUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 全复制时使用的文件解析器，标识为
 * PackType.SYNC_ALL_JAVA
 * 抛出的 IllegalStateException#Program Error 都是调试代码
 *
 * @author shark
 */
@Component("SyncAllResolver")
@Scope("prototype")
public class SyncAllResolver extends AbstractLogable implements Resolver {

    public static final int PACK_SIZE = SyncAllPacker.PACK_SIZE;
    public static final int HEAD_LENGTH = SyncAllPacker.HEAD_LENGTH;
    public static final int FILE_INFO_LENGTH = SyncAllPacker.FILE_INFO_LENGTH;
    /**
     * 文件信息长度，当被截断时，暂存此处
     */
    private final ByteBuffer fileInfoLenBuf = ByteBuffer.allocate(FILE_INFO_LENGTH);
    @Autowired
    @Qualifier("serverFM")
    protected FileManager fm;
    /**
     * 标识了这个文件在传输中的包分布情况，以下分别标识了开始和结束包，以及在其中的长度。
     * 默认文件是连续的形式，分布在中间的包，应该是填充满的。
     */
    private int beginPackIndex, endPackIndex, beginPackSize, endPackSize;
    @Autowired
    private AbstractFileOutput output;
    private ReadProcess stage = ReadProcess.CONTENT_DONE;
    private ByteBuffer fileInfo;
    /**
     * 当前读取的包序号
     */
    private int packIndex;
    private String backupPath;
    private int taskId;
    private FileInfoDO currentFile;

    @Override
    public SyncAllResolver setBackupPath(String backupPath) {
        this.backupPath = backupPath;
        return this;
    }

    @Override
    public boolean process(byte[] data) {
        packIndex = CommonUtils.byteArrayToInt(data, 4);
        // get pack info
        if (!PackType.valueOf(data[HEAD_LENGTH - 1]).equals(getPackType())) {
            throw new IllegalStateException("ERROR 0x05 : Bad data pack format. Wrong Resolver");
        }
        int currentPos = HEAD_LENGTH;
        // 判断上次读取的位置
        switch (stage) {
            case FILE_INFO_LENGTH:
                // 同下，从文件头读取
            case FILE_INFO_LENGTH_DONE:
                // 同下，从文件头读取
            case FILE_INFO:
                // 同下，从文件头读取
            case CONTENT_DONE:
                // 从文件头读取
                // 单个包中有多个文件
                while (currentPos < data.length) {
                    try {
                        currentPos = readFileData(data, readFileInfo(data, currentPos));
                    } catch (IOException e) {
                        throw new UnaryIoException("ERROR 0x01 : IO error.", e);
                    }
                }
                break;
            case FILE_INFO_DONE:
                // 同下，从文件内容读取
            case CONTENT:
                // 同下，从文件内容读取
                // 单个包中有多个文件
                while (currentPos < data.length) {
                    try {
                        currentPos = readFileInfo(data, readFileData(data, currentPos));
                    } catch (IOException e) {
                        throw new UnaryIoException("ERROR 0x01 : IO error.", e);
                    }
                }
                break;
            default:
                throw new IllegalStateException("Program error. Unexpected state " + stage);
        }
        if (fm.taskFinished(taskId)) {
            try {
                this.close();
            } catch (Exception e) {
                logger.error("close resource error", e);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 读取文件信息
     *
     * @param data       待读取数据
     * @param currentPos 偏移量
     * @return 读取完毕后的位置
     */
    private int readFileInfo(byte[] data, int currentPos) throws IOException {
        logger.debug("CurrentPos when read file info:" + currentPos);
        if (currentPos >= data.length) {
            return currentPos;
        }
        if (ReadProcess.FILE_INFO_DONE.equals(stage)
                || ReadProcess.CONTENT.equals(stage)) {
            throw new IllegalStateException("Program error. Illegal stage :" + stage);
        }
        // 当前包的可读字节数
        int packRemaining = data.length - currentPos;
        // 文件数据读完或者是文件信息长度未读完，都应该从这里执行
        if (ReadProcess.FILE_INFO_LENGTH.equals(stage)
                || ReadProcess.CONTENT_DONE.equals(stage)) {
            int fileInfoLenRemaining = fileInfoLenBuf.remaining();
            // 可读入文件信息长度数组的字节数
            int readableLen;
            if (packRemaining < fileInfoLenRemaining) {
                stage = ReadProcess.FILE_INFO_LENGTH;
                readableLen = packRemaining;
            } else {
                stage = ReadProcess.FILE_INFO_LENGTH_DONE;
                readableLen = fileInfoLenRemaining;
            }
            fileInfoLenBuf.put(data, currentPos, readableLen);
            currentPos += readableLen;
            packRemaining -= readableLen;
            if (packRemaining < 1) {
                if (currentPos != data.length) {
                    throw new IllegalStateException("Program error. currentPos :"
                            + currentPos + ", data size:" + data.length);
                }
                return data.length;
            }
        }

        if (ReadProcess.FILE_INFO_LENGTH_DONE.equals(stage)) {
            try {
                fileInfo = ByteBuffer.allocate(CommonUtils.byteArrayToInt(fileInfoLenBuf.array(), 0));
            } catch (Throwable throwable) {
                throw new IllegalStateException("packIndex:" + packIndex + " current file:" + currentFile.getFullName(), throwable);
            }
            fileInfoLenBuf.clear();
        }

        if (fileInfo.remaining() < 1) {
            throw new IllegalStateException("Program error.");
        }

        // 文件所需信息字节数 大于 当前包剩余有效数据的字节数
        if (fileInfo.remaining() > packRemaining) {
            stage = ReadProcess.FILE_INFO;
            fileInfo.put(data, currentPos, packRemaining);
            if (currentPos + packRemaining != data.length) {
                throw new IllegalStateException("Program error. currentPos:"
                        + currentPos + ",packRemaining:" + packRemaining + ", data.length:" + data.length);
            }
            return data.length;
        } else {
            stage = ReadProcess.FILE_INFO_DONE;
            int readSize = fileInfo.remaining();
            fileInfo.put(data, currentPos, readSize);
            currentPos += readSize;

            // 文件信息读取完毕，序列化文件信息。准备文件写入数据
            initCopyFile(data.length - currentPos);
            return currentPos;
        }
    }

    /**
     * 初始化待拷贝文件，计算出文件内容在传输过程中的包分布情况，并持久化。
     *
     * @param remainingSize 当前包的可读取字节数
     */
    private void initCopyFile(int remainingSize) throws IOException {
        logger.debug("A file info json start transfer to FileInfo");
        try {
            currentFile = JSON.parseObject(fileInfo.array(), FileInfoDO.class);
            beginPackIndex = endPackIndex = packIndex;
            long fileSizeExCurPack = currentFile.getFileSize() - remainingSize;
            currentFile.setBeginPackIndex(packIndex);
            if (fileSizeExCurPack > 0) {
                endPackIndex += (int) (fileSizeExCurPack / (PACK_SIZE - HEAD_LENGTH)) + 1;
                endPackSize = (int) (fileSizeExCurPack % (PACK_SIZE - HEAD_LENGTH));
                beginPackSize = remainingSize;
            } else {
                beginPackSize = endPackSize = (int) currentFile.getFileSize();
            }
            currentFile.setFinishPackIndex(endPackIndex);
            currentFile.setState(FileInfoDO.STATE.SYNCING);
            fm.save(currentFile);
        } catch (Exception e) {
            throw new IllegalStateException("ERROR 0x04 : Failed extract FileInfo from Json", e);
        }
        output.openFile(PathMapperUtil.sourcePathMapper(backupPath, currentFile.getFullName()));
    }

    /**
     * 读取文件数据
     *
     * @param data       待读取数据
     * @param currentPos 偏移量
     * @return 读取完毕后的位置
     */
    private int readFileData(byte[] data, int currentPos) throws IOException {
        logger.debug("currentPos when read file data:" + currentPos);
        if (currentPos >= data.length) {
            return currentPos;
        }
        if (!(ReadProcess.FILE_INFO_DONE.equals(stage)
                || ReadProcess.CONTENT.equals(stage))) {
            throw new IllegalStateException("Program error. Illegal stage :" + stage);
        }
        // size 是当前包有效的文件数据长度，是每个文件读取头信息的时候初始化的。 currentPos 当前包有效数据的位置
        int size;
        if (packIndex == beginPackIndex) {
            size = beginPackSize;
        } else if (packIndex == endPackIndex) {
            size = endPackSize;
        } else if (packIndex > beginPackIndex && packIndex < endPackIndex) {
            size = PACK_SIZE - HEAD_LENGTH;
        } else {
            throw new IllegalStateException("Program error");
        }

        if (currentPos >= data.length) {
            return currentPos;
        } else if (currentPos + size > data.length) {
            throw new IllegalStateException("Program error. Package valid data length sum error. PackIndex "
                    + packIndex + ", data size " + size);
        }
        output.write(data, currentPos, size);
        currentPos += size;
        if (packIndex >= endPackIndex) {
            beginPackIndex = endPackIndex = beginPackSize = endPackSize = 0;
            currentFile.setState(FileInfoDO.STATE.SYNCED);
            fm.save(currentFile);
            stage = ReadProcess.CONTENT_DONE;
        } else {
            stage = ReadProcess.CONTENT;
            if (currentPos < data.length) {
                throw new IllegalStateException("Program error. CurrentPos:"
                        + currentPos + ", data.length:" + data.length
                        + " suppose write " + size + " bytes. Pack index:" + packIndex);
            }
        }
        return currentPos;
    }

    @Override
    public PackType getPackType() {
        return PackType.SYNC_ALL_JAVA;
    }

    @Override
    public Resolver setTaskId(int taskId) {
        this.taskId = taskId;
        return this;
    }

    @Override
    public void close() throws Exception {
        if (output != null) {
            output.close();
        }
    }

    private enum ReadProcess {
        // 文件信息长度
        FILE_INFO_LENGTH,
        // 文件信息长度已读完
        FILE_INFO_LENGTH_DONE,
        // 文件信息未读完
        FILE_INFO,
        // 文件信息已读完
        FILE_INFO_DONE,
        // 文件数据未读完
        CONTENT,
        // 文件数据已读完
        CONTENT_DONE,
    }
}
