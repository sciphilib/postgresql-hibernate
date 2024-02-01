package com.lab2.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.lab2.entities.Doctor;
import com.lab2.util.HibernateUtil;

public class DoctorDao implements GenericDao<Doctor> {

    private final SessionFactory sessionFactory;

    public DoctorDao() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public Doctor findById(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Doctor.class, id);
        }
    }

    @Override
    public List<Doctor> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Doctor", Doctor.class).list();
        }
    }

    @Override
    public void save(Doctor doctor) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(doctor);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Doctor doctor) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(doctor);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(Doctor doctor) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(doctor);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Doctor> findWithCriteria(String field, String value) {
        try (Session session = sessionFactory.openSession()) {
            String queryStr = "from Doctor where " + field + " like :value";
            Query<Doctor> query = session.createQuery(queryStr, Doctor.class);
            query.setParameter("value", "%" + value + "%");
            return query.list();
        }
    }
}