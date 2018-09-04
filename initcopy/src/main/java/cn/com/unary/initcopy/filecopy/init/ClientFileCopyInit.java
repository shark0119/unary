package cn.com.unary.initcopy.filecopy.init;

import api.UnaryTClient;
import cn.com.unary.initcopy.InitCopyContext;
import cn.com.unary.initcopy.common.AbstractLoggable;
import cn.com.unary.initcopy.common.BeanConverter;
import cn.com.unary.initcopy.dao.FileManager;
import cn.com.unary.initcopy.entity.BaseFileInfoDO;
import cn.com.unary.initcopy.entity.FileInfoDO;
import cn.com.unary.initcopy.entity.SyncTaskDO;
import cn.com.unary.initcopy.exception.InfoPersistenceException;
import cn.com.unary.initcopy.grpc.client.ControlTaskGrpcClient;
import cn.com.unary.initcopy.grpc.constant.SyncType;
import cn.com.unary.initcopy.grpc.entity.BaseFileInfo;
import cn.com.unary.initcopy.grpc.entity.ClientInitReq;
import cn.com.unary.initcopy.grpc.entity.DiffFileInfo;
import cn.com.unary.initcopy.grpc.entity.ServerInitResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
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
    @Qualifier("clientFM")
    protected FileManager fm;

    @Autowired
    protected InitCopyContext context;

    /**
     * 文件复制前的初始化工作
     * 1.包括设置加密压缩等选项；
     * 2.读取本地文件，将必要信息读出并持久化；
     * 3.在差异复制时向目标端发送同步文件信息列表来核对待同步文件；
     * 差异复制时需要等待服务端确认核对后再返回
     *
     * @param syncTask      同步任务的相关配置信息
     * @param diffFileInfos 差异文件数据，由初始化模块接收到后，传入其中
     * @return 待同步的文件Id集合
     * @throws IOException      IO错误
     * @throws RuntimeException Grpc 服务调用错误。
     */
    public List<String> startInit(UnaryTClient client,
                                  final SyncTaskDO syncTask,
                                  final List<DiffFileInfo> diffFileInfos)
            throws IOException, InfoPersistenceException {
        logger.debug("Start init.");
        client.setSpeedLimit(syncTask.getSpeedLimit());
        client.setCompressType(syncTask.getCompressType());
        client.setEncryptType(syncTask.getEncryptType());

        logger.debug("Set transfer option done. Start to traversing files.");
        List<FileInfoDO> syncFiles = traversingFiles(syncTask.getFiles());
        List<String> syncFileIds = new ArrayList<>();
        int taskId = syncTask.getTaskId();
        for (FileInfoDO fi : syncFiles) {
            syncFileIds.add(fi.getFileId());
            fi.setTaskId(taskId);
            fm.save(fi);
        }
        logger.debug("We got " + syncFiles.size() + " files and " + syncFileIds.size()
                + " file id from specified local directory in task " + syncTask.getTaskId());

        // TODO change to grpc client and server
        ControlTaskGrpcClient controlTaskGrpcClient =
                new ControlTaskGrpcClient();

        long totalSize = 0L;
        List<BaseFileInfo> bfis = new ArrayList<>(syncFiles.size());
        for (BaseFileInfoDO bfi : syncFiles) {
            totalSize += bfi.getFileSize();
            bfis.add(BeanConverter.convert(bfi, BaseFileInfo.class));
        }
        ClientInitReq.Builder builder = ClientInitReq.newBuilder()
                .setTargetDir(syncTask.getTargetDir())
                .setTaskId(taskId)
                .setTotalSize(totalSize)
                .addAllBaseFileInfos(bfis);

        logger.debug("Try to init according sync type.");
        switch (syncTask.getSyncType()) {
            case SYNC_DIFF:
                syncFileIds = syncDiffInit(builder, controlTaskGrpcClient, diffFileInfos);
                break;
            // as default option
            case SYNC_ALL:
            default:
                syncAllInit(builder, controlTaskGrpcClient);
                break;
        }
        fm.saveTask(syncTask);
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
        if (!resp.getReady()) {
            throw new IllegalStateException("ERROR 0x07 : Server intern Error." + resp.getMsg());
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
        if (!resp.getReady()) {
            throw new IllegalStateException("ERROR 0x07 : Server intern Error." + resp.getMsg());
        }
    }

    private List<FileInfoDO> traversingFiles(List<String> files) throws IOException {
        Path path;
        final List<FileInfoDO> fileInfos = new ArrayList<>();
        for (String fileName : files) {
            path = Paths.get(fileName);
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                        throws IOException {
                    if (dir.toFile().list().length == -1) {
                        FileInfoDO fileInfo = new FileInfoDO(takeFromFile(dir.toFile()));
                        fileInfos.add(fileInfo);
                    }
                    // TODO take dir in
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                        throws IOException {
                    FileInfoDO fileInfo = new FileInfoDO(takeFromFile(file.toFile()));
                    fileInfos.add(fileInfo);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
        return fileInfos;
    }

    private BaseFileInfoDO takeFromFile(File file) {
        BaseFileInfoDO bfi = new BaseFileInfoDO();
        bfi.setFileSize(file.length());
        bfi.setFileId(UUID.randomUUID().toString());
        bfi.setModifyTime(file.lastModified());
        bfi.setFullName(file.getPath());
        return bfi;
    }
}
