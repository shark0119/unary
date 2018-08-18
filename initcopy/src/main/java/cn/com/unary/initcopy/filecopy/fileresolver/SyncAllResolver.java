package cn.com.unary.initcopy.filecopy.fileresolver;

import cn.com.unary.initcopy.dao.FileManager;
import cn.com.unary.initcopy.entity.Constants.PackType;
import cn.com.unary.initcopy.entity.FileInfo;
import cn.com.unary.initcopy.filecopy.filepacker.SyncAllPacker;
import cn.com.unary.initcopy.filecopy.io.AbstractFileOutput;
import cn.com.unary.initcopy.utils.AbstractLogable;
import cn.com.unary.initcopy.utils.CommonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 全复制时使用的文件解析器，标识为
 * PackType.SYNC_ALL_JAVA
 *
 * @author shark
 */
@Component("syncAllResolver")
public class SyncAllResolver extends AbstractLogable implements Resolver {

    public static final int PACK_SIZE = SyncAllPacker.PACK_SIZE;
    public static final int HEAD_LENGTH = SyncAllPacker.HEAD_LENGTH;
    public static final int FILE_INFO_LENGTH = SyncAllPacker.FILE_INFO_LENGTH;

    @Autowired
    private AbstractFileOutput output;
    @Autowired
    @Qualifier("sqliteFileManager")
    protected FileManager fm;

    // key：包序号 value：包文件数据长度
    private final Map<Integer, Integer> packValidDataSize = new HashMap<>();
    // 已经读取的包序号
    private final List<Integer> readPackIndex = new ArrayList<>();
    private final ObjectMapper mapper = new ObjectMapper();
    private ReadProcess stage = ReadProcess.CONTENT_DONE;
    private ByteBuffer fileInfo;
    // 当前读取的包序号
    private int packIndex;
    // 文件信息长度，当被截断时，暂存此处
    private final byte[] fileInfoLen = new byte[FILE_INFO_LENGTH];
    private int fileInfoLenPos = 0;

    @Override
    public void process(byte[] data) {
        packIndex = CommonUtils.byteArrayToInt(data);
        readPackIndex.add(packIndex);
        // get pack info
        if (!PackType.valueOf(data[4]).equals(getPackType())) {
            throw new IllegalStateException("ERROR 0x05 : Bad data pack format. Wrong Resolver");
        }
        int currentPos = HEAD_LENGTH;
        // 判断上次读取的位置
        switch (stage) {
            case FILE_INFO_LENGTH: // 同下，从文件头读取
            case FILE_INFO_LENGTH_DONE: // 同下，从文件头读取
            case FILE_INFO: // 同下，从文件头读取
            case CONTENT_DONE:  // 从文件头读取
                // 单个包中有多个文件
                while (currentPos < data.length) {
                    currentPos = readFileData(data, readFileInfo(data, currentPos));
                }
                break;
            case FILE_INFO_DONE:    // 同下，从文件内容读取
            case CONTENT:   // 同下，从文件内容读取
                // 单个包中有多个文件
                while (currentPos < data.length) {
                    currentPos = readFileInfo(data, readFileData(data, currentPos));
                }
                break;
            default:
                throw new IllegalStateException("Program error. Unexpected state " + stage);
        }
    }

    @Override
    public PackType getPackType() {
        return PackType.SYNC_ALL_JAVA;
    }

    @Override
    public void close() throws Exception {
        if (output != null)
            output.close();
    }

