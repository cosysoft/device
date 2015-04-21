package com.github.cosysoft.device.model;

import java.util.Locale;

import com.android.ddmlib.Client;
import com.android.ddmlib.ClientData;

/**
 * 
 * @author 兰天
 *
 */
public class ClientDataInfo {

	private final transient ClientData clientData;
	private final transient Client client;

	private String name;
	private Integer pid;
	private Integer port;

	public ClientDataInfo(Client client) {
		this.clientData = client.getClientData();
		this.client = client;
		this.name = name();
		this.pid = clientData.getPid();
		this.port = port();

	}

	public String getName() {
		return name;
	}

	public Integer getPid() {
		return pid;
	}

	public Integer getPort() {
		return port;
	}

	@Override
	public String toString() {
		return "ClientDataInfo [name=" + name + ", pid=" + pid + ", port="
				+ port + "]";
	}

	protected String name() {
		String name = clientData.getClientDescription();
		if (name != null) {
			if ((clientData.isValidUserId()) && (clientData.getUserId() != 0)) {
				return String.format(Locale.CHINA, "%s (%d)", new Object[] {
						name, Integer.valueOf(clientData.getUserId()) });
			}
			return name;
		}
		return "?";
	}

	protected Integer port() {
		int port = client.getDebuggerListenPort();
		return port;
	}
}
