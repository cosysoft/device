package org.cosysoft.device.node.domain;

import org.cosysoft.device.model.DeviceInfo;

/**
 * Created by ltyao on 2015/4/14.
 */
public class Device extends DeviceInfo {

    private String nodeIP;
    private int nodePort;

    public String getNodeIP() {
        return nodeIP;
    }

    public void setNodeIP(String nodeIP) {
        this.nodeIP = nodeIP;
    }

    public int getNodePort() {
        return nodePort;
    }

    public void setNodePort(int nodePort) {
        this.nodePort = nodePort;
    }

    public String getDid() {
        return this.getSerial() + "@" + this.getNodeIP();
    }
}
