package cn.com.unary.initcopy.exception;

/**
 * 为了防止 JNI 无法有效实现异常机智
 *
 * @author Shark.Yin
 * @since 1.0
 */
public class UnaryIoException extends RuntimeException {

	private static final long serialVersionUID = 8097701166213552429L;

	public UnaryIoException() {
		super();
	}

	public UnaryIoException(String message) {
		super(message);
	}

	public UnaryIoException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnaryIoException(Throwable cause) {
		super(cause);
	}

	protected UnaryIoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
