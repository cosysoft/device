package org.cosysoft.device.node.controller;

import org.cosysoft.device.model.DeviceInfo;
import org.cosysoft.device.node.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * Created by 兰天 on 2015/4/8.
 */
@RestController
@RequestMapping("/api/device")
public class DeviceController {

    @Autowired
    DeviceService deviceService;

    @RequestMapping
    public Collection<DeviceInfo> devices() {
        return deviceService.getDevices();
    }

    @RequestMapping(value = "/{serialId}/avatar", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] avatar(@PathVariable String serialId) {
        return deviceService.getAvatar(serialId);
    }

    @RequestMapping(value = "/{serialId}/screenshot", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] screenshot(@PathVariable String serialId) {
        return deviceService.takeScreenShot(serialId);
    }
}
