package com.github.cosysoft.device.node.task;

import com.github.cosysoft.device.node.service.DeviceService;
import com.github.cosysoft.device.node.domain.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;


/**
 * Created by 兰天 on 2015/4/8.
 */

@Service
public class NodeRegister {

    private static final Logger LOG = LoggerFactory.getLogger(NodeRegister.class);

    @Autowired
    private DeviceService deviceService;

    private RestTemplate restTemplate = new RestTemplate();


    @Value("${keeper.register}")
    private String registerUrl;

    @Scheduled(fixedDelay = 1000)
    public void register() {

        List<Device> devices = deviceService.getDevices();
        LOG.info("register to {} with devices{}", registerUrl, devices);
        restTemplate.postForObject(registerUrl, devices, String.class);

    }

}
