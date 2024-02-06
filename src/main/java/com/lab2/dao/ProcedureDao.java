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

	public List<Procedure> findByExample(Procedure exampleProcedure) {
		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<Procedure> cq = cb.createQuery(Procedure.class);
			Root<Procedure> root = cq.from(Procedure.class);
			List<Predicate> predicates = new ArrayList<>();

			if (exampleProcedure.getName() != null && !exampleProcedure.getName().isEmpty()) {
				predicates.add(cb.like(cb.lower(root.get("name")),
									   "%" + exampleProcedure.getName().toLowerCase() + "%"));
			}

			cq.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
			Query<Procedure> query = session.createQuery(cq);
			return query.getResultList();
		}
	}
}
