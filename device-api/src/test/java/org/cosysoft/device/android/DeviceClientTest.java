package org.cosysoft.device.android;

import org.junit.Test;

import com.android.ddmlib.Client;
import com.android.ddmlib.ClientData;

public class DeviceClientTest extends AndroidDeviceTest {

	@Test
	public void testClients() {
		Client[] clients = pollFirst().getDevice().getClients();
		for (int i = 0; i < clients.length; i++) {
			ClientData clientData = clients[i].getClientData();
			System.out.println(clientData.getClientDescription());
		}
	}
	
	

}
