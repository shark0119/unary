package cn.com.unary.initcopy.common.utils;

import cn.com.unary.initcopy.entity.BaseFileInfoDO;
import cn.com.unary.initcopy.entity.Constants;
import cn.com.unary.initcopy.entity.FileInfoDO;
import cn.com.unary.initcopy.entity.SyncTaskDO;
import cn.com.unary.initcopy.entity.TransmitParams;
import cn.com.unary.initcopy.grpc.entity.BaseFileInfo;
import cn.com.unary.initcopy.grpc.entity.ClientInitReq;
import cn.com.unary.initcopy.grpc.entity.SyncTask;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 将实体和 SQL 互相转换
 * 将实体与 GRPC 实体互相转换
 *
 * @author Shark.Yin
 * @since 1.0
 */
public class BeanExactUtil {

    private static final String ZERO_STR = "0";

    public static List<FileInfoDO> deSerFromResult(ResultSet rSet) throws SQLException {
        List<FileInfoDO> fis = new ArrayList<>();
        FileInfoDO fi ;
        while (rSet.next()) {
            fi = new FileInfoDO();
            fi.setFileId(rSet.getString("FILE_ID"));
            fi.setModifyTime(rSet.getLong("MODIFY_TIME"));
            fi.setFileSize(rSet.getLong("FILE_SIZE"));
            fi.setTaskId(rSet.getString("TASK_ID"));
            fis.add(fi);
        }
        return fis;
    }
    public static String serToSql (FileInfoDO fi) {
        StringBuilder sb = new StringBuilder();

        return sb.toString();
    }

    /**
     * 将初始化时发送的文件信息转化为文件信息实体。FileInfo 包含了文件除内容以外所有的信息。
     * 但本函数不初始化以下属性：
     *
     * {@link FileInfoDO#beginPackIndex}
     * {@link FileInfoDO#finishPackIndex}
     *
     * @param bfi 初始化时需要的文件基础信息
     * @return 文件的待同步信息
     * @throws IOException 由于读文件导致的 IO 异常
     */
    public static FileInfoDO readFromBaseFileInfo (BaseFileInfoDO bfi) throws IOException {
        FileInfoDO fi = new FileInfoDO(bfi);
        Path path = Paths.get(bfi.getFullName());
        BasicFileAttributes bfa =
                Files.getFileAttributeView(path, BasicFileAttributeView.class)
                        .readAttributes();
        if (bfa.isSymbolicLink()) {
            fi.setFileType(Constants.FileType.SYMBOLIC_LINK);
        } else if (bfa.isDirectory()) {
            fi.setFileType(Constants.FileType.DIR);
        }else if (bfa.isRegularFile()) {
            fi.setFileType(Constants.FileType.REGULAR_FILE);
        } else if (bfa.isOther()) {
            fi.setFileType(Constants.FileType.OTHER);
        }
        FileOwnerAttributeView ownerView = Files.getFileAttributeView(
                path, FileOwnerAttributeView.class);
        fi.setState(FileInfoDO.STATE.WAIT);
        fi.setAttr(FileInfoDO.newFileAttr());
        fi.getAttr().setOwner(ownerView.getOwner().getName());
        fi.getAttr().setHidden(path.toFile().isHidden());
        return fi;
    }

    public static List<BaseFileInfoDO> takeFromGrpc(List<BaseFileInfo> bfiList) {
        List<BaseFileInfoDO> bfiDOList = new ArrayList<>(bfiList.size());
        for (BaseFileInfo bfi : bfiList) {
            bfiDOList.add(takeFromGrpc(bfi));
        }
        return bfiDOList;
    }

    public static List<BaseFileInfo> takeFromDO(List<? extends BaseFileInfoDO> bfiDOList) {
        List<BaseFileInfo> bfiList = new ArrayList<>(bfiDOList.size());
        for (BaseFileInfoDO bfiDO : bfiDOList) {
            bfiList.add(takeFromDO(bfiDO));
        }
        return bfiList;
    }

    private static BaseFileInfo takeFromDO(BaseFileInfoDO bfiDO) {
        BaseFileInfo.Builder builder = BaseFileInfo.newBuilder();
        builder.setFileId(bfiDO.getFileId())
                .setFullName(bfiDO.getFullName())
                .setFileSize(bfiDO.getFileSize())
                .setModifyTime(bfiDO.getModifyTime())
                .setCheckSum(bfiDO.getCheckSum() == null ? "" : bfiDO.getCheckSum())
                .setBackUpPath(bfiDO.getBackUpPath() == null ? "" : bfiDO.getBackUpPath());
        return builder.build();
    }

    private static BaseFileInfoDO takeFromGrpc(BaseFileInfo bfi) {
        BaseFileInfoDO bfiDO = new BaseFileInfoDO();
        bfiDO.setFileId(bfi.getFileId());
        bfiDO.setFileSize(bfi.getFileSize());
        bfiDO.setFullName(bfi.getFullName());
        bfiDO.setModifyTime(bfi.getModifyTime());
        bfiDO.setCheckSum(bfi.getCheckSum());
        bfiDO.setBackUpPath(bfi.getBackUpPath());
        return bfiDO;
    }

    public static SyncTaskDO takeFromGrpc(SyncTask syncTask, ClientInitReq request) {
        SyncTaskDO taskDO = new SyncTaskDO();
        taskDO.setSyncedSize(new BigDecimal(ZERO_STR));
        if (taskDO.getSyncedSize().equals(taskDO.getTotalSize())) {
            throw new IllegalArgumentException(String.format("totalSize can't be null %s", request.getTotalSize()));
        }
        if (syncTask != null) {
            taskDO.setTaskId(syncTask.getTaskId());
            taskDO.setSpeedLimit(syncTask.getSpeedLimit());
            taskDO.setIp(syncTask.getTargetInfo().getIp());
            taskDO.setEncryptType(syncTask.getEncryptType());
            taskDO.setCompressType(syncTask.getCompressType());
            taskDO.setGrpcPort(syncTask.getTargetInfo().getGrpcPort());
            taskDO.setTransferPort(syncTask.getTargetInfo().getTransferPort());
        } else {
            taskDO.setTaskId(request.getTaskId());
        }
        if (request != null) {
            taskDO.setTotalSize(new BigDecimal(request.getTotalSize()));
            Map<String, Long> fileSizeMap = new HashMap<>(request.getBaseFileInfosCount());
            for (BaseFileInfo bfi : request.getBaseFileInfosList()) {
                fileSizeMap.put(bfi.getFileId(), bfi.getFileSize());
            }
            taskDO.setFileSizeMap(fileSizeMap);
        }
        return taskDO;
    }

    public static TransmitParams takeTransmitParamFromTaskDO(SyncTaskDO taskDO) {
        TransmitParams params = new TransmitParams();
        params.setSpeedLimit(taskDO.getSpeedLimit());
        params.setCompressType(taskDO.getCompressType());
        params.setEncryptType(taskDO.getEncryptType());
        params.setIp(taskDO.getIp());
        params.setPort(taskDO.getTransferPort());
        return params;
    }
}
