package org.cosysoft.device.node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * Created by 兰天 on 2015/4/5.
 */
@SpringBootApplication
@RestController
@ComponentScan(basePackages = "org.cosysoft.device.node")
@EnableScheduling
@RequestMapping("/api/version")
@EnableCaching
public class Application extends CachingConfigurerSupport {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = SpringApplication.run(Application.class, args);
    }

    @RequestMapping
    public String version() {
        return "0.9.0-SNAPSHOT";
    }

    @Bean
    @Override
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache("avatars"),
                new ConcurrentMapCache("devices"), new ConcurrentMapCache("default")));

        return cacheManager;
    }
}
