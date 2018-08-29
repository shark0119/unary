package cn.com.unary.initcopy.filecopy.filepacker;

import api.UnaryTClient;
import cn.com.unary.initcopy.grpc.entity.DiffFileInfo;

import java.util.List;

/**
 * 差异复制文件打包和解析
 *
 * @author shark
 */
public interface SyncDiffPacker extends Packer {
    /**
     * 设置目标端校验数据，为rsync做准备
     *
     * @param diffFileInfo 目标端文件的校验数据
     * @return 当前对象
     */
    SyncDiffPacker setFileDiffInfos(List<DiffFileInfo> diffFileInfo);

    /**
     * 设置任务 Id
     *
     * @param taskId 任务 Id
     * @return 当前对象
     * @see {@link Packer#setTaskId(int)}
     */
    @Override
    SyncDiffPacker setTaskId(int taskId);

    /**
     * 设置传输模块
     *
     * @param unaryTClient 传输模块客户端
     * @return 当前对象
     * @see {@link Packer#setTransfer(UnaryTClient)}
     */
    @Override
    SyncDiffPacker setTransfer(UnaryTClient unaryTClient);
}
