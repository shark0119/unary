package cn.com.unary.initcopy.common;

import java.io.Closeable;

/**
 * 线程池异常处理器
 *
 * @author Shark.Yin
 * @since 1.0
 */
public class ExecExceptionsHandler extends AbstractLoggable implements Thread.UncaughtExceptionHandler {

    private final Closeable resource;

    public ExecExceptionsHandler(Closeable closeable) {
        this.resource = closeable;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        try {
            resource.close();
        } catch (Exception e1) {
            logger.error("resource pause error.", e1);
        } finally {
            logger.error(t.getName(), e);
        }
    }
}

