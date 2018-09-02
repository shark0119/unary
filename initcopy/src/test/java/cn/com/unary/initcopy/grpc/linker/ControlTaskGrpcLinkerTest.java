package cn.com.unary.initcopy.grpc.linker;

import cn.com.unary.initcopy.config.BeanConfig;
import cn.com.unary.initcopy.grpc.entity.ClientInitReq;
import cn.com.unary.initcopy.grpc.entity.DeleteTask;
import cn.com.unary.initcopy.grpc.entity.ExecResult;
import cn.com.unary.initcopy.grpc.entity.ModifyTask;
import cn.com.unary.initcopy.grpc.entity.ServerInitResp;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Objects;

public class ControlTaskGrpcLinkerTest {

    AnnotationConfigApplicationContext ac;
    ControlTaskGrpcLinker linker;

    @Before
    public void setUp() throws Exception {
        ac = new AnnotationConfigApplicationContext(BeanConfig.class);
        linker = ac.getBean(ControlTaskGrpcLinker.class);
    }
    @After
    public void tearDown() throws Exception {
        ac.close();
    }

    @Test
    public void init() {
        ClientInitReq.Builder builder = ClientInitReq.newBuilder();
        ServerInitResp resp = linker.init(builder.build());
        Objects.requireNonNull(resp);
    }

    @Test
    public void delete() {
        DeleteTask.Builder builder = DeleteTask.newBuilder();
        ExecResult result = linker.delete(builder.build());
        Objects.requireNonNull(result);
    }

    @Test
    public void modify() {
        ModifyTask.Builder builder = ModifyTask.newBuilder();
        ExecResult result = linker.modify(builder.build());
        Objects.requireNonNull(result);
    }
}