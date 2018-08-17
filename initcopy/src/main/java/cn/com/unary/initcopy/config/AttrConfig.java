package cn.com.unary.initcopy.config;

import org.springframework.stereotype.Component;

/**
 * 用于定义一些通用属性
 * @author shark
 *
 */
@Component("AttrConfig")
public class AttrConfig {

	public String getDbUrl () {
		return "jdbc:sqlite:G:\\sqliteDB\\test.db";
	}
	public String getDbDriver () {
		return "org.sqlite.JDBC";
	}
	public String getDateFormat () {
		return "yyyy-MM-dd HH:mm:ss";
	}
}
