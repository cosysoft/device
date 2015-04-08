package org.cosysoft.device.node.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


/**
 * Created by 兰天 on 2015/4/8.
 */

@Service
public class NodeRegister {

    private static final Logger LOG = LoggerFactory.getLogger(NodeRegister.class);

    @Value("${keeper.register}")
    private String registerUrl;

    @Scheduled(fixedDelay = 1000)
    public void register() {
        LOG.info("register to {}", registerUrl);
    }

}
