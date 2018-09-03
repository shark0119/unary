package cn.com.unary.initcopy.exception;

/**
 * Jni 错误
 *
 * @author Shark.Yin
 * @since 1.0
 */
public class JniException extends Exception {
    public JniException() {
        super();
    }

    public JniException(String message) {
        super(message);
    }

    public JniException(String message, Throwable cause) {
        super(message, cause);
    }

    public JniException(Throwable cause) {
        super(cause);
    }

    protected JniException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
