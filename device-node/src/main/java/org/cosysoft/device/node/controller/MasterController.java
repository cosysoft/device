package org.cosysoft.device.node.controller;

import org.cosysoft.device.node.domain.Device;
import org.cosysoft.device.node.service.MasterDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 兰天 on 2015/4/13.
 */
@Controller
@RequestMapping("/hub")
public class MasterController {

    private static final Logger LOG = LoggerFactory.getLogger(MasterController.class);
    private static final List<String> byteUrls = Arrays.asList("/avatar", "/screenshot");

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private MasterDeviceService deviceService;

    @RequestMapping("/device/{serialId}/**")
    public ResponseEntity<?> mirrorRest(@RequestBody(required = false) String body, HttpMethod method,
                                        HttpServletRequest request, @PathVariable String serialId) throws URISyntaxException {

        Device device = deviceService.getDeviceByDid(serialId);

        URI uri = new URI("http", null, device.getNodeIP(), device.getNodePort(),
                request.getRequestURI().replace("/hub", "/api"), request.getQueryString(), null);

        Class<?> responseType = String.class;
        for (String part : byteUrls) {
            if (request.getRequestURI().contains(part)) {
                responseType = byte[].class;
                break;
            }
        }
        ResponseEntity<?> responseEntity =
                restTemplate.exchange(uri, method, new HttpEntity<Object>(body), responseType);

        return responseEntity;
    }

    @RequestMapping(value = "/device")
    @ResponseBody
    public List<Device> devices() {
        return deviceService.getDevices();
    }

    @RequestMapping(value = "/register", method = {RequestMethod.POST})
    @ResponseBody
    public String register(@RequestBody List<Device> devices) {
        deviceService.putOrDelete(devices);
        return "success";
    }


}
