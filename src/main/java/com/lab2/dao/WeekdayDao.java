package com.lab2.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.lab2.entities.Weekday;
import com.lab2.util.HibernateUtil;

public class WeekdayDao implements GenericDao<Weekday> {

    private final SessionFactory sessionFactory;

    public WeekdayDao() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public Weekday findById(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Weekday.class, id);
        }
    }

    @Override
    public List<Weekday> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Weekday", Weekday.class).list();
        }
    }

    @Override
    public void save(Weekday weekday) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(weekday);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Weekday weekday) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(weekday);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(Weekday weekday) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(weekday);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Weekday> findWithCriteria(String field, String value) {
        try (Session session = sessionFactory.openSession()) {
            String queryStr = "from Weekday where " + field + " like :value";
            Query<Weekday> query = session.createQuery(queryStr, Weekday.class);
            query.setParameter("value", "%" + value + "%");
            return query.list();
        }
    }
}
