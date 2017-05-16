package com.hzih.sslvpn.entity;

import org.apache.commons.net.telnet.TelnetClient;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TelnetH3c implements Telnet{
	
    private TelnetClient telnet = new TelnetClient("VT100");

    private InputStream in;

    private PrintStream out;

    private static final String ENTER_COMMAND_ARROW = ">";
    private static final String ENTER_COMMAND_BRACKETS = "]";
    private static final String ENTER_LOGIN = "login:";
    private static final String ENTER_PASSWORD = "Password:";
    private static final String ENTER_YN = "[Y/N]:";
    private static final String ENTER_KEY = "key):";

    /**
     * telnet 端口
     */
    private String port;

    /**
     * 用户名
     */
    private String user;

    /**
     * 密码
     */
    private String password;

    /**
     * IP 地址
     */
    private String ip;


    public TelnetH3c(String ip, String port, String user, String password) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    @Override
    public void ipMacVerify(String ethernet, boolean gigabit_boolean, boolean isOpen){
        write("system-view");
        String msg=readUntil(ENTER_COMMAND_BRACKETS);
        System.out.print(msg);
        if(gigabit_boolean) {
            write("interface GigabitEthernet "+ethernet);
        }else {
            write("interface Ethernet "+ethernet);
        }
        msg=readUntil(ENTER_COMMAND_BRACKETS);
        System.out.print(msg);
        if(isOpen) {
            write("ip verify source ip-address mac-address");
        }else {
            write("undo ip verify source");
        }
        msg=readUntil(ENTER_COMMAND_BRACKETS);
        System.out.print(msg);
        write("save");
        msg=readUntil(ENTER_YN);
        System.out.print(msg);
        write("y");
        msg=readUntil(ENTER_KEY);
        System.out.print(msg);
        write("\n");
        msg=readUntil(ENTER_YN);
        System.out.print(msg);
        write("y");
        msg=readUntil(ENTER_COMMAND_BRACKETS);
        System.out.print(msg);
        write("quit");
        msg=readUntil(ENTER_COMMAND_BRACKETS);
        System.out.print(msg);
        write("quit");
        msg=readUntil(ENTER_COMMAND_ARROW);
        System.out.print(msg);
    }

    @Override
    public String arpList(){
        String result =null;
        write("system-view");
        String msg=readUntil(ENTER_COMMAND_BRACKETS);
        System.out.print(msg);
        write("dis arp");
        result=readUntil(ENTER_COMMAND_BRACKETS);
        System.out.print(result);
        write("quit");
        msg=readUntil(ENTER_COMMAND_ARROW);
        System.out.print(msg);
        return result;
    }

    @Override
    public String disIpSourceBinding(){
        String result =null;
        write("system-view");
        String msg=readUntil(ENTER_COMMAND_BRACKETS);
        System.out.print(msg);
        /*if(gigabit_boolean) {
            write("interface GigabitEthernet "+ethernet);
        }else {
            write("interface Ethernet "+ethernet);
        }*/
        /*msg=readUntil(ENTER_COMMAND_BRACKETS);
        System.out.print(msg);*/
        write("dis ip source binding");
        result=readUntil(ENTER_COMMAND_BRACKETS);
        System.out.print(result);
       /* write("quit");
        msg=readUntil(ENTER_COMMAND_BRACKETS);
        System.out.print(msg);*/
        write("quit");
        msg=readUntil(ENTER_COMMAND_ARROW);
        System.out.print(msg);
        return result;
    }
    @Override
    public String ipMacBind(String ethernet, boolean gigabit_boolean, String ip, String mac, boolean bind){
        String result =null;
        write("system-view");
        String msg=readUntil(ENTER_COMMAND_BRACKETS);
        System.out.print(msg);
        if(gigabit_boolean) {
            write("interface GigabitEthernet "+ethernet);
        }else {
            write("interface Ethernet "+ethernet);
        }
        msg=readUntil(ENTER_COMMAND_BRACKETS);
        System.out.print(msg);
        if(bind) {
            write("ip source binding ip-address " + ip + " mac-address " + mac);
        }else {
            write("undo ip source binding ip-address "+ip+" mac-address "+mac);
        }
        result=readUntil(ENTER_COMMAND_BRACKETS);
        System.out.print(result);
        write("save");
        msg=readUntil(ENTER_YN);
        System.out.print(msg);
        write("y");
        msg=readUntil(ENTER_KEY);
        System.out.print(msg);
        write("\n");
        msg=readUntil(ENTER_YN);
        System.out.print(msg);
        write("y");
        msg=readUntil(ENTER_COMMAND_BRACKETS);
        System.out.print(msg);
        write("quit");
        msg=readUntil(ENTER_COMMAND_BRACKETS);
        System.out.print(msg);
        write("quit");
        msg=readUntil(ENTER_COMMAND_ARROW);
        System.out.print(msg);
        return result;
    }

    @Override
    public boolean login(){
        String msg=readUntil(ENTER_LOGIN);
        System.out.println(msg);
        write(user);
        msg=readUntil(ENTER_PASSWORD);
        System.out.print(msg);
        write(password);
        String result=readUntil(ENTER_COMMAND_ARROW);
        System.out.print(result);
        if(result!=null){
            return true;
        }
        return false;
    }

    /**
     * @return boolean 连接成功返回true，否则返回false
     */
    @Override
    public boolean connect() {
        boolean isConnect = true;
        try {
            telnet.connect(ip, Integer.parseInt(port));
            in = telnet.getInputStream();
            out = new PrintStream(telnet.getOutputStream());
            telnet.setKeepAlive(true);
        } catch (Exception e) {
            isConnect = false;
            e.printStackTrace();
            return isConnect;
        }
        return isConnect;
    }

    public String readUntil(String pattern) {
        try {
            char lastChar = pattern.charAt(pattern.length() - 1);
            StringBuffer sb = new StringBuffer();
            char ch = (char) in.read();
            while (true) {
                sb.append(ch);
                if (ch == lastChar) {
                    if (sb.toString().endsWith(pattern)) {
                        return sb.toString();
                    }
                }
                ch = (char) in.read();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void write(String value) {
        try {
            out.println(value);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() {
        try {
            telnet.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public List<String> getShellFileLine(String data) {
        List<String> strings = new ArrayList<String>();
        BufferedReader br = null;
        String str = null;
        try {
            br = new BufferedReader(new StringReader(data));
            while ((str = br.readLine()) != null) {
                strings.add(str);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if(br != null){
                try {
                    br.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return strings;
    }


    public static void main(String[] args) {
        /*String ss ="dis arp\n" +
                "\n" +
                "        IP address      MAC address    VLAN     Interface                Aging Type\n" +
                "        172.16.2.3      00e0-4c68-cc59 1        GE1/0/1                  7     D\n" +
                "        172.16.2.5      1c6f-6561-b7f4 1        GE1/0/1                  19    D\n" +
                "        172.16.2.7      00e0-4c68-cc48 1        GE1/0/1                  4     D\n" +
                "        172.16.2.8      00e0-4c68-cc54 1        GE1/0/1                  14    D\n" +
                "        172.16.2.12     94de-805c-2cf3 1        GE1/0/1                  2     D\n" +
                "        172.16.2.17     6636-6639-3937 1        GE1/0/1                  3     D\n" +
                "        172.16.2.22     00e0-4c68-5c46 1        GE1/0/1                  13    D\n" +
                "        172.16.2.29     eca8-6b70-98a9 1        GE1/0/1                  16    D\n" +
                "        172.16.2.30     00e0-6670-b786 1        GE1/0/1                  16    D\n" +
                "        172.16.2.31     00e0-4c68-cc45 1        GE1/0/1                  13    D\n" +
                "        172.16.2.33     00e0-4c69-0ba2 1        GE1/0/1                  16    D\n" +
                "        172.16.2.35     00e0-4c69-0f24 1        GE1/0/1                  6     D\n" +
                "        172.16.2.39     00e0-6901-ab6d 1        GE1/0/1                  6     D\n" +
                "        172.16.2.41     00e0-4c68-5c8a 1        GE1/0/1                  13    D\n" +
                "        172.16.2.91     00e0-6901-d023 1        GE1/0/1                  19    D\n" +
                "        172.16.2.122    00e0-4c36-01d0 1        GE1/0/1                  20    D\n" +
                "                [H3C]";

        List<ArpEntityH3c> arpEntities = new ArrayList<ArpEntityH3c>();
        List<String> lines = getShellFileLine(ss);
        for (String s : lines) {
            String[] cols = s.trim().split("\\s+");
            if (cols.length == 6 && !s.startsWith("IP address")) {
                ArpEntityH3c arpEntity = new ArpEntityH3c();
                arpEntity.setIpAddress(cols[0]);
                arpEntity.setMacAddress(cols[1]);
                arpEntity.setVlan(cols[2]);
                arpEntity.setInet(cols[3]);
                arpEntity.setAging(cols[4]);
                arpEntity.setType(cols[5]);
                arpEntities.add(arpEntity);
            }
        }
        System.out.println(arpEntities.size());*/

        try {
        	TelnetH3c telnet = new TelnetH3c("172.16.2.178", "23","telnet", "telnet");
        	telnet.connect();
        	boolean flag = telnet.login();
        	if(flag) {
                //String arpList = telnet.arpList();
                telnet.ipMacVerify("1/0/2",true,false);
                //telnet.ipMacBind("1/0/2",true,"172.16.2.9","00e0-b610-70df",false);

            }
            telnet.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}