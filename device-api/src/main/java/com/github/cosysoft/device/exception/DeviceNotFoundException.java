package com.github.cosysoft.device.exception;


public class DeviceNotFoundException extends NestedException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5641289183499526194L;

	public DeviceNotFoundException(String msg) {
		super(msg);
	}

	public DeviceNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public DeviceNotFoundException(Throwable cause) {
		super(cause);
	}

}
