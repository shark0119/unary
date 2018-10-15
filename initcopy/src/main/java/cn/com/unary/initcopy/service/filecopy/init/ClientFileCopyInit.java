package cn.com.unary.initcopy.service.filecopy.init;

import api.UnaryTransferClient;
import cn.com.unary.initcopy.InitCopyContext;
import cn.com.unary.initcopy.common.AbstractLoggable;
import cn.com.unary.initcopy.common.utils.BeanExactUtil;
import cn.com.unary.initcopy.dao.FileManager;
import cn.com.unary.initcopy.entity.BaseFileInfoDO;
import cn.com.unary.initcopy.entity.FileInfoDO;
import cn.com.unary.initcopy.exception.InfoPersistenceException;
import cn.com.unary.initcopy.grpc.constant.SyncType;
import cn.com.unary.initcopy.grpc.entity.BaseFileInfo;
import cn.com.unary.initcopy.grpc.entity.ClientInitReq;
import cn.com.unary.initcopy.grpc.entity.DiffFileInfo;
import cn.com.unary.initcopy.grpc.entity.ServerInitResp;
import cn.com.unary.initcopy.grpc.entity.SyncTask;
import cn.com.unary.initcopy.grpc.service.ControlTaskGrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 负责初始化复制文件复制模块的初始化
 * 线程安全
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Component("ClientFileCopyInit")
@Scope("singleton")
public class ClientFileCopyInit extends AbstractLoggable {

    @Autowired
    protected InitCopyContext context;
    @Autowired
    @Qualifier("clientFM")
    private FileManager fm;

    /**
     * 文件复制前的初始化工作
     * 1.包括设置加密压缩等选项；
     * 2.读取本地文件，将必要信息读出并持久化；
     * 3.在差异复制时向目标端发送同步文件信息列表来核对待同步文件；
     * 差异复制时需要等待服务端确认核对后再返回
     *
     * @param task          同步任务的相关配置信息
     * @param diffFileInfos 差异文件数据，由初始化模块接收到后，传入其中
     * @return 待同步的文件Id集合
     * @throws IOException      IO错误
     * @throws RuntimeException Grpc 服务调用错误。
     */
    public List<String> startInit(UnaryTransferClient transferClient, final SyncTask task,
                                  final List<DiffFileInfo> diffFileInfos)
            throws IOException, InfoPersistenceException {
        logger.debug("Start init.");
        transferClient.setSpeedLimit(task.getSpeedLimit());
        transferClient.setCompressType(task.getCompressType());
        transferClient.setEncryptType(task.getEncryptType());

        logger.debug("Set transfer option done. Start to traversing files.");
        List<FileInfoDO> syncFiles = traversingFiles(task.getFilesList(), task.getTargetDirsList());
        List<String> syncFileIds = new ArrayList<>(syncFiles.size());
        String taskId = task.getTaskId();
        for (FileInfoDO fi : syncFiles) {
            syncFileIds.add(fi.getFileId());
            fi.setTaskId(taskId);
            fm.save(fi);
        }
        logger.debug(String.format("We got %d files and %d file id from specified local directory in task %s."
                , syncFiles.size(), syncFileIds.size(), task.getTaskId()));

        // @Cleanup
        ControlTaskGrpcClient grpcClient = new ControlTaskGrpcClient(
                task.getTargetInfo().getIp(),
                task.getTargetInfo().getGrpcPort());
        List<BaseFileInfo> bfiList = BeanExactUtil.takeFromDO(syncFiles);
        BigDecimal totalSize = new BigDecimal("0");

        for (BaseFileInfoDO bfiDO : syncFiles) {
            totalSize = totalSize.add(new BigDecimal(bfiDO.getFileSize().toString()));
        }

        ClientInitReq.Builder builder = ClientInitReq.newBuilder()
                .setTaskId(taskId).setTotalSize(totalSize.toString()).addAllBaseFileInfos(bfiList);

        if (task.getTargetDirsCount() == 1) {
            builder.setBackUpPath(task.getTargetDirs(0));
        }

        logger.debug("Try to init according sync type.");
        switch (task.getSyncType()) {
            case SYNC_DIFF:
                syncFileIds = syncDiffInit(builder, grpcClient, diffFileInfos);
                break;
            case SYNC_ALL:
                // sync all as default option
            default:
                syncAllInit(builder, grpcClient);
                break;
        }

        fm.saveTask(BeanExactUtil.takeFromGrpc(task, builder.build()));
        return syncFileIds;
    }

    /**
     * 差异复制向目标端发送初始化请求
     *
     * @param builder               初始化请求的 Builder
     * @param controlTaskGrpcClient 和控制任务 GRPC 通讯的客户端
     * @param diffFileInfos         从目标端收到的文件同步差异信息数据
     * @return 目标端确认后的待同步文件列表
     */
    private List<String> syncDiffInit(ClientInitReq.Builder builder,
                                      ControlTaskGrpcClient controlTaskGrpcClient,
                                      List<DiffFileInfo> diffFileInfos) {
        builder.setSyncType(SyncType.SYNC_DIFF);
        ServerInitResp resp = controlTaskGrpcClient.invokeGrpcInit(builder.build());
        if (!resp.getExecResult().getHealthy()) {
            throw new IllegalStateException("ERROR 0x07 : Server intern Error." + resp.getExecResult().getMsg());
        }
        diffFileInfos.addAll(resp.getDiffFileInfosList());

        List<String> list = new ArrayList<>();
        for (DiffFileInfo dfi : diffFileInfos) {
            list.add(dfi.getFileId());
        }
        return list;
    }

    private void syncAllInit(ClientInitReq.Builder builder, ControlTaskGrpcClient controlTaskGrpcClient) {
        builder.setSyncType(SyncType.SYNC_ALL);
        ServerInitResp resp = controlTaskGrpcClient.invokeGrpcInit(builder.build());
        if (!resp.getExecResult().getHealthy()) {
            throw new IllegalStateException("ERROR 0x07 : Server intern Error." + resp.getExecResult().getMsg());
        }
    }

    private List<FileInfoDO> traversingFiles(List<String> files, List<String> backUpPaths) throws IOException {
        Path path;
        String backUpPath = "";
        boolean isMultiMap = backUpPaths.size() > 1;
        final List<FileInfoDO> fileInfos = new ArrayList<>();

        for (int i = 0; i < files.size(); i++) {
            if (isMultiMap) {
                backUpPath = backUpPaths.get(i);
            }
            final String tempBackUpPath = backUpPath;
            path = Paths.get(files.get(i));
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    if (dir.toFile().list().length == 0) {
                        FileInfoDO fileInfo = takeFromFile(dir.toFile());
                        fileInfo.setBackUpPath(tempBackUpPath);
                        fileInfos.add(fileInfo);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    FileInfoDO fileInfo = takeFromFile(file.toFile());
                    fileInfo.setBackUpPath(tempBackUpPath);
                    fileInfos.add(fileInfo);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
        return fileInfos;
    }

    private FileInfoDO takeFromFile(File file) {
        FileInfoDO fiDO = new FileInfoDO();
        fiDO.setFileSize(file.length());
        fiDO.setFileId(UUID.randomUUID().toString());
        fiDO.setModifyTime(file.lastModified());
        fiDO.setFullName(file.getPath());
        fiDO.setState(FileInfoDO.STATE.WAIT);
        return fiDO;
    }
}
