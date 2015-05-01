package com.github.cosysoft.device.node.domain;

import com.github.cosysoft.device.model.DeviceInfo;

import java.util.Date;

/**
 * Created by ltyao on 2015/4/14.
 */
public class Device extends DeviceInfo {

    private String nodeIP;
    private int nodePort;
    private Date lastRegisterDate = new Date();


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

    public Date getLastRegisterDate() {
        return lastRegisterDate;
    }

    public void setLastRegisterDate(Date lastRegisterDate) {
        this.lastRegisterDate = lastRegisterDate;
    }

    public String getAvatarUri() {
        return "hub/device/" + this.getDid() + "/avatar";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Device device = (Device) o;

        return getNodeIP().equals(device.getNodeIP());

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getNodeIP().hashCode();
        return result;
    }

    public String getDid() {
        return this.getSerial() + "@" + this.getNodeIP();
    }
}
