package cn.com.unary.initcopy;

import cn.com.unary.initcopy.config.BeanConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ClientTest {

    private static AnnotationConfigApplicationContext ac;
    public static final Integer CLIENT_TRANSFER_PORT = 50005;
    public static final Integer CLIENT_GRPC_PORT = 50006;
    public static final Integer CLIENT_INNER_GRPC_PORT = 50007;

    private static void setUp() {
        ac = new AnnotationConfigApplicationContext(BeanConfig.class);
        InitCopyContext context = ac.getBean(InitCopyContext.class);
        context.start(CLIENT_TRANSFER_PORT, CLIENT_GRPC_PORT, CLIENT_INNER_GRPC_PORT);
    }

    public static void main(String[] args) {
        setUp();
    }
}
