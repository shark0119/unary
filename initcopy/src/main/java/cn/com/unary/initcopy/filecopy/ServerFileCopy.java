package cn.com.unary.initcopy.filecopy;

import cn.com.unary.initcopy.dao.FileManager;
import cn.com.unary.initcopy.grpc.entity.ClientInitReq;
import cn.com.unary.initcopy.grpc.entity.ServerInitResp;
import cn.com.unary.initcopy.utils.AbstractLogable;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 目标端的文件复制模块
 * 线程安全
 *
 * @author Shark.Yin
 * @since 1.0
 */
@Component("ServerFileCopy")
public class ServerFileCopy extends AbstractLogable implements ApplicationContextAware {

    private ApplicationContext context;
    @Autowired
    private FileManager fm;

    /**
     * 在传输模块接收到数据包后，调用该方法
     *
     * @param data 数据包
     */
    public void resolverPack (byte[] data) {

    }

    public ServerInitResp startInit (ClientInitReq req) {
        Thread thread = new Thread();
        thread.getId();

        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
