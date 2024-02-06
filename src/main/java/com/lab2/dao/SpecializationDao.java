package com.lab2.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import com.lab2.entities.Specialization;
import com.lab2.dao.SpecializationDao;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import java.util.ArrayList;
import java.util.List;
import com.lab2.util.HibernateUtil;

public class SpecializationDao implements GenericDao<Specialization> {

    private final SessionFactory sessionFactory;

    public SpecializationDao() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public Specialization findById(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Specialization.class, id);
        }
    }

    @Override
    public List<Specialization> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Specialization", Specialization.class).list();
        }
    }

    @Override
    public void save(Specialization specialization) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(specialization);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Specialization specialization) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(specialization);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(Specialization specialization) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(specialization);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Specialization> findWithCriteria(String field, String value) {
        try (Session session = sessionFactory.openSession()) {
            String queryStr = "from Specialization where " + field + " like :value";
            Query<Specialization> query = session.createQuery(queryStr, Specialization.class);
            query.setParameter("value", "%" + value + "%");
            return query.list();
        }
    }

	public List<Specialization> findByExample(Specialization exampleSpecialization) {
		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<Specialization> cq = cb.createQuery(Specialization.class);
			Root<Specialization> root = cq.from(Specialization.class);
			List<Predicate> predicates = new ArrayList<>();

			if (exampleSpecialization.getName() != null && !exampleSpecialization.getName().isEmpty()) {
				predicates.add(cb.like(cb.lower(root.get("name")),
									   "%" + exampleSpecialization.getName().toLowerCase() + "%"));
			}

			cq.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
			Query<Specialization> query = session.createQuery(cq);
			return query.getResultList();
		}
	}
}
