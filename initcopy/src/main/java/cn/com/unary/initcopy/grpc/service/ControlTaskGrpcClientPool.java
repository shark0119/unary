package cn.com.unary.initcopy.grpc.service;

import cn.com.unary.initcopy.common.AbstractLoggable;
import cn.com.unary.initcopy.grpc.ControlTaskGrpc;
import cn.com.unary.initcopy.grpc.entity.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static cn.com.unary.initcopy.InitCopyContext.RESOURCE_TIME_OUT;

/**
 * @author Shark.Yin
 * @since 1.0
 */
public class ControlTaskGrpcClientPool {

    private static Map<String, ClientInfo> grpcClientMap = new ConcurrentHashMap<>(10);

    private ControlTaskGrpcClientPool() {
    }

    public static ControlTaskGrpcClient getClient(String ip, Integer port) {
        ClientInfo clientInfo = grpcClientMap.get(ip + port);
        if (clientInfo == null) {
            clientInfo = new ClientInfo();
            clientInfo.grpcClient = new ControlTaskGrpcClient(ip, port);
        }
        clientInfo.timeStamp = System.currentTimeMillis();
        return clientInfo.grpcClient;
    }

    public static void clean() {
        long currentTimeTimeMillis = System.currentTimeMillis();
        List<String> removeKey = new ArrayList<>(grpcClientMap.size() / 2);

        for (Map.Entry<String, ClientInfo> entry : grpcClientMap.entrySet()) {
            if (currentTimeTimeMillis - entry.getValue().timeStamp > RESOURCE_TIME_OUT) {
                removeKey.add(entry.getKey());
            }
        }
        for (String key : removeKey) {
            grpcClientMap.remove(key).grpcClient.close();
        }
    }

    private static class ClientInfo {
        ControlTaskGrpcClient grpcClient;
        long timeStamp;
    }

    public static class ControlTaskGrpcClient extends AbstractLoggable {

        private static final String MSG_SERVER_ERROR = "Error grpc server status";
        private ControlTaskGrpc.ControlTaskBlockingStub blockingStub;
        private ManagedChannel channel;

        /**
         * 配置 GRPC 服务的相关信息
         *
         * @param host GRPC 服务地址
         * @param port GRPC 服务监听的端口
         */
        private ControlTaskGrpcClient(String host, int port) {
            try {
                channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build();
                blockingStub = ControlTaskGrpc.newBlockingStub(channel);
            } catch (Exception e) {
                throw new IllegalStateException(MSG_SERVER_ERROR, e);
            }
        }

        /**
         * 调用 {@link cn.com.unary.initcopy.grpc.ControlTaskGrpc#METHODID_INIT}
         *
         * @param req 初始化请求
         * @return 初始化响应
         */
        public ServerInitResp invokeGrpcInit(ClientInitReq req) {
            try {
                return blockingStub.init(req);
            } catch (Exception e) {
                throw new IllegalStateException(MSG_SERVER_ERROR, e);
            }
        }

        /**
         * 调用 {@link cn.com.unary.initcopy.grpc.ControlTaskGrpc#METHODID_DELETE}
         *
         * @param deleteTask 删除任务的相关参数
         * @return 执行结果
         */
        public ExecResult invokeGrpcDelete(DeleteTask deleteTask) {
            try {
                return blockingStub.delete(deleteTask);
            } catch (Exception e) {
                throw new IllegalStateException(MSG_SERVER_ERROR, e);
            }
        }

        /**
         * 调用 {@link cn.com.unary.initcopy.grpc.ControlTaskGrpc#METHOD_RESUME}
         *
         * @param resumeTask 修改任务的相关参数
         * @return 执行结果
         */
        public SyncProcess invokeGrpcResume(ResumeTask resumeTask) {
            try {
                return blockingStub.resume(resumeTask);
            } catch (Exception e) {
                throw new IllegalStateException(MSG_SERVER_ERROR, e);
            }
        }

        public TaskState invokeGrpcQuery(QueryTask queryTask) {
            try {
                return blockingStub.query(queryTask);
            } catch (Exception e) {
                throw new IllegalStateException(MSG_SERVER_ERROR, e);
            }
        }

        public void close() {
            channel.shutdown();
        }
    }
}
