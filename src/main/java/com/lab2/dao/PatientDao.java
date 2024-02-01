package com.lab2.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.lab2.entities.Patient;
import com.lab2.util.HibernateUtil;

public class PatientDao implements GenericDao<Patient> {

    private final SessionFactory sessionFactory;

    public PatientDao() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public Patient findById(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Patient.class, id);
        }
    }

    @Override
    public List<Patient> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Patient", Patient.class).list();
        }
    }

    @Override
    public void save(Patient patient) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(patient);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Patient patient) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(patient);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(Patient patient) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(patient);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Patient> findWithCriteria(String field, String value) {
        try (Session session = sessionFactory.openSession()) {
            String queryStr = "from Patient where " + field + " like :value";
            Query<Patient> query = session.createQuery(queryStr, Patient.class);
            query.setParameter("value", "%" + value + "%");
            return query.list();
        }
    }
}
