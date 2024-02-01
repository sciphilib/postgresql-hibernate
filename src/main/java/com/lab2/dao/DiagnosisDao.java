package com.lab2.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.lab2.entities.Diagnosis;
import com.lab2.util.HibernateUtil;

public class DiagnosisDao implements GenericDao<Diagnosis> {

    private final SessionFactory sessionFactory;

    public DiagnosisDao() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public Diagnosis findById(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Diagnosis.class, id);
        }
    }

    @Override
    public List<Diagnosis> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Diagnosis", Diagnosis.class).list();
        }
    }

    @Override
    public void save(Diagnosis diagnosis) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(diagnosis);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Diagnosis diagnosis) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(diagnosis);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(Diagnosis diagnosis) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(diagnosis);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Diagnosis> findWithCriteria(String field, String value) {
        try (Session session = sessionFactory.openSession()) {
            String queryStr = "from Diagnosis where " + field + " like :value";
            Query<Diagnosis> query = session.createQuery(queryStr, Diagnosis.class);
            query.setParameter("value", "%" + value + "%");
            return query.list();
        }
    }
}
