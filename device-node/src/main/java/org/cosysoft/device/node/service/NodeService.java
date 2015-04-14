package org.cosysoft.device.node.service;

import org.cosysoft.device.exception.NestedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by ltyao on 2015/4/14.
 */
@Service
public class NodeService {

    @Value("${server.port:8080}")
    private int port;

    public String getIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new NestedException("", e);
        }
    }

    public String getName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            throw new NestedException("", e);
        }
    }

    public int getPort() {
        return port;
    }
}
