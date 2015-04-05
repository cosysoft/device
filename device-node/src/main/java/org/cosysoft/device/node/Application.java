package org.cosysoft.device.node;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 兰天 on 2015/4/5.
 */
@SpringBootApplication
@RestController
@RequestMapping("/api/version")
public class Application {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @RequestMapping
    @ResponseBody
    public String version() {
        return "0.9.0-SNAPSHOT";
    }
}
