package transmit.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class UnaryConnetMap {

    public static InternalLogger logger = InternalLoggerFactory.getInstance(UnaryConnetMap.class);

    public static ConcurrentHashMap<Integer, UnaryConnect> allClientMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, Integer> ip2idMap = new ConcurrentHashMap<>();

    public static int addClientConnection(ChannelHandlerContext ctx, String agentip, String agentmac) {

        Integer agentid = ip2idMap.get(agentip);
        if (agentid == null) {
            UnaryConnect coninfo = new UnaryConnect(agentip, agentmac, ctx);
            agentid = coninfo.getAgentid();
            if (UnaryConnetMap.allClientMap.putIfAbsent(agentid, coninfo) != null) {
                logger.error("Duplicated agentid");
                return agentid;
            }
            ip2idMap.putIfAbsent(agentip, agentid);
        } else {
            logger.debug("agentip: {} exist,update info", agentip);
            UnaryConnect coninfo = allClientMap.get(agentid);
            coninfo.setAgentmac(agentmac);
            coninfo.setCtx(ctx);
            coninfo.clearHandshake();
            coninfo.clearBreaken();
        }
        return agentid;
    }

    public static UnaryConnect getClientConnection(ChannelHandlerContext ctx) {
        int agentid = ctx.channel().attr(UnaryConnect.AGENTID).get();

        UnaryConnect conn = allClientMap.get(agentid);
        if (conn != null)
            return conn;
        else {
            logger.info("ClientConenction not found in allClientMap, agentid: " + agentid);
        }
        return null;
    }

    public static UnaryConnect getClientConnection(int agentid) {
        UnaryConnect conn = allClientMap.get(agentid);
        if (conn != null)
            return conn;
        else {
            logger.info("ClientConenction not found in allClientMap, agentid: " + agentid);
        }
        return null;
    }

    public static UnaryConnect getClientConnection(String agentip) {
        Integer agentid = ip2idMap.get(agentip);
        if (agentid == null)
            return null;

        UnaryConnect conn = allClientMap.get(agentid);
        if (conn != null)
            return conn;
        else {
            logger.info("ClientConenction not found in allClientMap, agentid: " + agentid);
        }
        return null;
    }

    public static void removeClientConnection(int agentid) {
        UnaryConnect coninfo = getClientConnection(agentid);
        String ip = coninfo.getAgentip();
        if (UnaryConnetMap.allClientMap.remove(agentid) == null) {
            removeAgentip(ip);
        } else {
            logger.info("agentid: {} is not exist in allClientMap", agentid);
        }

        logger.debug("Client disconnected, agentid: {}, agentip: ", agentid, ip);
    }

    public static void removeClientConnection(ChannelHandlerContext ctx) {
        UnaryConnect coninfo = getClientConnection(ctx);
        int id = coninfo.getAgentid();
        String ip = coninfo.getAgentip();
        if (UnaryConnetMap.allClientMap.remove(id) == null) {
            removeAgentip(ip);
        } else {
            logger.info("agentid: {} is not exist in allClientMap", id);
        }

        logger.debug("Client disconnected, agentid: {}, agentip: {}", id, ip);
    }

    public static int agentip2id(String agentip) {
        Integer agentid = ip2idMap.get(agentip);
        if (agentid != null)
            return agentid;
        else {
            logger.info("User not login, agentip: " + agentip);
        }
        return 0;
    }

    protected static void removeAgentip(String agentip) {
        if (UnaryConnetMap.ip2idMap.remove(agentip) == null) {
            logger.info("agentip: {} is not exist in ip2idMap", agentip);
        }
    }
}
