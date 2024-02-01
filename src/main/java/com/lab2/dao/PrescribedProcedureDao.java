package com.lab2.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.lab2.entities.PrescribedProcedure;
import com.lab2.util.HibernateUtil;

public class PrescribedProcedureDao implements GenericDao<PrescribedProcedure> {

    private final SessionFactory sessionFactory;

    public PrescribedProcedureDao() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public PrescribedProcedure findById(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(PrescribedProcedure.class, id);
        }
    }

    @Override
    public List<PrescribedProcedure> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from PrescribedProcedure", PrescribedProcedure.class).list();
        }
    }

    @Override
    public void save(PrescribedProcedure prescribedProcedure) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(prescribedProcedure);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(PrescribedProcedure prescribedProcedure) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(prescribedProcedure);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(PrescribedProcedure prescribedProcedure) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(prescribedProcedure);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<PrescribedProcedure> findWithCriteria(String field, String value) {
        try (Session session = sessionFactory.openSession()) {
            String queryStr = "from PrescribedProcedure where " + field + " like :value";
            Query<PrescribedProcedure> query = session.createQuery(queryStr, PrescribedProcedure.class);
            query.setParameter("value", "%" + value + "%");
            return query.list();
        }
    }
}
