package cn.com.unary.initcopy.service;

import cn.com.unary.initcopy.grpc.service.ControlTaskGrpcClientPool;
import cn.com.unary.initcopy.service.filecopy.ServerFileCopy;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 用于定时巡视注册的资源并进行管理
 *
 * @author Shark.Yin
 * @since 1.0
 */

@Component
@Scope("singleton")
public class ResourceDirector implements Runnable {
    private static final long INTERVAL = 30000L;
    @Autowired
    private ServerFileCopy serverFileCopy;
    @Setter
    private String threadName;

    @Override
    public void run() {
        if (threadName != null) {
            Thread.currentThread().setName(threadName);
        }
        while (true) {
            try {
                Thread.sleep(INTERVAL);
            } catch (InterruptedException e) {
                break;
            }
            serverFileCopy.clean();
            ControlTaskGrpcClientPool.clean();
        }
    }
}
