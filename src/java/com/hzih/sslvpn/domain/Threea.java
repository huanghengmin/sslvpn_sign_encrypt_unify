package com.hzih.sslvpn.domain;


/**
 * Created by Administrator on 2017/6/7.
 */
public class Threea {
    private int id;
    private String ip;
    private int port;
    private String lowermode;

    public void setPort(Integer port) {
        this.port = port;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getLowermode() {
        return lowermode;
    }

    public void setLowermode(String lowermode) {
        this.lowermode = lowermode;
    }
}
