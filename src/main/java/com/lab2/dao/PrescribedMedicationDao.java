package com.lab2.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.lab2.entities.PrescribedMedication;
import com.lab2.util.HibernateUtil;

public class PrescribedMedicationDao implements GenericDao<PrescribedMedication> {

    private final SessionFactory sessionFactory;

    public PrescribedMedicationDao() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public PrescribedMedication findById(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(PrescribedMedication.class, id);
        }
    }

    @Override
    public List<PrescribedMedication> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from PrescribedMedication", PrescribedMedication.class).list();
        }
    }

    @Override
    public void save(PrescribedMedication prescribedMedication) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(prescribedMedication);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(PrescribedMedication prescribedMedication) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(prescribedMedication);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(PrescribedMedication prescribedMedication) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(prescribedMedication);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<PrescribedMedication> findWithCriteria(String field, String value) {
        try (Session session = sessionFactory.openSession()) {
            String queryStr = "from PrescribedMedication where " + field + " like :value";
            Query<PrescribedMedication> query = session.createQuery(queryStr, PrescribedMedication.class);
            query.setParameter("value", "%" + value + "%");
            return query.list();
        }
    }
}
