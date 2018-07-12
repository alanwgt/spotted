package com.alanwgt.spotted.dao;

import com.alanwgt.spotted.models.Report;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReportDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public List<Report> getAllReports() {
        Session session = sessionFactory.getCurrentSession();
        Query<Report> query = session.createQuery("from Report", Report.class);
        return query.getResultList();
    }

    public List<Report> getUserReports(String uid) {
        Session session = sessionFactory.getCurrentSession();
        Query<Report> query = session.createQuery("from Report where uid=:uid", Report.class);
        query.setParameter("uid", uid);
        return query.getResultList();
    }

    public void create(Report report) {
        sessionFactory.getCurrentSession().saveOrUpdate(report);
    }

}
