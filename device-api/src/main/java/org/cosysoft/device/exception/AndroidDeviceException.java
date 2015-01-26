package org.cosysoft.device.exception;


public class AndroidDeviceException extends NestedException {
	private static final long serialVersionUID = 5431510243540521938L;

	public AndroidDeviceException(String message) {
		super(message);
	}

	public AndroidDeviceException(Throwable t) {
		super(t);
	}

	public AndroidDeviceException(String message, Throwable t) {
		super(message, t);
	}
}
