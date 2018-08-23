package cn.com.unary.initcopy.filecopy.init;

import api.UnaryTClient;
import cn.com.unary.initcopy.grpc.entity.DiffFileInfo;
import cn.com.unary.initcopy.grpc.entity.SyncTask;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 负责初始化复制文件复制模块的初始化
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Component("clientFileCopyInit")
public class ClientFileCopyInit {

    protected UnaryTClient utc;

    /**
     * 文件复制前的初始化工作
     * 1.包括设置加密压缩等选项；
     * 2.读取本地文件，将必要信息读出并持久化；
     * 3.在差异复制时向目标端发送同步文件信息列表来核对待同步文件；
     * 4.在准备就绪后，通知文件打包开始文件读取打包发送；
     * 差异复制时需要等待服务端确认核对后再返回
     *
     * @param syncTask 同步任务的相关配置信息
     * @param diffFileInfos 差异文件数据，由初始化模块接收到后，传入其中
     * @return 待同步的文件Id集合
     * @throws Exception 初始化失败
     */
    public List<String> startInit(SyncTask syncTask, final List<DiffFileInfo> diffFileInfos) throws Exception {
        // TODO 设置加密压缩等选项
        switch (syncTask.getSyncType()) {
            case SYNC_DIFF:

            case SYNC_ALL:
                default:

        }
        return null;
    }

    public void setUtc(UnaryTClient utc) {
        this.utc = utc;
    }

}
