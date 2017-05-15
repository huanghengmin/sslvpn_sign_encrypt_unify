package com.hzih.sslvpn.web.action.unify;

import com.hzih.sslvpn.entity.Telnet;
import com.hzih.sslvpn.entity.TelnetH3c;
import com.hzih.sslvpn.entity.TelnetHuawei;
import com.hzih.sslvpn.service.LogService;
import com.hzih.sslvpn.web.SessionUtils;
import com.hzih.sslvpn.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: hhm
 * Date: 12-11-8
 * Time: 下午11:11
 * To change this template use File | Settings | File Templates.
 */
public class UnifyConfigAction extends ActionSupport {
    private Logger logger = Logger.getLogger(UnifyConfigAction.class);

    private LogService logService;

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    /**
     * 保存配置
     * @return
     * @throws java.io.IOException
     */
    public String save() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        String ip = request.getParameter("ip");
        String port = request.getParameter("port");
        String eth = request.getParameter("eth");
        String adm = request.getParameter("adm");
        String pwd = request.getParameter("pwd");
        String type = request.getParameter("type");
        boolean flag = UnifyXMLUtils.save(ip,Integer.parseInt(port),adm,pwd,eth,type);
        if(flag){
            msg = "接入配置保存成功";
            json = "{success:true,msg:'" + msg + "'}";
            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "LDAP配置", msg);

        }else {
            msg = "接入配置保存失败";
            json = "{success:false,msg:'" + msg + "'}";
            logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
            logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "LDAP配置", msg);
        }
        actionBase.actionEnd(response,json,result);
        return null;
    }


    public String openBind() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        String ip = request.getParameter("ip");
        String port = request.getParameter("port");
        String eth = request.getParameter("eth");
        String adm = request.getParameter("adm");
        String pwd = request.getParameter("pwd");
        String type = request.getParameter("type");

        Telnet telnet = null;
        if(type.equals("HuaWei")){
            telnet = new TelnetHuawei(ip, port, adm, pwd);
        }else if(type.equals("H3c")) {
            telnet = new TelnetH3c(ip, port, adm, pwd);
        }
        try {
            telnet.connect();
            boolean f = telnet.login();
            if(f) {
                    telnet.ipMacVerify(eth,true,true);
                    msg = "开启绑定成功";
                    json = "{success:true,msg:'" + msg + "!'}";
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "资源管理", msg);
                }else {
                    msg = "开启绑定失败";
                    json = "{success:true,msg:'" + msg + "!'}";
                    logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                    logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "资源管理", msg);
                }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            telnet.disconnect();
        }
        actionBase.actionEnd(response,json,result);
        return null;
    }


    public String closeBind() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        String json = null;
        String msg = null;
        String ip = request.getParameter("ip");
        String port = request.getParameter("port");
        String eth = request.getParameter("eth");
        String adm = request.getParameter("adm");
        String pwd = request.getParameter("pwd");
        String type = request.getParameter("type");
        Telnet telnet = null;
        if(type.equals("HuaWei")){
            telnet = new TelnetHuawei(ip, port, adm, pwd);
        }else if(type.equals("H3c")) {
            telnet = new TelnetH3c(ip, port, adm, pwd);
        }
        try {
            telnet.connect();
            boolean f = telnet.login();
            if(f) {
                telnet.ipMacVerify(eth,true,false);
                msg = "关闭绑定成功";
                json = "{success:true,msg:'" + msg + "!'}";
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "资源管理", msg);
            }else {
                msg = "关闭绑定失败";
                json = "{success:true,msg:'" + msg + "!'}";
                logger.info("管理员" + SessionUtils.getAccount(request).getUserName() + ",操作时间:" + new Date() + ",操作信息:" + msg);
                logService.newLog("INFO", SessionUtils.getAccount(request).getUserName(), "资源管理", msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            telnet.disconnect();
        }
        actionBase.actionEnd(response,json,result);
        return null;
    }

    /**
     * 查找
     * @return
     */
    public String find(){
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        ActionBase actionBase = new ActionBase();
        String result =	actionBase.actionBegin(request);
        int totalCount =0;
        StringBuilder sb = new StringBuilder();
        jsonResult(sb);
        totalCount = totalCount+1;
        StringBuilder json=new StringBuilder("{totalCount:"+totalCount+",root:[");
        json.append(sb.toString());
        json.append("]}");
        try {
            actionBase.actionEnd(response,json.toString(),result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回JSON数据格式
     * @param sb
     */
    private void jsonResult(StringBuilder sb) {
        String adm = UnifyXMLUtils.getValue(UnifyXMLUtils.adm);
        if(adm==null){
            adm = "";
        }
        String pwd = UnifyXMLUtils.getValue(UnifyXMLUtils.pwd);
        if(pwd==null){
            pwd = "";
        }
        String eth = UnifyXMLUtils.getValue(UnifyXMLUtils.eth);
        if(eth==null){
            eth = "";
        }
        String ip = UnifyXMLUtils.getValue(UnifyXMLUtils.ip);
        if(ip==null){
            ip = "";
        }
        String port = UnifyXMLUtils.getValue(UnifyXMLUtils.port);
        if(port==null){
            port = "";
        }
        String type = UnifyXMLUtils.getValue(UnifyXMLUtils.type);
        if(type==null){
            type = "";
        }
        sb.append("{");
        sb.append("adm:'"+ adm+"',");
        sb.append("pwd:'"+ pwd+"',");
        sb.append("eth:'"+eth+"',");
        sb.append("type:'"+type+"',");
        sb.append("ip:'"+ ip+"',");
        sb.append("port:'"+ port+"'");
        sb.append("}");
    }
}
