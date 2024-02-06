package com.lab2.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import com.lab2.entities.Test;
import com.lab2.util.HibernateUtil;

public class TestDao implements GenericDao<Test> {

    private final SessionFactory sessionFactory;

    public TestDao() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public Test findById(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Test.class, id);
        }
    }

    @Override
    public List<Test> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Test", Test.class).list();
        }
    }

    @Override
    public void save(Test test) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(test);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Test test) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(test);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(Test test) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(test);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Test> findWithCriteria(String field, String value) {
        try (Session session = sessionFactory.openSession()) {
            String queryStr = "from Test where " + field + " like :value";
            Query<Test> query = session.createQuery(queryStr, Test.class);
            query.setParameter("value", "%" + value + "%");
            return query.list();
        }
    }

	public List<Test> findByExample(Test exampleTest) {
		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<Test> cq = cb.createQuery(Test.class);
			Root<Test> root = cq.from(Test.class);
			List<Predicate> predicates = new ArrayList<>();

			if (exampleTest.getName() != null && !exampleTest.getName().isEmpty()) {
				predicates.add(cb.like(cb.lower(root.get("name")),
									   "%" + exampleTest.getName().toLowerCase() + "%"));
			}

			cq.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
			Query<Test> query = session.createQuery(cq);
			return query.getResultList();
		}
	}
}
