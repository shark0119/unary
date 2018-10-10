package cn.com.unary.initcopy.filecopy.init;

import cn.com.unary.initcopy.common.utils.BeanExactUtil;
import cn.com.unary.initcopy.dao.FileManager;
import cn.com.unary.initcopy.entity.BaseFileInfoDO;
import cn.com.unary.initcopy.entity.FileInfoDO;
import cn.com.unary.initcopy.exception.InfoPersistenceException;
import cn.com.unary.initcopy.grpc.entity.ClientInitReq;
import cn.com.unary.initcopy.grpc.entity.DiffFileInfo;
import cn.com.unary.initcopy.grpc.entity.ServerInitResp;
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

    public ServerInitResp startInit(ClientInitReq req) throws InfoPersistenceException {
        ServerInitResp.Builder builder = ServerInitResp.newBuilder();
        builder.setReady(true);
        builder.setMsg("success");
        builder.setTaskId(req.getTaskId());
        List<BaseFileInfoDO> bfiDOList = BeanExactUtil.takeFromGrpc(req.getBaseFileInfosList());
        switch (req.getSyncType()) {
            case SYNC_DIFF:
                builder.addAllDiffFileInfos(syncDiffInit(bfiDOList));
                break;
            case SYNC_ALL:
            default:
                FileInfoDO fi;
                for (BaseFileInfoDO fbi : bfiDOList) {
                    fi = new FileInfoDO(fbi);
                    fi.setTaskId(req.getTaskId());
                    fm.save(fi);
                }
                break;
        }
        return builder.build();
    }

    private List<DiffFileInfo> syncDiffInit(List<BaseFileInfoDO> bfiDOList) {
        return new ArrayList<>(bfiDOList.size());
    }
}
