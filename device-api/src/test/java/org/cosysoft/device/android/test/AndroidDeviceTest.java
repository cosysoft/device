package org.cosysoft.device.android.test;

import static org.junit.Assert.assertTrue;

import java.util.TreeSet;

import org.cosysoft.device.DeviceStore;
import org.cosysoft.device.android.AndroidDevice;
import org.cosysoft.device.android.impl.AndroidDeviceStore;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * super test for device
 * 
 * @author ltyao
 *
 */
public class AndroidDeviceTest {

	protected static final Logger logger = LoggerFactory
			.getLogger(AndroidDeviceTest.class);
	protected static DeviceStore deviceStore;

	@BeforeClass
	public static void setUp() {
		deviceStore = AndroidDeviceStore.getInstance();
		assertTrue("devices size must > 0", deviceStore.getDevices().size() > 0);
	}

	protected AndroidDevice pollFirst() {
		return deviceStore.getDevices().pollFirst();
	}

	protected TreeSet<AndroidDevice> getDevices() {
		return deviceStore.getDevices();
	}

	@AfterClass
	public static void tearDown() {
		deviceStore.shutdownForcely();
	}
}
