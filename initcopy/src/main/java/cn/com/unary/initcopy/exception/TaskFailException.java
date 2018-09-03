package cn.com.unary.initcopy.exception;

/**
 * 面向上层的任务异常
 *
 * @author Shark.Yin
 * @since 1.0
 */
public class TaskFailException extends Exception {
    public TaskFailException() {
        super();
    }

    public TaskFailException(String message) {
        super(message);
    }

    public TaskFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskFailException(Throwable cause) {
        super(cause);
    }

    protected TaskFailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
