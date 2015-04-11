package org.cosysoft.device.node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 兰天 on 2015/4/5.
 */
@SpringBootApplication
@RestController
@ComponentScan(basePackages = "org.cosysoft.device.node")
@EnableScheduling
@RequestMapping("/api/version")
public class Application  {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = SpringApplication.run(Application.class, args);
    }

    @RequestMapping
    public String version() {
        return "0.9.0-SNAPSHOT";
    }


}
