package com.lab2.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.lab2.entities.Visit;
import com.lab2.util.HibernateUtil;

public class VisitDao implements GenericDao<Visit> {

    private final SessionFactory sessionFactory;

    public VisitDao() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public Visit findById(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Visit.class, id);
        }
    }

    @Override
    public List<Visit> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Visit", Visit.class).list();
        }
    }

    @Override
    public void save(Visit visit) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(visit);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Visit visit) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(visit);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(Visit visit) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(visit);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Visit> findWithCriteria(String field, String value) {
        try (Session session = sessionFactory.openSession()) {
            String queryStr = "from Visit where " + field + " like :value";
            Query<Visit> query = session.createQuery(queryStr, Visit.class);
            query.setParameter("value", "%" + value + "%");
            return query.list();
        }
    }
}