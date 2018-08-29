package cn.com.unary.initcopy.common;

import org.apache.log4j.Logger;

/**
 * 获取日志类
 *
 * @author shark
 */
public abstract class AbstractLogable {
    protected static Logger logger;

    public AbstractLogable() {
        logger = Logger.getLogger(this.getClass());
    }
}
