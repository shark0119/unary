package cn.com.unary.initcopy.filecopy.init;

import api.UnaryTClient;
import cn.com.unary.initcopy.InitCopyContext;
import cn.com.unary.initcopy.dao.FileManager;
import cn.com.unary.initcopy.entity.BaseFileInfo;
import cn.com.unary.initcopy.entity.FileInfo;
import cn.com.unary.initcopy.grpc.client.ControlTaskGrpcClient;
import cn.com.unary.initcopy.grpc.constant.SyncType;
import cn.com.unary.initcopy.grpc.entity.ClientInitReq;
import cn.com.unary.initcopy.grpc.entity.DiffFileInfo;
import cn.com.unary.initcopy.grpc.entity.ServerInitResp;
import cn.com.unary.initcopy.grpc.entity.SyncTask;
import cn.com.unary.initcopy.grpc.linker.ControlTaskGrpcLinker;
import cn.com.unary.initcopy.utils.AbstractLogable;
import cn.com.unary.initcopy.utils.BeanConvertUtil;
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
public class ClientFileCopyInit extends AbstractLogable {

    @Autowired
    @Qualifier("clientFM")
    protected FileManager fileManager;

    @Autowired
    protected InitCopyContext context;

    @Autowired
    protected ControlTaskGrpcLinker linker;

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
                                  final SyncTask syncTask,
                                  final List<DiffFileInfo> diffFileInfos)
            throws IOException {
        logger.debug("Start init.");
        // TODO 设置加密压缩等选项

        logger.debug("Set transfer option done. Start to traversing files.");
        List<BaseFileInfo> syncFiles = traversingFiles(syncTask.getFileList());
        List<String> syncFileIds = new ArrayList<>();
        for (BaseFileInfo fi : syncFiles) {
            syncFileIds.add(fi.getId());
        }

        logger.debug("Try to init according sync type.");
        ControlTaskGrpcClient controlTaskGrpcClient =
                new ControlTaskGrpcClient(syncTask.getTargetInfo().getIp(),
                        InitCopyContext.CONTROL_TASK_GRPC_PORT);
        controlTaskGrpcClient.setLinker (linker);

        long totalSize = 0l;
        for (BaseFileInfo bfi : syncFiles) {
            totalSize += bfi.getFileSize();
        }
        ClientInitReq.Builder builder = ClientInitReq.newBuilder()
                .setTargetDir(syncTask.getTargetDir())
                .setTaskId(syncTask.getTaskId())
                .setTotalSize(totalSize);

        switch (syncTask.getSyncType()) {
            case SYNC_DIFF:
                syncFileIds = syncDiffInit(builder, controlTaskGrpcClient, syncFiles, diffFileInfos);
                break;
            case SYNC_ALL: // as default option
            default:
                syncAllInit(builder, controlTaskGrpcClient);
                break;
        }
        return syncFileIds;
    }

    /**
     * 差异复制向目标端发送初始化请求
     *
     * @param builder 初始化请求的 Builder
     * @param controlTaskGrpcClient 和控制任务 GRPC 通讯的客户端
     * @param syncFiles 待同步的文件列表
     * @param diffFileInfos 从目标端收到的文件同步差异信息数据
     * @return 目标端确认后的待同步文件列表
     */
    private List<String> syncDiffInit(ClientInitReq.Builder builder,
                                      ControlTaskGrpcClient controlTaskGrpcClient,
                                      List<BaseFileInfo> syncFiles,
                                      List<DiffFileInfo> diffFileInfos) {
        builder.addAllFileBaseInfos(BeanConvertUtil.takeToGrpc(syncFiles))
                .setSyncType(SyncType.SYNC_DIFF);
        ServerInitResp resp = controlTaskGrpcClient.invokeGrpcInit(builder.build());
        if (!resp.getReady()) {
            throw new IllegalStateException("ERROR 0x07 : Server intern Error." + resp.getMsg());
        }
        diffFileInfos.addAll(resp.getDiffFileInfoList());

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

    private List<BaseFileInfo> traversingFiles(List<String> files) throws IOException {
        Path path;
        final List<BaseFileInfo> fileInfos = new ArrayList<>();
        for (String fileName : files) {
            path = Paths.get(fileName);
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                        throws IOException {
                    if (dir.toFile().list().length == 0) {
                        FileInfo fileInfo = new FileInfo(takeFromFile(dir.toFile()));
                        fileInfos.add(fileInfo);
                        fileManager.save(fileInfo);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                        throws IOException {
                    FileInfo fileInfo = new FileInfo(takeFromFile(file.toFile()));
                    fileInfos.add(fileInfo);
                    fileManager.save(fileInfo);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
        return fileInfos;
    }

    private BaseFileInfo takeFromFile(File file) {
        BaseFileInfo bfi = new BaseFileInfo();
        bfi.setFileSize(file.length());
        bfi.setId(UUID.randomUUID().toString());
        bfi.setModifyTime(file.lastModified());
        bfi.setFullName(file.getPath());
        return bfi;
    }
}
