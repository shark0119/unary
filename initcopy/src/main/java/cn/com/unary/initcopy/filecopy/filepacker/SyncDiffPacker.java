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
}
