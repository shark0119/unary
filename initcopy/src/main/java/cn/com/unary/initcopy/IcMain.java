package cn.com.unary.initcopy;

/**
 * 程序入口
 *
 * @author Shark.Yin
 * @since 1.0
 */
public class IcMain {

    public static void main(String[] args) {
        /*boolean isServer = false;
        boolean isClient = false;
        boolean nextIsPort = false;
        String help = "--help", asServer = "-s", asClient = "-c", designatedPort = "-p";;
        int maxPort = 65535;
        int port = 0;
        if (args.length == 0) {
            System.out.println("Input filesync --help for help.");
            return;
        }
        if (args.length == 1 && help.equals(args[0].trim())) {
            System.out.println("\"filesync -c\" to start a grpc server.");
            System.out.println("\"filesync -s -p [port]\" to start a target server.");
            return;
        }
        for (String arg : args) {
            if (nextIsPort) {
                try {
                    port = Integer.parseInt(arg);
                } catch (Exception e) {
                    System.out.println(arg + " is not a invalid port.");
                }
                nextIsPort = false;
            } else if (arg.trim().equals(asServer)) {
                if (isClient) {
                    System.out.println("can't use both -c and -s at the same time");
                    return;
                }
                isServer = true;
            } else if (arg.trim().equals(asClient)) {
                if (isServer) {
                    System.out.println("can't use both -c and -s at the same time");
                    return;
                }
                isClient = true;
            } else {
                if (arg.trim().equals(designatedPort)) {
                    nextIsPort = true;
                } else {
                    System.out.println("invalid arg: " + arg);
                    return;
                }
            }
        }
        if (port < 0 || port > maxPort) {
            System.out.println("invalid port :" + port);
            return;
        } else if (port == 0) {
            port = 50052;
            System.out.println("warning. using default port " + port);
        }

        if (isServer) {
            try {
                InitCopyServer.start(1, 2);
            } catch (Exception e) {
                System.out.println("Copy Server Start Fail. " + e.getMessage());
                return;
            }
        } else {
            try {
                new GrpcServiceStarter(new InitCopyGrpcImpl(new InitCopyGrpcLinker()), port);
            } catch (Exception e) {
                System.out.println("GRPC Server Start Fail. " + e.getMessage());
                return;
            }
        }*/
    }
}
