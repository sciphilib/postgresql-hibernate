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

	public List<Doctor> findByExample(Doctor exampleDoctor) {
		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<Doctor> cq = cb.createQuery(Doctor.class);
			Root<Doctor> root = cq.from(Doctor.class);
			List<Predicate> predicates = new ArrayList<>();

			if (exampleDoctor.getLastName() != null && !exampleDoctor.getLastName().isEmpty()) {
				predicates.add(cb.like(cb.lower(root.get("lastName")),
									   "%" + exampleDoctor.getLastName().toLowerCase() + "%"));
			}
			if (exampleDoctor.getFirstName() != null && !exampleDoctor.getFirstName().isEmpty()) {
				predicates.add(cb.like(cb.lower(root.get("firstName")),
									   "%" + exampleDoctor.getFirstName().toLowerCase() + "%"));
			}
			if (exampleDoctor.getMiddleName() != null && !exampleDoctor.getMiddleName().isEmpty()) {
				predicates.add(cb.like(cb.lower(root.get("middleName")),
									   "%" + exampleDoctor.getMiddleName().toLowerCase() + "%"));
			}
			if (exampleDoctor.getIdSpec() != null) {
				predicates.add(cb.equal(root.get("idSpec"), exampleDoctor.getIdSpec()));
			}

			cq.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
			Query<Doctor> query = session.createQuery(cq);
			return query.getResultList();
		}
	}
}
