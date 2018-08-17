package cn.com.unary.initcopy.config;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.alibaba.druid.pool.DruidDataSource;

import cn.com.unary.initcopy.InitCopyContext;
import cn.com.unary.initcopy.dao.FileManager;
import cn.com.unary.initcopy.dao.RamFileManager;
import cn.com.unary.initcopy.dao.SqliteFileManager;
import cn.com.unary.initcopy.filecopy.filepacker.Packer;
import cn.com.unary.initcopy.filecopy.filepacker.SyncAllPacker;
import cn.com.unary.initcopy.filecopy.io.AbstractFileInput;
import cn.com.unary.initcopy.filecopy.io.AbstractFileOutput;
import cn.com.unary.initcopy.filecopy.io.JavaNioFileInput;
import cn.com.unary.initcopy.filecopy.io.JavaNioFileOutput;
import cn.com.unary.initcopy.mock.UnaryProcess;
import cn.com.unary.initcopy.mock.UnaryTransferClient;

/**
 * 用于 Spring 创建 Beans
 * @author shark
 *
 */
@Configuration
@ComponentScan(basePackages= {"cn.com.unary.initcopy.config", "com.cn.unary"})
public class BeanConfig {

	@Bean
	@Scope("singleton")
	public InitCopyContext context () {
		return new InitCopyContext();
	}
	/**
	 * 创建数据源
	 * @param url 数据库URL
	 * @param driver 数据库驱动
	 * @return 数据源
	 * @throws SQLException 数据源创建失败
	 */
	@Bean
	public DruidDataSource dataSource (
			@Value("#{AttrConfig.getDbUrl()}")String url,
			@Value("#{AttrConfig.getDbDriver()}")String driver) throws SQLException {
		DruidDataSource dds = new DruidDataSource();
		dds = new DruidDataSource();
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
	public SimpleDateFormat sdf () {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	@Bean
	public AbstractFileInput javaNioFileInput () {
		return new JavaNioFileInput();
	}
	@Bean
	public AbstractFileOutput javaNioFileOutput () {
		return new JavaNioFileOutput();
	}
	@Bean
	public FileManager ramFileManager () {
		return new RamFileManager();
	}
	@Bean
	public FileManager sqliteFileManager () {
		return new SqliteFileManager();
	}
	@Bean
	public UnaryTransferClient utc () {
		return new UnaryTransferClient() {
			public void start(String ip, int port) {			}
			public void setProcess(UnaryProcess process) {			}
			public void sendData(byte[] data) {			}
			public int getMaxPackSize() {				return 0;			}
		};
	}
	@Bean
	public Packer syncAllFilePackAndResolve (
			InitCopyContext context,
			@Value("#{sqliteFileManager}")FileManager ifm,
			AbstractFileInput afi,
			UnaryTransferClient utc) {
		SyncAllPacker safar = new SyncAllPacker();
		safar.setContext(context);
		safar.setIfm(ifm);
		safar.setInput(afi);
		safar.setUtc(utc);
		return safar;
	}
	
	/*@Bean
	// @Scope("singleton")
	public FileInfo fileInfo () {
		return new FileInfo();
	}
	@Bean
	public String str1 () {
		return "str1";
	}
	@Bean 
	public String str2 (@Value("#{str1}")String str) {
		return str + "  str22";
	}*/
}
