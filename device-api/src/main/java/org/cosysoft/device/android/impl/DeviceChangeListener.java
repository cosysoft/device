package org.cosysoft.device.android.impl;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import org.cosysoft.device.android.AndroidDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;

/**
 *
 */
public class DeviceChangeListener implements AndroidDebugBridge.IDeviceChangeListener {

    private static final Logger logger = LoggerFactory
            .getLogger(DeviceChangeListener.class);
    private final Map<IDevice, AndroidDevice> connectedDevices;

    public DeviceChangeListener(Map<IDevice, AndroidDevice> connectedDevices) {
        this.connectedDevices = connectedDevices;
    }

    @Override
        public void deviceConnected(IDevice device) {
            logger.info("deviceConnected {}", device.getSerialNumber());
            AndroidDevice ad = new DefaultHardwareDevice(device);
            Iterator<Map.Entry<IDevice, AndroidDevice>> entryIterator = connectedDevices.entrySet().iterator();
            boolean contain = false;
            while (entryIterator.hasNext()) {
                Map.Entry entry = entryIterator.next();
                if (entry.getValue().equals(ad)) {
                    contain = true;
                    break;
                }
            }
            if (!contain) {
                connectedDevices.put(device, ad);
            }
        }

        @Override
        public void deviceDisconnected(IDevice device) {
            logger.info("deviceDisconnected {}", device.getSerialNumber());
            AndroidDevice ad = new DefaultHardwareDevice(device);
            Iterator<Map.Entry<IDevice, AndroidDevice>> entryIterator = connectedDevices.entrySet().iterator();
            while (entryIterator.hasNext()) {
                Map.Entry entry = entryIterator.next();
                if (entry.getValue().equals(ad)) {
                    entryIterator.remove();
                }
            }
        }

        @Override
        public void deviceChanged(IDevice device, int changeMask) {
            logger.info(device.getSerialNumber() + " " + changeMask);
        }
    }