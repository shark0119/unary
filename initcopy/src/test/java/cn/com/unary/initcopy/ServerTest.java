package cn.com.unary.initcopy;

import cn.com.unary.initcopy.config.BeanConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ServerTest {
    static final int SERVER_TRANSFER_PORT = 60005;
    static final int SERVER_GRPC_PORT = 60006;
    static final int SERVER_INNER_GRPC_PORT = 60007;

    private static void setUp() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(BeanConfig.class);
        InitCopyContext context = ac.getBean(InitCopyContext.class);
        context.start(SERVER_TRANSFER_PORT, SERVER_GRPC_PORT, SERVER_INNER_GRPC_PORT);
    }

    public static void main(String[] args) {
        setUp();
    }
}
