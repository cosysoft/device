package org.cosysoft.device.android;

import java.awt.image.BufferedImage;
import java.util.TreeSet;

import org.cosysoft.device.android.impl.AndroidDeviceStore;
import org.cosysoft.device.image.ImageUtils;
import org.junit.Test;

public class Readme extends AndroidDeviceTest {

	@Test
	public void takeDevices() {
		TreeSet<AndroidDevice> devices = AndroidDeviceStore.getInstance()
				.getDevices();
		AndroidDevice device = devices.pollFirst();
		System.out.println(device.getName());
	}

	@Test
	public void takeScreenshot(){
		AndroidDevice device = getDevices().pollFirst();
		BufferedImage image = device.takeScreenshot();
		ImageUtils.writeToFile(image, "d:\\a.png");
	}
	
}
