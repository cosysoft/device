package com.github.cosysoft.device.exception;


public class DeviceStoreException extends NestedException {
	private static final long serialVersionUID = 5431510243540521938L;

	public DeviceStoreException(String message) {
		super(message);
	}

	public DeviceStoreException(Throwable t) {
		super(t);
	}

	public DeviceStoreException(String message, Throwable t) {
		super(message, t);
	}
}
