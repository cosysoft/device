package org.cosysoft.device.node.test;

import org.junit.Test;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

/**
 * Created by 兰天 on 2015/4/5.
 */
public class ApplicationTest {

    RestTemplate template = new TestRestTemplate();

    @Test
    public void testVersion() {
        String version = template.getForEntity("http://localhost:8080/api/version", String.class).getBody();
        assertThat(version, containsString("SNAPSHOT"));
    }
}


