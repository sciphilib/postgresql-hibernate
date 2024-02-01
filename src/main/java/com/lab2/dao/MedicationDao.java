package com.lab2.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.lab2.entities.Medication;
import com.lab2.util.HibernateUtil;

public class MedicationDao implements GenericDao<Medication> {

    private final SessionFactory sessionFactory;

    public MedicationDao() {
            this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public Medication findById(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Medication.class, id);
        }
    }

    @Override
    public List<Medication> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Medication", Medication.class).list();
        }
    }

    @Override
    public void save(Medication medication) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(medication);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Medication medication) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(medication);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(Medication medication) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(medication);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Medication> findWithCriteria(String field, String value) {
        try (Session session = sessionFactory.openSession()) {
            String queryStr = "from Medication where " + field + " like :value";
            Query<Medication> query = session.createQuery(queryStr, Medication.class);
            query.setParameter("value", "%" + value + "%");
            return query.list();
        }
    }
}