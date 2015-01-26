package org.cosysoft.device.exception;


public class SSHException extends NestedException {

	private static final long serialVersionUID = -7174061223976794607L;

	

	public SSHException(String message) {
		super(message);
	}

	public SSHException(Throwable cause) {
		super(cause);
	}

	public SSHException(String message, Throwable cause) {
		super(message, cause);
	}
}
