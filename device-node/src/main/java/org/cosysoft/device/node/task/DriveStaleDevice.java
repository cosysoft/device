package org.cosysoft.device.node.task;

import org.cosysoft.device.node.service.MasterDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Created by 兰天 on 2015/4/16.
 */
@Service
public class DriveStaleDevice {

    @Autowired
    private MasterDeviceService deviceService;

    @Scheduled(fixedDelay = 3000)
    public void action() {
        deviceService.deleteStale();
    }

}