    /**
     * 读取文件信息
     *
     * @param data       待读取数据
     * @param currentPos 偏移量
     * @return 读取完毕后的位置
     */
    private int readFileInfo(byte[] data, int currentPos) {
        if (currentPos >= data.length) {
            return currentPos;
        }
        int packRemaining = data.length - currentPos;   // 当前包的可读字节数
        if (ReadProcess.FILE_INFO_LENGTH.equals(stage)) {
            int fileInfoLenRemaining = fileInfoLen.length - fileInfoLenPos; // 文件信息长度所需要的字节个数
            int readableLen;    // 可读入文件信息长度数组的字节数
            if (packRemaining < fileInfoLenRemaining) {
                stage = ReadProcess.FILE_INFO_LENGTH;
                readableLen = packRemaining;
            } else {
                stage = ReadProcess.FILE_INFO_LENGTH_DONE;
                readableLen = fileInfoLenRemaining;
            }
            for (int i = 0; i < readableLen; i++, fileInfoLenPos++, currentPos++, packRemaining--) {
                fileInfoLen[fileInfoLenPos] = data[currentPos];
            }
            if (packRemaining < 1) {
                return currentPos;
            }
        }

        if (ReadProcess.FILE_INFO_LENGTH_DONE.equals(stage)) {
            fileInfo = ByteBuffer.allocate(CommonUtils.byteArrayToInt(fileInfoLen));
            fileInfoLenPos = 0;
        }

        // 文件信息还需要的字节数
        int fileInfoRemaining = fileInfo.remaining();
        int readSize = fileInfoRemaining > packRemaining ? packRemaining : fileInfoRemaining;
        fileInfo.put(data, currentPos, readSize);
        if (fileInfo.hasRemaining()) {
            stage = ReadProcess.FILE_INFO;
        } else {
            stage = ReadProcess.FILE_INFO_DONE;
            initCopyFile(data.length - currentPos - readSize);
        }
        return currentPos + readSize;
    }

    /**
     * 初始化待拷贝文件
     *
     * @param remainingSize 当前包的可读取字节数
     */
    private void initCopyFile(int remainingSize) {
        try {
            FileInfo fileInfo = mapper.readValue(this.fileInfo.array(), FileInfo.class);
            packValidDataSize.put(packIndex, remainingSize);
            long fileSize = fileInfo.getFileSize();
            fileInfo.setBeginPackIndex(packIndex);
            if (fileSize > remainingSize) {
                fileSize -= remainingSize;
                int packSize = (int) (fileSize % (PACK_SIZE - HEAD_LENGTH));
                fileSize -= packSize * (PACK_SIZE - HEAD_LENGTH);
                for (int i = 1; i < packSize; i++) {
                    packValidDataSize.put(packIndex + i, PACK_SIZE - HEAD_LENGTH);
                }
                packValidDataSize.put(packIndex + packSize, (int) fileSize);
                fileInfo.setFinishPackIndex(packIndex + packSize);
            } else {
                fileInfo.setFinishPackIndex(packIndex);
            }
            fm.save(fileInfo);
        } catch (IOException e) {
            throw new IllegalStateException("ERROR 0x04 : Failed extract FileInfo from Json", e);
        }
    }

    /**
     * 读取文件数据
     *
     * @param data       待读取数据
     * @param currentPos 偏移量
     * @return 读取完毕后的位置
     */
    private int readFileData(byte[] data, int currentPos) {
        // size 是当前包有效的文件数据长度，是每个文件读取头信息的时候初始化的。 currentPos 是包头的长度
        int size = packValidDataSize.get(packIndex);
        if (currentPos >= data.length) {
            return currentPos;
        } else if (currentPos + size > data.length) {
            logger.error("Program error. Package valid data length sum error. PackIndex {1}, data size {2}", packIndex, size);
            throw new IllegalStateException("Program error. Package valid data length sum error.");
        }
        output.write(data, currentPos, size);
        if (readPackIndex.size() >= packValidDataSize.keySet().size()) {
            packValidDataSize.clear();
            readPackIndex.clear();
            stage = ReadProcess.CONTENT_DONE;
        } else {
            stage = ReadProcess.CONTENT;
        }
        return currentPos + size;
    }

    private enum ReadProcess {
        FILE_INFO_LENGTH, // 文件信息长度
        FILE_INFO_LENGTH_DONE, // 文件信息长度已读完
        FILE_INFO,    // 文件信息未读完
        FILE_INFO_DONE,    // 文件信息已读完
        CONTENT,    // 文件数据未读完
        CONTENT_DONE, // 文件数据已读完
    }
}
