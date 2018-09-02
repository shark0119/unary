package cn.com.unary.initcopy.common;


import java.util.Objects;

/**
 * 线程池内线程未知异常捕获处理
 *
 * @author Shark.Yin
 * @since 1.0
 */
public class ExecutorExceptionHandler implements Thread.UncaughtExceptionHandler {

    public ExecutorExceptionHandler(Object informer) {
        this.informer = informer;
    }

    private final Object informer;

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (informer != null) {
            synchronized (informer) {
                informer.notifyAll();
            }
        }
    }
}
