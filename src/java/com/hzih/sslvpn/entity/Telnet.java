package com.hzih.sslvpn.entity;

import java.util.List;

/**
 * Created by huanghengmin on 2017/5/12.
 */
public interface Telnet {

    void ipMacVerify(String ethernet, boolean gigabit_boolean, boolean isOpen);

    String arpList();

    String disIpSourceBinding();

    String ipMacBind(String ethernet, boolean gigabit_boolean, String ip, String mac, boolean bind);

    boolean login();

    boolean connect();

    void disconnect();

    List<String> getShellFileLine(String data);
}
