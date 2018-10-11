package cn.com.unary.initcopy.grpc.linker;

public class ControlTaskGrpcLinkerTest {
/*
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
    }*/
}