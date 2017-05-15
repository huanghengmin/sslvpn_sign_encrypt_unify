package com.hzih.sslvpn.web.action.unify;

import com.hzih.sslvpn.utils.StringContext;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;

public class UnifyXMLUtils {
    private static Logger logger = Logger.getLogger(UnifyXMLUtils.class);
    public static final String unify = "unify";
    public static final String ip = "ip";
    public static final String port = "port";
    public static final String adm = "adm";
    public static final String pwd = "pwd";
    public static final String eth = "eth";
    public static final String charset = "utf-8";
    public static final String type = "type";


    /**
     * @param name
     * @return
     */
    public static String getValue(String name) {
        SAXReader saxReader = new SAXReader();
        Document doc = null;
        String result = null;
        try {
            doc = saxReader.read(new File(StringContext.unify_xml));
        } catch (DocumentException e) {
            logger.error(e.getMessage());
        }
        if(doc!=null){
            Element ldap = doc.getRootElement();
            Element el = ldap.element(name);
            result = el.getText();
        }
        return result;
    }
    /**
     * @param ip
     * @param port
     * @param adm
     * @param pwd
     * @param eth
     */
    public static boolean save(String ip, int port, String adm, String pwd, String eth,String type) {
        boolean flag = false;
        Document doc = DocumentHelper.createDocument();
        Element ldap = doc.addElement(UnifyXMLUtils.unify);
        Element host_el = ldap.addElement(UnifyXMLUtils.ip);
        host_el.addText(ip);
        Element port_el = ldap.addElement(UnifyXMLUtils.port);
        port_el.addText(String.valueOf(port));
        Element adm_el = ldap.addElement(UnifyXMLUtils.adm);
        adm_el.addText(adm);
        Element pwd_el = ldap.addElement(UnifyXMLUtils.pwd);
        pwd_el.addText(pwd);
        Element base_el = ldap.addElement(UnifyXMLUtils.eth);
        base_el.addText(eth);
        Element type_el = ldap.addElement(UnifyXMLUtils.type);
        type_el.addText(type);
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding(charset);
        format.setIndent(true);
        try {
            XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(new File(StringContext.unify_xml)), format);
            try {
                xmlWriter.write(doc);
                flag = true;
            } catch (IOException e) {
                logger.info(e.getMessage());
            } finally {
                try {
                    xmlWriter.flush();
                    xmlWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        } catch (UnsupportedEncodingException e) {
            logger.info(e.getMessage());
        } catch (FileNotFoundException e) {
            logger.info(e.getMessage());
        }
        return flag;
    }
}


