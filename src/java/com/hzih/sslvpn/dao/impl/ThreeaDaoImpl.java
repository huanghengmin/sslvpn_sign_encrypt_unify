package com.hzih.sslvpn.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import com.hzih.sslvpn.dao.ThreeaDao;
import com.hzih.sslvpn.domain.Threea;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 * Created by Administrator on 2017/6/7.
 */
public class ThreeaDaoImpl extends MyDaoSupport implements ThreeaDao {


    @Override
    public void setEntityClass() {
        this.entityClass = Threea.class;
    }

    @Override
    public int save(Threea threea) {

        int flag=0;
        try {
            Session session = getSessionFactory( ).getCurrentSession( );
            session.save(threea);
        } catch (HibernateException e) {
            e.printStackTrace( );
            flag=1;
        }
        return  flag;
    }
}
