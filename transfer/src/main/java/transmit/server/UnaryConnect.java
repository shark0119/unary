package transmit.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

import java.util.concurrent.atomic.AtomicInteger;

public class UnaryConnect {
    private static final AtomicInteger idgenerator = new AtomicInteger(0);
    private String agentip;
    private String agentmac;
    private int agentid;
    private ChannelHandlerContext ctx;
    private int handshake;
    private Boolean breaken;

    public static AttributeKey<Integer> AGENTID = AttributeKey.valueOf("agentid");

    public UnaryConnect(String agentip, String agentmac, ChannelHandlerContext ctx) {
        super();
        this.agentip = agentip;
        this.agentmac = agentmac;
        this.ctx = ctx;
        this.agentid = idgenerator.incrementAndGet();
        this.handshake = 0;
        this.breaken = false;
        this.ctx.channel().attr(UnaryConnect.AGENTID).set(agentid);
    }

    public String getAgentip() {
        return agentip;
    }

    public String getAgentmac() {
        return agentmac;
    }

    public void setAgentmac(String agentmac) {
        this.agentmac = agentmac;
    }

    public int getAgentid() {
        return agentid;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        this.ctx.channel().attr(UnaryConnect.AGENTID).set(agentid);
    }

    public int getHandshake() {
        return handshake;
    }

    public void selfAddHandshake() {
        this.handshake++;
    }

    public void clearHandshake() {
        this.handshake = 0;
    }

    public Boolean getBreaken() {
        return breaken;
    }

    public void setBreaken() {
        this.breaken = true;
    }

    public void clearBreaken() {
        this.breaken = false;
    }

    @Override
    public String toString() {
        return "ConnetInfo [agentip=" + agentip + ", agentmac=" + agentmac + ", agentid=" + agentid + ", handshake="
                + handshake + ", breaken=" + breaken + "]";
    }
}
