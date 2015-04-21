package com.github.cosysoft.device.exception;

/**
 * 
 * @author ltyao
 *
 */
public class NestedException extends RuntimeException {

	private static final long serialVersionUID = 5439915454935047936L;

	static {
		// Eagerly load the NestedExceptionUtils class to avoid classloader
		// deadlock
		// issues on OSGi when calling getMessage(). Reported by Don Brown;
		// SPR-5607.
		NestedExceptionUtils.class.getName();
	}

	/**
	 * Construct a {@code NestedRuntimeException} with the specified detail
	 * message.
	 * 
	 * @param msg
	 *            the detail message
	 */
	public NestedException(String msg) {
		super(msg);
	}

	/**
	 * Construct a {@code NestedRuntimeException} with the specified detail
	 * message and nested exception.
	 * 
	 * @param msg
	 *            the detail message
	 * @param cause
	 *            the nested exception
	 */
	public NestedException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public NestedException(Throwable cause) {
		super(cause);
	}

	/**
	 * Return the detail message, including the message from the nested
	 * exception if there is one.
	 */
	@Override
	public String getMessage() {
		return NestedExceptionUtils
				.buildMessage(super.getMessage(), getCause());
	}

	/**
	 * Retrieve the innermost cause of this exception, if any.
	 * 
	 * @return the innermost exception, or {@code null} if none
	 * @since 2.0
	 */
	public Throwable getRootCause() {
		Throwable rootCause = null;
		Throwable cause = getCause();
		while (cause != null && cause != rootCause) {
			rootCause = cause;
			cause = cause.getCause();
		}
		return rootCause;
	}

	/**
	 * Retrieve the most specific cause of this exception, that is, either the
	 * innermost cause (root cause) or this exception itself.
	 * <p>
	 * Differs from {@link #getRootCause()} in that it falls back to the present
	 * exception if there is no root cause.
	 * 
	 * @return the most specific cause (never {@code null})
	 * @since 2.0.3
	 */
	public Throwable getMostSpecificCause() {
		Throwable rootCause = getRootCause();
		return (rootCause != null ? rootCause : this);
	}

	/**
	 * Check whether this exception contains an exception of the given type:
	 * either it is of the given class itself or it contains a nested cause of
	 * the given type.
	 * 
	 * @param exType
	 *            the exception type to look for
	 * @return whether there is a nested exception of the specified type
	 */
	@SuppressWarnings("rawtypes")
	public boolean contains(Class exType) {
		if (exType == null) {
			return false;
		}
		if (exType.isInstance(this)) {
			return true;
		}
		Throwable cause = getCause();
		if (cause == this) {
			return false;
		}
		if (cause instanceof NestedException) {
			return ((NestedException) cause).contains(exType);
		} else {
			while (cause != null) {
				if (exType.isInstance(cause)) {
					return true;
				}
				if (cause.getCause() == cause) {
					break;
				}
				cause = cause.getCause();
			}
			return false;
		}
	}

}
