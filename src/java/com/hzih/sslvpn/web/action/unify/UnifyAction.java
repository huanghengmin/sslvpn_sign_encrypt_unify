package com.hzih.sslvpn.web.action.unify;

import com.hzih.sslvpn.entity.ArpEntity;
import com.hzih.sslvpn.entity.Telnet;
import com.hzih.sslvpn.entity.TelnetH3c;
import com.hzih.sslvpn.entity.TelnetHuawei;
import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.web.SessionUtils;
import com.hzih.sslvpn.web.action.ActionBase;
import com.hzih.sslvpn.web.action.sslvpn.set.SourceNetsAction;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by huanghengmin on 2017/4/11.
 */
public class UnifyAction extends ActionSupport {
    private Logger logger = Logger.getLogger(SourceNetsAction.class);
    private LogService logService;
    private ArpEntity arpEntity;

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    private int start;
    private int limit;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String add() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false,msg:'添加失败'}";
        String msg = null;
        String ip = UnifyXMLUtils.getValue(UnifyXMLUtils.ip);
        String port = UnifyXMLUtils.getValue(UnifyXMLUtils.port);
        String adm = UnifyXMLUtils.getValue(UnifyXMLUtils.adm);
        String pwd = UnifyXMLUtils.getValue(UnifyXMLUtils.pwd);
        String type = UnifyXMLUtils.getValue(UnifyXMLUtils.type);
        if(ip!=null&&port!=null&&adm!=null&&pwd!=null) {
            Telnet telnet = null;
            if(type.equals("HuaWei")){
                 telnet = new TelnetHuawei(ip, port, adm, pwd);
            }else if(type.equals("H3c")) {
                 telnet = new TelnetH3c(ip, port, adm, pwd);
            }
            if(telnet!=null) {
                try {
                    telnet.connect();
                    boolean f = telnet.login();
                    if (f) {
                        telnet.ipMacBind(arpEntity.getInet().replaceFirst("GE", ""), true, arpEntity.getIpAddress(), arpEntity.getMacAddress(), true);
                        msg = "添加成功";
                        json = "{success:true,msg:'" + msg + "!'}";
                        logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                        logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "资源管理", msg);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    telnet.disconnect();
                }
            }
        }else {
            msg = "请先配置交换机相关信息";
            json = "{success:true,msg:'" + msg + "!'}";
            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "资源管理", msg);

        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String remove() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String json = "{success:false,msg:'删除失败'}";
        String msg = null;
        String ip = UnifyXMLUtils.getValue(UnifyXMLUtils.ip);
        String port = UnifyXMLUtils.getValue(UnifyXMLUtils.port);
        String adm = UnifyXMLUtils.getValue(UnifyXMLUtils.adm);
        String pwd = UnifyXMLUtils.getValue(UnifyXMLUtils.pwd);
        String type = UnifyXMLUtils.getValue(UnifyXMLUtils.type);
        Telnet telnet = null;
        if(ip!=null&&port!=null&&adm!=null&&pwd!=null) {
            if (type.equals("HuaWei")) {
                telnet = new TelnetHuawei(ip, port, adm, pwd);
            } else if (type.equals("H3c")) {
                telnet = new TelnetH3c(ip, port, adm, pwd);
            }
            if (telnet != null) {
                try {
                    telnet.connect();
                    boolean f = telnet.login();
                    if (f) {
                        telnet.ipMacBind(arpEntity.getInet().replaceFirst("GE", ""), true, arpEntity.getIpAddress(), arpEntity.getMacAddress(), false);
                        msg = "删除成功";
                        json = "{success:true,msg:'" + msg + "!'}";
                        logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                        logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "资源管理", msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    telnet.disconnect();
                }
            } else {
                msg = "请先配置交换机相关信息";
                json = "{success:true,msg:'" + msg + "!'}";
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "资源管理", msg);

            }
        }
        actionBase.actionEnd(response, json, result);
        return null;
    }

    public String find() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String ip = UnifyXMLUtils.getValue(UnifyXMLUtils.ip);
        String port = UnifyXMLUtils.getValue(UnifyXMLUtils.port);
        String adm = UnifyXMLUtils.getValue(UnifyXMLUtils.adm);
        String pwd = UnifyXMLUtils.getValue(UnifyXMLUtils.pwd);
        String type = UnifyXMLUtils.getValue(UnifyXMLUtils.type);
        Telnet telnet = null;
        if(ip!=null&&port!=null&&adm!=null&&pwd!=null) {
            if (type.equals("HuaWei")) {
                telnet = new TelnetHuawei(ip, port, adm, pwd);
                String re = telnet.disIpSourceBinding();
                List<ArpEntity> arpEntities = new ArrayList<ArpEntity>();
                List<String> lines = telnet.getShellFileLine(re);
                for (String s : lines) {
                    String[] cols = s.trim().split("\\s+");
                    if (cols.length == 6 && !cols[0].equals("IP") && !cols[0].equals("Print")) {
                        ArpEntity arpEntity = new ArpEntity();
                        arpEntity.setIpAddress(cols[0]);
                        arpEntity.setMacAddress(cols[1]);
                        arpEntity.setInet(cols[5]);
                        arpEntities.add(arpEntity);
                    }
                }

                String json = "{success:true,total:" + arpEntities.size() + ",rows:[";
                Iterator<ArpEntity> raUserIterator = arpEntities.iterator();
                while (raUserIterator.hasNext()) {
                    ArpEntity log = raUserIterator.next();
                    if (raUserIterator.hasNext()) {
                        json += "{" +
                                "mac:'" + log.getMacAddress() +
                                "',inet:'" + log.getInet() +
                                "',vlan:'" + log.getVlan() +
                                "',aging:'" + log.getAging() +
                                "',type:'" + log.getType() +
                                "',expire:'" +
                                "',ip:'" + log.getIpAddress() + "'" +
                                "},";
                    } else {
                        json += "{" +
                                "mac:'" + log.getMacAddress() +
                                "',inet:'" + log.getInet() +
                                "',vlan:'" + log.getVlan() +
                                "',aging:'" + log.getAging() +
                                "',expire:'" + log.getExpire() +
                                "',expire:'" +
                                "',ip:'" + log.getIpAddress() + "'" +
                                "}";
                    }
                }
                json += "]}";
                actionBase.actionEnd(response, json, result);

            } else if (type.equals("H3c")) {
                telnet = new TelnetH3c(ip, port, adm, pwd);
                telnet = new TelnetHuawei(ip, port, adm, pwd);
                String re = telnet.disIpSourceBinding();
                List<ArpEntity> arpEntities = new ArrayList<ArpEntity>();
                List<String> lines = telnet.getShellFileLine(re);
                for (String s : lines) {
                    String[] cols = s.trim().split("\\s+");
                    if (cols.length == 6 && !cols[0].equals("IP") && !cols[0].equals("Print")) {
                        ArpEntity arpEntity = new ArpEntity();
                        arpEntity.setIpAddress(cols[0]);
                        arpEntity.setMacAddress(cols[1]);
                        arpEntity.setInet(cols[5]);
                        arpEntities.add(arpEntity);
                    }
                }

                String json = "{success:true,total:" + arpEntities.size() + ",rows:[";
                Iterator<ArpEntity> raUserIterator = arpEntities.iterator();
                while (raUserIterator.hasNext()) {
                    ArpEntity log = raUserIterator.next();
                    if (raUserIterator.hasNext()) {
                        json += "{" +
                                "mac:'" + log.getMacAddress() +
                                "',inet:'" + log.getInet() +
                                "',vlan:'" + log.getVlan() +
                                "',aging:'" + log.getAging() +
                                "',type:'" + log.getType() +
                                "',expire:'" +
                                "',ip:'" + log.getIpAddress() + "'" +
                                "},";
                    } else {
                        json += "{" +
                                "mac:'" + log.getMacAddress() +
                                "',inet:'" + log.getInet() +
                                "',vlan:'" + log.getVlan() +
                                "',aging:'" + log.getAging() +
                                "',expire:'" + log.getExpire() +
                                "',expire:'" +
                                "',ip:'" + log.getIpAddress() + "'" +
                                "}";
                    }
                }
                json += "]}";
                actionBase.actionEnd(response, json, result);
            }

        }
        return null;
    }

    public String arg_find() {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result = actionBase.actionBegin(request);
        String ip = UnifyXMLUtils.getValue(UnifyXMLUtils.ip);
        String port = UnifyXMLUtils.getValue(UnifyXMLUtils.port);
        String adm = UnifyXMLUtils.getValue(UnifyXMLUtils.adm);
        String pwd = UnifyXMLUtils.getValue(UnifyXMLUtils.pwd);
        String type = UnifyXMLUtils.getValue(UnifyXMLUtils.type);

        Telnet telnet = null;
        if (ip != null && port != null && adm != null && pwd != null) {
            if (type.equals("HuaWei")) {
                telnet = new TelnetHuawei(ip, port, adm, pwd);

                try {
                    telnet.connect();
                    boolean flag = telnet.login();
                    if (flag) {
                        String arpList = telnet.arpList();
                        List<ArpEntity> unifies = new ArrayList<ArpEntity>();
                        List<String> lines = telnet.getShellFileLine(arpList);
                        for (String s : lines) {
                            String[] cols = s.trim().split("\\s+");
                            if (cols.length == 5) {
                                ArpEntity arpEntity = new ArpEntity();
                                arpEntity.setIpAddress(cols[0]);
                                arpEntity.setMacAddress(cols[1]);
                                arpEntity.setExpire(cols[2]);
                                arpEntity.setType(cols[3]);
                                arpEntity.setInet(cols[4]);
                                unifies.add(arpEntity);
                            }
                        }
                        String json = "{success:true,total:" + unifies.size() + ",rows:[";
                        Iterator<ArpEntity> raUserIterator = unifies.iterator();
                        while (raUserIterator.hasNext()) {
                            ArpEntity log = raUserIterator.next();
                            if (raUserIterator.hasNext()) {
                                json += "{" +
                                        "mac:'" + log.getMacAddress() +
                                        "',inet:'" + log.getInet() +
                                        "',vlan:'" + log.getVlan() +
                                        "',aging:'" + log.getAging() +
                                        "',type:'" + log.getType() +
                                        "',expire:'" +log.getExpire()+
                                        "',ip:'" + log.getIpAddress() + "'" +
                                        "},";
                            } else {
                                json += "{" +
                                        "mac:'" + log.getMacAddress() +
                                        "',inet:'" + log.getInet() +
                                        "',vlan:'" + log.getVlan() +
                                        "',aging:'" + log.getAging() +
                                        "',expire:'" + log.getExpire() +
                                        "',expire:'" +log.getExpire()+
                                        "',ip:'" + log.getIpAddress() + "'" +
                                        "}";
                            }
                        }
                        json += "]}";
                        actionBase.actionEnd(response, json, result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    telnet.disconnect();
                }
            } else if (type.equals("H3c")) {
                try {
                    telnet.connect();
                    boolean flag = telnet.login();
                    if (flag) {
                        String arpList = telnet.arpList();
                        List<ArpEntity> unifies = new ArrayList<ArpEntity>();
                        List<String> lines = telnet.getShellFileLine(arpList);
                        for (String s : lines) {
                            String[] cols = s.trim().split("\\s+");
                            if (cols.length == 6) {
                                ArpEntity arpEntity = new ArpEntity();
                                arpEntity.setIpAddress(cols[0]);
                                arpEntity.setMacAddress(cols[1]);
                                arpEntity.setVlan(cols[2]);
                                arpEntity.setInet(cols[3]);
                                arpEntity.setAging(cols[4]);
                                arpEntity.setType(cols[5]);
                            }
                        }
                        String json = "{success:true,total:" + unifies.size() + ",rows:[";
                        Iterator<ArpEntity> raUserIterator = unifies.iterator();
                        while (raUserIterator.hasNext()) {
                            ArpEntity log = raUserIterator.next();
                            if (raUserIterator.hasNext()) {
                                json += "{" +
                                        "mac:'" + log.getMacAddress() +
                                        "',inet:'" + log.getInet() +
                                        "',vlan:'" + log.getVlan() +
                                        "',aging:'" + log.getAging() +
                                        "',type:'" + log.getType() +
                                        "',expire:'" +
                                        "',ip:'" + log.getIpAddress() + "'" +
                                        "},";
                            } else {
                                json += "{" +
                                        "mac:'" + log.getMacAddress() +
                                        "',inet:'" + log.getInet() +
                                        "',vlan:'" + log.getVlan() +
                                        "',aging:'" + log.getAging() +
                                        "',expire:'" + log.getExpire() +
                                        "',expire:'" +
                                        "',ip:'" + log.getIpAddress() + "'" +
                                        "}";
                            }
                        }
                        json += "]}";
                        actionBase.actionEnd(response, json, result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    telnet.disconnect();
                }
            }
        }
            return null;
    }
}
