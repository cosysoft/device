package org.cosysoft.device.node.test;

import org.cosysoft.device.node.domain.Device;
import org.junit.Test;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void testRegister() {
        List<Device> devices = new ArrayList<>();
        Device device = new Device();
        device.setName("android-emulator");
        devices.add(device);

        template.postForObject("http://localhost:8080/hub/register", devices, String.class);

    }
}


