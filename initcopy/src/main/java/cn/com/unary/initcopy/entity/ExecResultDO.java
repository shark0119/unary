package cn.com.unary.initcopy.entity;

import cn.com.unary.initcopy.common.Entity;

/**
 * @author shark
 */
public class ExecResultDO implements Entity {
    private boolean isHealthy;
    private int code;
    private String msg;

    public ExecResultDO() {
    }

    public ExecResultDO(Boolean isHealthy, Integer code, String msg) {
        this.isHealthy = isHealthy;
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ExecResultDO{" +
                "isHealthy=" + isHealthy +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }

    public boolean getIsHealthy() {
        return isHealthy;
    }

    public ExecResultDO setIsHealthy(boolean healthy) {
        isHealthy = healthy;
        return this;
    }

    public int getCode() {
        return code;
    }

    public ExecResultDO setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ExecResultDO setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}
