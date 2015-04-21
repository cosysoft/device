package com.github.cosysoft.device.android.test;

import static org.junit.Assert.assertTrue;

import java.util.TreeSet;

import com.github.cosysoft.device.DeviceStore;
import com.github.cosysoft.device.android.AndroidDevice;
import com.github.cosysoft.device.android.impl.AndroidDeviceStore;
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
