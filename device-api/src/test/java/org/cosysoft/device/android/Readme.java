package org.cosysoft.device.android;

import java.util.TreeSet;

import org.cosysoft.device.android.impl.AndroidDeviceStore;
import org.junit.Test;

public class Readme {

	@Test
	public void takeDevices() {
		TreeSet<AndroidDevice> devices = AndroidDeviceStore.getInstance().getDevices();
		AndroidDevice device = devices.pollFirst();
		System.out.println(device.getName());
	}

}
