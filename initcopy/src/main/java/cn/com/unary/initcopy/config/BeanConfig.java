package cn.com.unary.initcopy.config;

import cn.com.unary.initcopy.dao.RamFileManager;
import cn.com.unary.initcopy.common.ExecutorExceptionHandler;
import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 用于 Spring 创建 Beans
 *
 * @author shark
 */
@Configuration
@ComponentScan(basePackages = {"cn.com.unary"})
public class BeanConfig {
    /**
     * 创建数据源
     *
     * @param url    数据库URL
     * @param driver 数据库驱动
     * @return 数据源
     * @throws SQLException 数据源创建失败
     */
    @Bean
    @Scope("singleton")
    public DruidDataSource dataSource(
            @Value("#{AttrConfig.getDbUrl()}") String url,
            @Value("#{AttrConfig.getDbDriver()}") String driver) throws SQLException {
        DruidDataSource dds = new DruidDataSource();
        dds.setUrl(url);
        dds.setDriverClassName(driver);
        dds.setInitialSize(1);
        dds.setMinIdle(1);
        dds.setMaxActive(20);
        dds.setMaxWait(60_000);
        dds.setTimeBetweenEvictionRunsMillis(60_000);
        dds.setMinEvictableIdleTimeMillis(300_000);
        dds.setValidationQuery("SELECT 'x'");
        dds.setTestWhileIdle(true);
        dds.setTestOnBorrow(false);
        dds.setTestOnReturn(false);
        dds.setFilters("stat");
        dds.init();
        return dds;
    }

    @Bean
    @Scope("singleton")
    public SimpleDateFormat sdf() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    @Bean
    @Scope("singleton")
    public ExecutorService serverExecutor () {
        ThreadFactory executorThreadFactory = new BasicThreadFactory.Builder()
                .namingPattern("server-%d-task-")
                .uncaughtExceptionHandler(new ExecutorExceptionHandler())
                .build();
        return new ThreadPoolExecutor(1,2,3,
                null, null, executorThreadFactory);
    }
    @Bean
    @Scope("singleton")
    public ExecutorService clientExecutor () {
        ThreadFactory executorThreadFactory = new BasicThreadFactory.Builder()
                .namingPattern("client-%d-pack-")
                .uncaughtExceptionHandler(new ExecutorExceptionHandler())
                .build();
        return new ThreadPoolExecutor(1,2,3,
                null, null, executorThreadFactory);
    }
    @Bean
    @Scope("singleton")
    public ExecutorService contextExecutor () {
        ThreadFactory executorThreadFactory = new BasicThreadFactory.Builder()
                .namingPattern("init-copy-context-executor-%d")
                .uncaughtExceptionHandler(new ExecutorExceptionHandler())
                .build();
        return new ThreadPoolExecutor(1,2,3,
                null, null, executorThreadFactory);
    }
    @Bean
    @Scope("singleton")
    public RamFileManager clientFM () {
        return new RamFileManager();
    }
    @Bean
    @Scope("singleton")
    public RamFileManager serverFM () {
        return new RamFileManager();
    }
}
