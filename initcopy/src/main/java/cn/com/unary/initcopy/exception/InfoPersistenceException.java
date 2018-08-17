package cn.com.unary.initcopy.exception;

/**
 * 与信息持久化相关的异常
 * @author shark
 *
 */
public class InfoPersistenceException extends RuntimeException {

	private static final long serialVersionUID = 8097701166213552429L;

	public InfoPersistenceException() {
		super();
	}

	public InfoPersistenceException(String message) {
		super(message);
	}

	public InfoPersistenceException(String message, Throwable cause) {
		super(message, cause);
	}

	public InfoPersistenceException(Throwable cause) {
		super(cause);
	}

	protected InfoPersistenceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
