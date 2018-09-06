package cn.com.unary.initcopy.common;

import org.apache.log4j.Logger;

/**
 * 获取 Logger 类
 *
 * @author shark
 */
public abstract class AbstractLoggable {
    protected static Logger logger;

    public AbstractLoggable() {
        logger = Logger.getLogger(this.getClass());
    }
}
