package org.cosysoft.device.node.service;

import org.cosysoft.device.exception.DeviceNotFoundException;
import org.cosysoft.device.node.domain.Device;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by ltyao on 2015/4/14.
 */
@Service
public class MasterDeviceService {

    private List<Device> devices = new CopyOnWriteArrayList<>();

    public List<Device> getDevices() {
        return devices;
    }

    public Device getDeviceByDid(String did) {
        for (Device device : devices) {
            if (device.getDid().equals(did)) {
                return device;
            }
        }
        throw new DeviceNotFoundException(String.format("with did %s", did));
    }

    public void putOrDelete(List<Device> ds) {
        if (ds.size() < 0) {
            return;
        }
        String ip = ds.get(0).getNodeIP();
        for (Device device : devices) {
            if (device.getNodeIP().equals(ip)) {
                devices.remove(device); //safe for COW
            }
        }
        devices.addAll(ds);
    }

}
