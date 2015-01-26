package org.cosysoft.device.android.impl;

import java.util.Set;
import java.util.TreeSet;

import org.cosysoft.device.DeviceStore;
import org.cosysoft.device.android.AndroidDevice;
import org.cosysoft.device.exception.AndroidDeviceException;
import org.cosysoft.device.exception.DeviceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AndroidDeviceStore
		implements DeviceStore {
	
	protected static final Logger log = LoggerFactory
			.getLogger(AndroidDeviceStore.class);

	protected final TreeSet<AndroidDevice> devicesInUse = new TreeSet<>();
	protected final TreeSet<AndroidDevice> devices = new TreeSet<>();

	DefaultDeviceManager deviceManager = new DefaultDeviceManager(true);

	static class StoreHolder {

		static final AndroidDeviceStore instance = init();

		static AndroidDeviceStore init() {
			AndroidDeviceStore instance;
			instance = new AndroidDeviceStore();
			instance.initAndroidDevices(false);
			return instance;
		}
	}

	public static AndroidDeviceStore getInstance() {
		return StoreHolder.instance;
	}

	/**
	 * call once
	 * 
	 * @param shouldKeepAdbAlive
	 */
	public void initAndroidDevices(boolean shouldKeepAdbAlive)
			throws AndroidDeviceException {

		deviceManager.initializeAdbConnection();
		Set<AndroidDevice> androidDevices = deviceManager.getAndroidDevices();

		if (androidDevices.isEmpty()) {
			throw new DeviceNotFoundException(
					"No android devices were found. Please start an Android hardware device via USB, "
							+ "or check other device offline problem such as open USB Debug");
		}
		devices.addAll(androidDevices);
		devicesInUse.addAll(androidDevices);

	}

	@Override
	public void shutdown() {
		deviceManager.shutdown();
	}

	@Override
	public void shutdownForcely() {
		deviceManager.shutdownForcely();

	}

	@Override
	public TreeSet<AndroidDevice> getDevices() {
		return new TreeSet<AndroidDevice>(devices);
	}
}
