package cn.com.unary.initcopy.filecopy.init;

import cn.com.unary.initcopy.dao.FileManager;
import cn.com.unary.initcopy.entity.BaseFileInfoDO;
import cn.com.unary.initcopy.entity.ClientInitReqDO;
import cn.com.unary.initcopy.entity.DiffFileInfoDO;
import cn.com.unary.initcopy.entity.FileInfoDO;
import cn.com.unary.initcopy.entity.ServerInitRespDO;
import cn.com.unary.initcopy.exception.InfoPersistenceException;
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

    public ServerInitRespDO startInit(ClientInitReqDO req) throws InfoPersistenceException {
        ServerInitRespDO resp = new ServerInitRespDO();
        resp.setReady(true).setMsg("success")
                .setTaskId(req.getTaskId());
        switch (req.getSyncType()) {
            case SYNC_DIFF:
                resp.setDiffFileInfos(syncDiffInit(req.getFileBaseInfos()));
                break;
            case SYNC_ALL:
            default:
                FileInfoDO fi;
                for (BaseFileInfoDO fbi : req.getFileBaseInfos()) {
                    fi = new FileInfoDO(fbi);
                    fi.setTaskId(req.getTaskId());
                    fm.save(fi);
                }
                break;
        }
        return resp;
    }

    private List<DiffFileInfoDO> syncDiffInit(List<BaseFileInfoDO> fileBaseInfosList) {
        return new ArrayList<>();
    }
}
