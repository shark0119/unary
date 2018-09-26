package cn.com.unary.initcopy;

import cn.com.unary.initcopy.config.BeanConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 程序入口
 *
 * @author Shark.Yin
 * @since 1.0
 */
public class IcMain {

    public static void main(String[] args) {
        final int paraAmounts = 6;
        final String transPortPara = "-tp";
        final String grpcPortPara = "-gp";
        final String innerGrpcPortPara = "-igp";
        if (args.length != paraAmounts
                || !args[0].equals(transPortPara)
                || !grpcPortPara.equals(args[2])
                || !innerGrpcPortPara.equals(args[4])) {
            System.out.println("-tp [transmitPort] -gp [grpcPort] -igp [innerGrpcPort]");
            return;
        }
        try {
            int transPort = Integer.parseInt(args[1]);
            int grpcPort = Integer.parseInt(args[3]);
            int innerGrpcPort = Integer.parseInt(args[5]);
            AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(BeanConfig.class);
            InitCopyContext context = ac.getBean(InitCopyContext.class);
            context.start(transPort, grpcPort, innerGrpcPort);
        } catch (Exception e) {
            System.out.println("args 1,3,5 must be integer");
            return;
        }
    }
}
