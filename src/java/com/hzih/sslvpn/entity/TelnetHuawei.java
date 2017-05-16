package com.hzih.sslvpn.entity;

import org.apache.commons.net.telnet.TelnetClient;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TelnetHuawei implements Telnet {

    private TelnetClient telnet = new TelnetClient("VT100");

    private InputStream in;

    private PrintStream out;

    private static final String ENTER_COMMAND_ARROW = ">";
    private static final String ENTER_COMMAND_BRACKETS = "]";
    private static final String ENTER_COMMAND_MORE = "---- More ----";
    private static final String ENTER_LOGIN = "Username:";
    private static final String ENTER_PASSWORD = "Password:";
    private static final String ENTER_SYMBOL = "\\[42D";
    private static final String ENTER_YN = "[Y/N]";

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


    public TelnetHuawei(String ip, String port, String user, String password) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.password = password;
    }


    @Override
    public void ipMacVerify(String ethernet, boolean gigabit_boolean, boolean isOpen) {
        write("system-view");
        String msg = readUntil(ENTER_COMMAND_BRACKETS);
        System.out.print(msg);
        if (gigabit_boolean) {
            write("interface GigabitEthernet " + ethernet);
        } else {
            write("interface Ethernet " + ethernet);
        }
        msg = readUntil(ENTER_COMMAND_BRACKETS);
        System.out.print(msg);
        if (isOpen) {
            write("ip source check user-bind enable");
        } else {
            write("undo ip source check user-bind enable");
        }
        msg = readUntil(ENTER_COMMAND_BRACKETS);
        System.out.print(msg);
        write("quit");
        msg = readUntil(ENTER_COMMAND_BRACKETS);
        System.out.print(msg);
        write("quit");
        msg = readUntil(ENTER_COMMAND_ARROW);
        System.out.print(msg);
        write("save");
        msg = readUntil(ENTER_YN);
        System.out.print(msg);
        write("Y");
        msg = readUntil(ENTER_COMMAND_ARROW);
        System.out.print(msg);
    }

    @Override
    public String arpList() {
        String result = null;
        write("dis arp");
        String msg = readAll();
        System.out.println(msg);
        return msg;
    }

    @Override
    public String disIpSourceBinding() {
        String result = null;
        write("system-view");
        String msg = readUntil(ENTER_COMMAND_BRACKETS);
        System.out.print(msg);
        write("dis dhcp static user-bind all");
        result = readUntil(ENTER_COMMAND_BRACKETS);
        System.out.print(result);
        write("quit");
        msg = readUntil(ENTER_COMMAND_ARROW);
        System.out.print(msg);
        return result;
    }

    @Override
    public String ipMacBind(String ethernet, boolean gigabit_boolean, String ip, String mac, boolean bind) {
        String result = null;
        write("system-view");
        String msg = readUntil(ENTER_COMMAND_BRACKETS);
        String eth = "Ethernet";
        if (gigabit_boolean) {
            eth = "GigabitEthernet";
        }
        System.out.print(msg);
        if (bind) {
            write("user-bind static ip-address " + ip + " mac-address " + mac + " interface " + eth + " " + ethernet);
        } else {
            write("undo user-bind static ip-address " + ip + " mac-address " + mac + " interface " + eth + " " + ethernet);
        }
        msg = readUntil(ENTER_COMMAND_BRACKETS);
        System.out.print(msg);
        write("quit");
        msg = readUntil(ENTER_COMMAND_ARROW);
        System.out.print(msg);
        write("save");
        msg = readUntil(ENTER_YN);
        System.out.print(msg);
        write("Y");
        msg = readUntil(ENTER_COMMAND_ARROW);
        System.out.print(msg);
        return result;
    }

    @Override
    public boolean login() {
        String msg = readUntil(ENTER_LOGIN);
        System.out.println(msg);
        write(user);
        msg = readUntil(ENTER_PASSWORD);
        System.out.print(msg);
        write(password);
        String result = readUntil(ENTER_COMMAND_ARROW);
        System.out.print(result);
        if (result != null) {
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


    public String readAll() {
        StringBuffer sb = new StringBuffer();
        try {
            int i =-1;
            while ((i =in.read()) != -1){
                char ch = (char) i;
                 sb.append(ch);
                if(sb.toString().endsWith(ENTER_COMMAND_ARROW)){
                    return sb.toString().replaceAll(ENTER_COMMAND_MORE,"").replaceAll(ENTER_SYMBOL,"");
                }
                 if(sb.toString().endsWith(ENTER_COMMAND_MORE)){
                     write("\n");
                 }
            }
            return sb.toString().replaceAll(ENTER_COMMAND_MORE,"").replaceAll(ENTER_SYMBOL,"");
        } catch (Exception e) {
           e.printStackTrace();
            return sb.toString().replaceAll(ENTER_COMMAND_MORE,"").replaceAll(ENTER_SYMBOL,"");
        }
    }


    public String readLine() {
        try {
            StringBuffer sb = new StringBuffer();
            char ch = (char) in.read();
            while (true) {
                sb.append(ch);
                if (ch == '\n') {
                    return sb.toString();
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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return strings;
    }


    public static void main(String[] args) {
       /* String ss ="[Quidway]dis arp\n" +
                "            IP ADDRESS      MAC ADDRESS     EXPIRE(M) TYPE INTERFACE      VPN-INSTANCE\n" +
                "            VLAN\n" +
                "                    ------------------------------------------------------------------------------\n" +
                "            172.16.2.179    688f-8406-0bee            I -  Vlanif1\n" +
                "            172.16.2.1      0090-27fe-3c9f  20        D-0  GE0/0/2\n" +
                "            1\n" +
                "            172.16.2.178    ac74-0960-3a94  10        D-0  GE0/0/2\n" +
                "            1\n" +
                "            172.16.2.9      00e0-b610-70df  14        D-0  GE0/0/1\n" +
                "            1\n" +
                "            172.16.2.33     00e0-4c69-0ba2  13        D-0  GE0/0/2\n" +
                "            1\n" +
                "            172.16.2.5      1c6f-6561-b7f4  3         D-0  GE0/0/2\n" +
                "            1\n" +
                "            172.16.2.122    00e0-4c36-01d0  15        D-0  GE0/0/2\n" +
                "            1\n" +
                "            172.16.2.30     00e0-6670-b786  7         D-0  GE0/0/2\n" +
                "            1\n" +
                "            172.16.2.29     eca8-6b70-98a9  14        D-0  GE0/0/2\n" +
                "            1\n" +
                "            172.16.2.2      00e0-6633-c2e6  8         D-0  GE0/0/2\n" +
                "            1\n" +
                "            172.16.2.12     94de-805c-2cf3  8         D-0  GE0/0/2\n" +
                "            1\n" +
                "            172.16.2.41     00e0-4c68-5c8a  4         D-0  GE0/0/2\n" +
                "            1\n" +
                "            172.16.2.181    00e0-6901-5913  15        D-0  GE0/0/2\n" +
                "            1\n" +
                "            172.16.2.182    507b-9d97-a145  17        D-0  GE0/0/2\n" +
                "            1\n" +
                "                    ------------------------------------------------------------------------------\n" +
                "            Total:14        Dynamic:13      Static:0     Interface:1\n" +
                "            [Quidway]";

        List<ArpEntityHuawei> arpEntities = new ArrayList<ArpEntityHuawei>();
        List<String> lines = getShellFileLine(ss);
        for (String s : lines) {
            String[] cols = s.trim().split("\\s+");
            if (cols.length == 5 ) {
                ArpEntityHuawei arpEntity = new ArpEntityHuawei();
                arpEntity.setIpAddress(cols[0]);
                arpEntity.setMacAddress(cols[1]);
                arpEntity.setExpire(cols[2]);
                arpEntity.setType(cols[3]);
                arpEntity.setInet(cols[4]);
                arpEntities.add(arpEntity);
            }
        }
        System.out.println(arpEntities.size());*/


      /* String ss = "[Quidway]dis dhcp static user-bind all\n" +
               "            DHCP static Bind-table:\n" +
               "            Flags:O - outer vlan ,I - inner vlan ,P - Vlan-mapping\n" +
               "            IP Address                      MAC Address     VSI/VLAN(O/I/P) Interface\n" +
               "                    --------------------------------------------------------------------------------\n" +
               "            172.16.2.9                      00e0-b610-70df  --  /--  /--    GE0/0/1\n" +
               "                    --------------------------------------------------------------------------------\n" +
               "            Print count:           1          Total count:           1\n" +
               "            [Quidway]";

        List<ArpEntityHuawei> arpEntities = new ArrayList<ArpEntityHuawei>();
        List<String> lines = getShellFileLine(ss);
        for (String s : lines) {
            String[] cols = s.trim().split("\\s+");
            if (cols.length == 6 && !cols[0].equals("IP")&&!cols[0].equals("Print")) {
                ArpEntityHuawei arpEntity = new ArpEntityHuawei();
                arpEntity.setIpAddress(cols[0]);
                arpEntity.setMacAddress(cols[1]);
                arpEntity.setInet(cols[5]);
                arpEntities.add(arpEntity);
            }
        }
        System.out.println(arpEntities.size());*/


        try {
            TelnetHuawei telnet = new TelnetHuawei("172.16.2.179", "23", "telnet", "telnet@123");
            telnet.connect();
            boolean flag = telnet.login();
            if (flag) {
               /* String arpList = telnet.arpList();
                List<ArpEntity> arpEntities = new ArrayList<ArpEntity>();
                List<String> lines = telnet.getShellFileLine(arpList);
                for (String s : lines) {
                    String[] cols = s.trim().split("\\s+");
                    if (cols.length == 5 ) {
                        ArpEntity arpEntity = new ArpEntity();
                        arpEntity.setIpAddress(cols[0]);
                        arpEntity.setMacAddress(cols[1]);
                        arpEntity.setExpire(cols[2]);
                        arpEntity.setType(cols[3]);
                        arpEntity.setInet(cols[4]);
                        arpEntities.add(arpEntity);
                    }
                }
                System.out.println(arpEntities.size());*/
                //telnet.ipMacVerify("0/0/1",true,false);
                //telnet.ipMacBind("0/0/1",true,"172.16.2.9","00e0-b610-70df",false);
                String dis  = telnet.disIpSourceBinding();
                System.out.print(dis);

                List<ArpEntity> arpEntities = new ArrayList<ArpEntity>();
                List<String> lines = telnet.getShellFileLine(dis);
                for (String s : lines) {
                    String[] cols = s.trim().split("\\s+");
                    if (cols.length == 6 && !cols[0].equals("IP")&&!cols[0].equals("Print")) {
                        ArpEntity arpEntity = new ArpEntity();
                        arpEntity.setIpAddress(cols[0]);
                        arpEntity.setMacAddress(cols[1]);
                        arpEntity.setInet(cols[5]);
                        arpEntities.add(arpEntity);
                    }
                }
                System.out.println(arpEntities.size());

            }
            telnet.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}