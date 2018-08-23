package cn.com.unary.initcopy.filecopy.init;

import cn.com.unary.initcopy.grpc.entity.ServerInitResp;
import cn.com.unary.initcopy.grpc.entity.SyncTask;
import org.springframework.stereotype.Component;

@Component("serverFileCopyInit")
public class ServerFileCopyInit {
    public ServerInitResp startInit(SyncTask syncTask) throws Exception {
        return null;
    }
}
