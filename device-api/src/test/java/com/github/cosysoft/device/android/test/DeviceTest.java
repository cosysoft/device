package com.github.cosysoft.device.android.test;

import java.io.File;

import com.github.cosysoft.device.android.AndroidApp;
import com.github.cosysoft.device.android.AndroidDevice;
import com.github.cosysoft.device.android.impl.DefaultAndroidApp;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author ltyao
 *
 */
public class DeviceTest extends AndroidDeviceTest {

	static final Logger logger = LoggerFactory.getLogger(DeviceTest.class);

	@Test
	public void testGetProperties() {
		for (AndroidDevice device : getDevices()) {
			device.getDeviceInfo();
		}
	}

	@Test
	public void testGetInfo() {
		for (AndroidDevice device : getDevices()) {
			logger.debug(device.getName().toUpperCase());
		}
	}

	@Test
	public void testUnlock() {
		for (AndroidDevice device : getDevices()) {
			device.unlock();
		}
	}

	@Test
	public void testInstallAndKill() throws InterruptedException {
		for (AndroidDevice device : getDevices()) {
			File xiaomi = new File("d:\\apkpack\\Ctrip_V5.10_SIT7.2_TEST.apk");
			AndroidApp app = new DefaultAndroidApp(xiaomi);
			device.unlock();
			device.uninstall(app);
			device.install(app);
			device.start(app);
			Thread.sleep(3000);
			device.kill(app);
		}
	}
}
