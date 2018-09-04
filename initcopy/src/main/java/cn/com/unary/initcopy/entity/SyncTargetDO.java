package cn.com.unary.initcopy.entity;

import cn.com.unary.initcopy.common.Entity;

/**
 * 目标端IP及端口信息
 *
 * @author Shark.Yin
 * @since 1.0
 */
public class SyncTargetDO implements Entity {
    private String ip;
    private int transferPort;
    private int grpcPort;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getTransferPort() {
        return transferPort;
    }

    public void setTransferPort(Integer transferPort) {
        this.transferPort = transferPort;
    }

    public int getGrpcPort() {
        return grpcPort;
    }

    public void setGrpcPort(int grpcPort) {
        this.grpcPort = grpcPort;
    }

    @Override
    public String toString() {
        return "SyncTargetDO{" +
                "ip='" + ip + '\'' +
                ", transferPort=" + transferPort +
                ", grpcPort=" + grpcPort +
                '}';
    }
}
