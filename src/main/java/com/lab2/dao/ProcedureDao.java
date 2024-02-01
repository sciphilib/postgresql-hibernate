package com.lab2.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.lab2.entities.Procedure;
import com.lab2.util.HibernateUtil;

public class ProcedureDao implements GenericDao<Procedure> {

    private final SessionFactory sessionFactory;

    public ProcedureDao() {
            this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public Procedure findById(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Procedure.class, id);
        }
    }

    @Override
    public List<Procedure> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Procedure", Procedure.class).list();
        }
    }

    @Override
    public void save(Procedure procedure) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(procedure);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Procedure procedure) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(procedure);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(Procedure procedure) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(procedure);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Procedure> findWithCriteria(String field, String value) {
        try (Session session = sessionFactory.openSession()) {
            String queryStr = "from Procedure where " + field + " like :value";
            Query<Procedure> query = session.createQuery(queryStr, Procedure.class);
            query.setParameter("value", "%" + value + "%");
            return query.list();
        }
    }
}