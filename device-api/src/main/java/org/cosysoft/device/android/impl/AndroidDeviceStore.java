package org.cosysoft.device.android.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import org.cosysoft.device.DeviceStore;
import org.cosysoft.device.android.AndroidDevice;
import org.cosysoft.device.exception.AndroidDeviceException;
import org.cosysoft.device.exception.DeviceNotFoundException;
import org.cosysoft.device.exception.NestedException;
import org.cosysoft.device.shell.AndroidSdk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.IDevice.DeviceState;

public class AndroidDeviceStore implements DeviceStore {

	protected static final Logger logger = LoggerFactory
			.getLogger(AndroidDeviceStore.class);

	protected final TreeSet<AndroidDevice> devices = new TreeSet<>();
	private Map<IDevice, DefaultHardwareDevice> connectedDevices = new HashMap<>();

	private AndroidDebugBridge bridge;
	private boolean shouldKeepAdbAlive;

	static class DeviceStoreHolder {

		static final AndroidDeviceStore instance = init();

		static AndroidDeviceStore init() {
			AndroidDeviceStore instance;
			instance = new AndroidDeviceStore();
			instance.initAndroidDevices(true);
			return instance;
		}
	}

	public static AndroidDeviceStore getInstance() {
		return DeviceStoreHolder.instance;
	}

	/**
	 * call once
	 * 
	 * @param shouldKeepAdbAlive
	 */
	public void initAndroidDevices(boolean shouldKeepAdbAlive)
			throws AndroidDeviceException {

		this.initializeAdbConnection();
		TreeSet<AndroidDevice> androidDevices = this.getDevices();

		if (androidDevices.isEmpty()) {
			throw new DeviceNotFoundException(
					"No android devices were found. Please start an Android hardware device via USB, "
							+ "or check other device offline problem such as open USB Debug");
		}
		devices.addAll(androidDevices);

	}

	/**
	 * Initializes the AndroidDebugBridge and registers the
	 * DefaultHardwareDeviceManager with the AndroidDebugBridge device change
	 * listener.
	 */
	protected void initializeAdbConnection() {
		// Get a device bridge instance. Initialize, create and restart.
		try {
			AndroidDebugBridge.init(true);
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
		this.devices.addAll(connectedDevices.values());
	}

	@Override
	public TreeSet<AndroidDevice> getDevices() {
		return new TreeSet<AndroidDevice>(devices);
	}

	/**
	 * Shutdown the AndroidDebugBridge and clean up all connected devices.
	 */
	@Override
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
	@Override
	public void shutdownForcely() {
		AndroidDebugBridge.disconnectBridge();
		AndroidDebugBridge.terminate();
	}
}
