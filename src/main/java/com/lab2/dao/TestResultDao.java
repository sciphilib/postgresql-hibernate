package com.lab2.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import com.lab2.entities.Visit;
import com.lab2.entities.Test;
import com.lab2.dao.VisitDao;
import com.lab2.dao.TestDao;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import java.util.ArrayList;
import java.util.List;
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

	public List<TestResult> findByExample(TestResult exampleTestResult) {
		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<TestResult> cq = cb.createQuery(TestResult.class);
			Root<TestResult> root = cq.from(TestResult.class);
			List<Predicate> predicates = new ArrayList<>();

			if (exampleTestResult.getIdVisit() != null) {
				VisitDao visitDao = new VisitDao();
				Visit chosenVisit = visitDao.findById(exampleTestResult.getIdVisit());
				predicates.add(cb.equal(root.get("idVisit"), chosenVisit.getId()));
			}
			if (exampleTestResult.getIdTest() != null) {
				TestDao testDao = new TestDao();
				Test chosenTest = testDao.findById(exampleTestResult.getIdTest());
				predicates.add(cb.equal(root.get("idTest"), chosenTest.getId()));
			}
			if (exampleTestResult.getResult() != null && !exampleTestResult.getResult().isEmpty()) {
				predicates.add(cb.like(cb.lower(root.get("result")),
									   "%" + exampleTestResult.getResult().toLowerCase() + "%"));
			}

			cq.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
			Query<TestResult> query = session.createQuery(cq);
			return query.getResultList();
		}
	}
}
