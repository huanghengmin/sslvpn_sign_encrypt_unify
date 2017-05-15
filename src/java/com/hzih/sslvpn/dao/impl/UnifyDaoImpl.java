/*
package com.hzih.sslvpn.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import cn.collin.commons.domain.PageResult;
import com.hzih.sslvpn.dao.UnifyDao;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

*/
/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-3
 * Time: 下午3:50
 * To change this template use File | Settings | File Templates.
 *//*

public class UnifyDaoImpl extends MyDaoSupport implements UnifyDao {
    @Override
    public void setEntityClass() {
        this.entityClass = Unify.class;
    }

    @Override
    public PageResult listByPage(int pageIndex, int limit) {
        String hql = " from Unify s where 1=1";
        List paramsList = new ArrayList();

        String countHql = "select count(*) " + hql;

        PageResult ps = this.findByPage(hql, countHql, paramsList.toArray(),
                pageIndex, limit);
        return ps;
    }

    @Override
    public boolean add(Unify net) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().save(net);
        flag = true;
        return flag;
    }

    @Override
    public boolean modify(Unify net) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().saveOrUpdate(net);
        flag = true;
        return flag;
    }

    @Override
    public boolean delete(Unify net) throws Exception {
        boolean flag =false;
        super.getHibernateTemplate().delete(net);
        flag = true;
        return flag;
    }

    @Override
    public Unify findByMac(String net) throws Exception {
        String hql= String.format("from Unify s where s.mac ='%s'", net);
        List<Unify> privateNets  = super.getHibernateTemplate().find(hql);
        if(privateNets.size()>0&&privateNets!=null){
            return privateNets.get(0);
        }else {
            return null;
        }
    }

    @Override
    public Unify findById(int id) throws Exception {
        String hql= String.format("from Unify s where s.id =%d", id);
        List<Unify> privateNets  = super.getHibernateTemplate().find(hql);
        if(privateNets.size()>0&&privateNets!=null){
            return privateNets.get(0);
        }else {
            return null;
        }
    }

    @Override
    public boolean enable(int id) {
        boolean flag =false;
        String s ="update Unify u set u.status= "+0+" where u.id = "+id;
        Session session = super.getSession();
        try{
            session.beginTransaction();
            Query query = session.createQuery(s);
            query.executeUpdate();
            session.getTransaction().commit();
            flag = true;
        } catch (Exception e){
            logger.error(e.getMessage(),e);
        } finally {
            session.close();
        }
        return flag;
    }

    @Override
    public boolean disable(int id) {
        boolean flag =false;
        String s ="update Unify u set u.status= "+1+" where u.id = "+id;
        Session session = super.getSession();
        try{
            session.beginTransaction();
            Query query = session.createQuery(s);
            query.executeUpdate();
            session.getTransaction().commit();
            flag = true;
        } catch (Exception e){
            logger.error(e.getMessage(),e);
        } finally {
            session.close();
        }
        return flag;
    }
}
*/
