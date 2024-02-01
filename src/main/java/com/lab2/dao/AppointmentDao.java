package com.lab2.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.lab2.entities.Appointment;
import com.lab2.util.HibernateUtil;

public class AppointmentDao implements GenericDao<Appointment> {

    private final SessionFactory sessionFactory;

    public AppointmentDao() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public Appointment findById(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Appointment.class, id);
        }
    }

    @Override
    public List<Appointment> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Appointment", Appointment.class).list();
        }
    }

    @Override
    public void save(Appointment appointment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(appointment);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Appointment appointment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(appointment);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(Appointment appointment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(appointment);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Appointment> findWithCriteria(String field, String value) {
        try (Session session = sessionFactory.openSession()) {
            String queryStr = "from Appointment where " + field + " like :value";
            Query<Appointment> query = session.createQuery(queryStr, Appointment.class);
            query.setParameter("value", "%" + value + "%");
            return query.list();
        }
    }
}
