package com.github.cosysoft.device;

import java.util.TreeSet;

import com.github.cosysoft.device.android.AndroidDevice;

/**
 * main class for phone resouces take and release
 *
 * @author ltyao
 */
public interface DeviceStore {

    void shutdown();

    void shutdownForcely();

    /**
     * internal usage
     *
     * @return
     */
    TreeSet<AndroidDevice> getDevices();

    AndroidDevice getDeviceBySerial(String serialID);
}
