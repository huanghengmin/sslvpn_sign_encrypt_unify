package com.hzih.sslvpn.entity;

/**
 * Created by huanghengmin on 2017/4/24.
 */
public class ArpEntity {
    private String ipAddress;
    private String macAddress;
    private String vlan;
    private String inet;
    private String aging;
    private String type;
    private String expire;

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getVlan() {
        return vlan;
    }

    public void setVlan(String vlan) {
        this.vlan = vlan;
    }

    public String getInet() {
        return inet;
    }

    public void setInet(String inet) {
        this.inet = inet;
    }

    public String getAging() {
        return aging;
    }

    public void setAging(String aging) {
        this.aging = aging;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

}
