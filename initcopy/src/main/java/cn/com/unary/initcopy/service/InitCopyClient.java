package cn.com.unary.initcopy.service;

/**
 * 初始化复制的客户端
 * 开启向外部开放的 GRPC 服务
 *
 * @author Shark.Yin
 * @since 1.0
 */
public class InitCopyClient {
    private InitCopyClient () {}
    /**
     * 开启面向外部应用的 GRPC 接口，提供任务的 CRUD
     *
     * @param port GRPC 服务监听的端口
     * @throws Exception 服务启动失败
     */
    public static void start (int port) throws Exception {

    }
}
