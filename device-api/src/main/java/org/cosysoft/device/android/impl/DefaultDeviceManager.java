package org.cosysoft.device.android.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.cosysoft.device.android.AndroidDevice;
import org.cosysoft.device.exception.NestedException;
import org.cosysoft.device.shell.AndroidSdk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.IDevice.DeviceState;

/**
 * reviewed
 * 
 * @author ltyao
 *
 */
public class DefaultDeviceManager {
	private static final Logger logger = LoggerFactory
			.getLogger(DefaultDeviceManager.class);
	private Map<IDevice, DefaultHardwareDevice> connectedDevices = new HashMap<>();

	private AndroidDebugBridge bridge;
	private boolean shouldKeepAdbAlive;

	public DefaultDeviceManager(boolean shouldKeepAdbAlive) {
		this.shouldKeepAdbAlive = shouldKeepAdbAlive;
	}

	/**
	 * Initializes the AndroidDebugBridge and registers the
	 * DefaultHardwareDeviceManager with the AndroidDebugBridge device change
	 * listener.
	 */
	protected void initializeAdbConnection() {
		// Get a device bridge instance. Initialize, create and restart.
		try {
			AndroidDebugBridge.init(false);
		} catch (IllegalStateException e) {
			// When we keep the adb connection alive the AndroidDebugBridge may
			// have been already
			// initialized at this point and it generates an exception. Do not
			// print it.
			if (!shouldKeepAdbAlive) {
				logger.error(
						"The IllegalStateException is not a show "
								+ "stopper. It has been handled. This is just debug spew. Please proceed.",
						e);
				throw new NestedException("ADB init failed", e);
			}
		}

		bridge = AndroidDebugBridge.getBridge();

		if (bridge == null) {
			bridge = AndroidDebugBridge.createBridge(AndroidSdk.adb()
					.getAbsolutePath(), false);
		}

		long timeout = System.currentTimeMillis() + 60000;
		while (!bridge.hasInitialDeviceList()
				&& System.currentTimeMillis() < timeout) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		// Add the existing devices to the list of devices we are tracking.
		IDevice[] devices = bridge.getDevices();
		logger.info("initialDeviceList size {}", devices.length);
		for (int i = 0; i < devices.length; i++) {
			logger.info("devices state: {},{} ", devices[i].getName(),
					devices[i].getState());
			if (DeviceState.ONLINE == devices[i].getState()) {
				connectedDevices.put(devices[i], new DefaultHardwareDevice(
						devices[i]));
			}

		}

	}

	public Set<AndroidDevice> getAndroidDevices() {
		Set<AndroidDevice> devices = new TreeSet<AndroidDevice>(
				connectedDevices.values());
		return devices;

	}

	/**
	 * Shutdown the AndroidDebugBridge and clean up all connected devices.
	 */
	public void shutdown() {
		if (!shouldKeepAdbAlive) {
			AndroidDebugBridge.disconnectBridge();
			AndroidDebugBridge.terminate();
		}
		logger.info("stopping Device Manager");
	}

	/**
	 * used with caution or don't call this method
	 */
	public void shutdownForcely() {
		AndroidDebugBridge.disconnectBridge();
		AndroidDebugBridge.terminate();
	}
}
