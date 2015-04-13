package org.cosysoft.device.node.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by 兰天 on 2015/4/13.
 */
@RestController
@RequestMapping("/hub")
public class MasterController {

    private RestTemplate restTemplate = new RestTemplate();

    @RequestMapping("/device/{serialId}/**")
    public Object mirrorRest(@RequestBody(required = false) String body, HttpMethod method, HttpServletRequest request,
                             HttpServletResponse response) throws URISyntaxException {
        URI uri = new URI("http", null, "localhost", 8080, request.getRequestURI().replace("/hub", "/api"), request.getQueryString(), null);

        ResponseEntity<Object> responseEntity =
                restTemplate.exchange(uri, method, new HttpEntity<Object>(body), Object.class);

        return responseEntity.getBody();
    }


}
