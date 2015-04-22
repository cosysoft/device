package com.github.cosysoft.device.node.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

    private static final String[] SERVLET_RESOURCE_LOCATIONS = {"/"};

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/app/"};

    private static final String[] RESOURCE_LOCATIONS;

    static {
        RESOURCE_LOCATIONS = new String[CLASSPATH_RESOURCE_LOCATIONS.length
                + SERVLET_RESOURCE_LOCATIONS.length];
        System.arraycopy(SERVLET_RESOURCE_LOCATIONS, 0, RESOURCE_LOCATIONS, 0,
                SERVLET_RESOURCE_LOCATIONS.length);
        System.arraycopy(CLASSPATH_RESOURCE_LOCATIONS, 0, RESOURCE_LOCATIONS,
                SERVLET_RESOURCE_LOCATIONS.length, CLASSPATH_RESOURCE_LOCATIONS.length);
    }

    private static final String[] STATIC_INDEX_HTML_RESOURCES;

    static {
        STATIC_INDEX_HTML_RESOURCES = new String[RESOURCE_LOCATIONS.length];
        for (int i = 0; i < STATIC_INDEX_HTML_RESOURCES.length; i++) {
            STATIC_INDEX_HTML_RESOURCES[i] = RESOURCE_LOCATIONS[i] + "index.html";
        }
    }

    @Autowired
    private ResourceLoader resourceLoader;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations(RESOURCE_LOCATIONS)
                .setCachePeriod(0);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        addStaticIndexHtmlViewControllers(registry);
    }

    private void addStaticIndexHtmlViewControllers(ViewControllerRegistry registry) {
        for (String resource : STATIC_INDEX_HTML_RESOURCES) {
            if (this.resourceLoader.getResource(resource).exists()) {
                try {
                    logger.info("Adding welcome page: "
                            + this.resourceLoader.getResource(resource).getURL());
                } catch (IOException ex) {
                    // Ignore
                }
                // Use forward: prefix so that no view resolution is done
                registry.addViewController("/").setViewName("forward:index.html");
                return;
            }
        }
    }
}
