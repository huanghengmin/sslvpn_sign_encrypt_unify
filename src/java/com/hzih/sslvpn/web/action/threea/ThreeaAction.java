package com.hzih.sslvpn.web.action.threea;

import com.hzih.sslvpn.dao.ThreeaDao;
import com.hzih.sslvpn.domain.Threea;
import com.hzih.sslvpn.web.action.ActionBase;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Administrator on 2017/6/7.
 */
public class ThreeaAction   extends ActionSupport {

    private static final Logger logger = Logger.getLogger(ThreeaAction.class);

    private ThreeaDao threeaDao;

    public ThreeaDao getThreeaDao() {
        return threeaDao;
    }

    public void setThreeaDao(ThreeaDao threeaDao) {
        this.threeaDao = threeaDao;
    }

    public String insert() throws IOException {
        HttpServletRequest request = ServletActionContext.getRequest( );
        HttpServletResponse response = ServletActionContext.getResponse( );
        ActionBase actionBase = new ActionBase( );
        String result = actionBase.actionBegin(request);
        String msg = null;
        try {
            Threea threea=new Threea();
            threea.setIp(request.getParameter("ip"));
            threea.setPort(Integer.valueOf(request.getParameter("port")));
            threea.setLowermode(request.getParameter("lowermode"));
            int flag=threeaDao.save(threea);
            if (flag==0) {
                msg= "<font color=\"green\">添加成功,点击[确定]返回列表!</font>";
            }else{
                msg= "<font color=\"green\">添加失败,请检查!</font>";
            }
            logger.info("新增3A"+threea.getIp());
        } catch (NumberFormatException e) {
            logger.error("新增失败,请检查",e);
        }
        String json = "{success:true,msg:'"+msg+"'}";
        actionBase.actionEnd(response,json);
        return null;
    }
}
