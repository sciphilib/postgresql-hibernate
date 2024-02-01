package com.lab2.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.lab2.entities.TestResult;
import com.lab2.util.HibernateUtil;

public class TestResultDao implements GenericDao<TestResult> {

    private final SessionFactory sessionFactory;

    public TestResultDao() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public TestResult findById(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(TestResult.class, id);
        }
    }

    @Override
    public List<TestResult> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from TestResult", TestResult.class).list();
        }
    }

    @Override
    public void save(TestResult testResult) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(testResult);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(TestResult testResult) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(testResult);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(TestResult testResult) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(testResult);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<TestResult> findWithCriteria(String field, String value) {
        try (Session session = sessionFactory.openSession()) {
            String queryStr = "from TestResult where " + field + " like :value";
            Query<TestResult> query = session.createQuery(queryStr, TestResult.class);
            query.setParameter("value", "%" + value + "%");
            return query.list();
        }
    }
}
