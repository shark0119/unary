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
    private Integer port;

    @Override
    public String toString() {
        return "SyncTargetDO{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
