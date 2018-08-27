package cn.com.unary.initcopy.filecopy.init;

import cn.com.unary.initcopy.dao.FileManager;
import cn.com.unary.initcopy.entity.BaseFileInfo;
import cn.com.unary.initcopy.entity.FileInfo;
import cn.com.unary.initcopy.grpc.entity.ClientInitReq;
import cn.com.unary.initcopy.grpc.entity.DiffFileInfo;
import cn.com.unary.initcopy.grpc.entity.FileBaseInfo;
import cn.com.unary.initcopy.grpc.entity.ServerInitResp;
import cn.com.unary.initcopy.grpc.entity.SyncTask;
import cn.com.unary.initcopy.utils.BeanConvertUtil;
import io.grpc.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 目标端文件复制初始化
 * 线程安全
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Component("ServerFileCopyInit")
@Scope("singleton")
public class ServerFileCopyInit {
    @Autowired
    @Qualifier("serverFM")
    private FileManager fm;
    public ServerInitResp startInit(ClientInitReq req) throws Exception {
        ServerInitResp.Builder builder = ServerInitResp.newBuilder();
        builder.setReady(true).setMsg("success")
                .setTaskId(req.getTaskId());
        switch (req.getSyncType()) {
            case SYNC_DIFF:
                builder.addAllDiffFileInfo(syncDiffInit(req.getFileBaseInfosList()));
                break;
            case SYNC_ALL: // as default option
                default:
                    for(FileBaseInfo fbi : req.getFileBaseInfosList()) {
                        fm.save(new FileInfo(BeanConvertUtil.takeFromGrpc(fbi)));
                    }
                    break;
        }
        return builder.build();
    }

    private Iterable<? extends DiffFileInfo> syncDiffInit(List<FileBaseInfo> fileBaseInfosList) {
        return new ArrayList<>();
    }
}
