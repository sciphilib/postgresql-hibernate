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

	public List<Patient> findByExample(Patient examplePatient) {
		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<Patient> cq = cb.createQuery(Patient.class);
			Root<Patient> root = cq.from(Patient.class);
			List<Predicate> predicates = new ArrayList<>();

			if (examplePatient.getLastName() != null && !examplePatient.getLastName().isEmpty()) {
				predicates.add(cb.like(cb.lower(root.get("lastName")),
									   "%" + examplePatient.getLastName().toLowerCase() + "%"));
			}
			if (examplePatient.getFirstName() != null && !examplePatient.getFirstName().isEmpty()) {
				predicates.add(cb.like(cb.lower(root.get("firstName")),
									   "%" + examplePatient.getFirstName().toLowerCase() + "%"));
			}
			if (examplePatient.getMiddleName() != null && !examplePatient.getMiddleName().isEmpty()) {
				predicates.add(cb.like(cb.lower(root.get("middleName")),
									   "%" + examplePatient.getMiddleName().toLowerCase() + "%"));
			}
			if (examplePatient.getAddress() != null && !examplePatient.getAddress().isEmpty()) {
				predicates.add(cb.like(cb.lower(root.get("address")),
									   "%" + examplePatient.getAddress().toLowerCase() + "%"));
			}

			cq.where(cb.and(predicates.toArray(new Predicate[0])));
			Query<Patient> query = session.createQuery(cq);
			return query.getResultList();
		}
	}
}
