package cn.com.unary.initcopy.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取日志类
 * @author shark
 *
 */
public abstract class AbstractLogable {
	protected static Logger logger ;
	public AbstractLogable () {
		logger = LoggerFactory.getLogger(this.getClass());
	}
}
