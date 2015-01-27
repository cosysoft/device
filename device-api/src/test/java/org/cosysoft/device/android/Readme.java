package org.cosysoft.device.android;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.TreeSet;

import org.cosysoft.device.android.impl.AndroidDeviceStore;
import org.cosysoft.device.android.impl.DefaultAndroidApp;
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
	public void takeScreenshot() {
		AndroidDevice device = getDevices().pollFirst();
		BufferedImage image = device.takeScreenshot();
		String imagePath = new File(System.getProperty("java.io.tmpdir"),
				"screenshot.png").getAbsolutePath();
		ImageUtils.writeToFile(image, imagePath);
	}

	@Test
	public void installApp() {
		AndroidDevice device = getDevices().pollFirst();

		AndroidApp app = new DefaultAndroidApp(new File(
				"d:\\uat\\com.android.chrome.apk"));
		device.install(app);
		if (device.isInstalled(app)) {
			device.uninstall(app);
		}
	}
	
	@Test
	public void testLogcat() throws InterruptedException{
		AndroidDevice device = getDevices().pollFirst();
		
		device.runLogService(null);
		
		Thread.sleep(5000);
		
		
	}
	
	

}
