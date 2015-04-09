package org.cosysoft.device.android.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.TreeSet;

import org.cosysoft.device.android.AndroidApp;
import org.cosysoft.device.android.AndroidDevice;
import org.cosysoft.device.android.impl.AndroidDeviceStore;
import org.cosysoft.device.android.impl.DefaultAndroidApp;
import org.cosysoft.device.image.ImageUtils;
import org.cosysoft.device.model.ClientDataInfo;
import org.junit.Test;
import org.slf4j.profiler.Profiler;

import com.android.ddmlib.Log.LogLevel;
import com.android.ddmlib.logcat.LogCatFilter;
import com.android.ddmlib.logcat.LogCatListener;
import com.android.ddmlib.logcat.LogCatMessage;

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
		
		Profiler profiler = new Profiler("screen");
		profiler.start("start");
		BufferedImage image = device.takeScreenshot();
		profiler.stop();
		profiler.print();
		String imagePath = new File(System.getProperty("java.io.tmpdir"),
				"screenshot.png").getAbsolutePath();
		ImageUtils.writeToFile(image, imagePath);
		logger.debug("image saved to path {}", imagePath);
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
	public void testLogcat() throws InterruptedException {
		AndroidDevice device = getDevices().pollFirst();

		final LogCatFilter filter = new LogCatFilter("", "", "com.android", "",
				"", LogLevel.WARN);
		final LogCatListener lcl = new LogCatListener() {
			@Override
			public void log(List<LogCatMessage> msgList) {
				for (LogCatMessage msg : msgList) {
					if (filter.matches(msg)) {
						System.out.println(msg);
					}
				}
			}
		};

		device.addLogCatListener(lcl);

		Thread.sleep(60000);

	}

	@Test
	public void testListClients() {
		AndroidDevice device = getDevices().pollFirst();
		List<ClientDataInfo> clientDataInfos = device.getClientDatasInfo();
		for (ClientDataInfo client : clientDataInfos) {
			System.out.println(client.getName());
			System.out.println(client.getPid());
		}
	}

}
