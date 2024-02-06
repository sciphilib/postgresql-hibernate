package com.lab2.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import java.util.ArrayList;
import java.util.List;

import com.lab2.entities.Doctor;
import com.lab2.entities.Patient;
import com.lab2.dao.DoctorDao;
import com.lab2.dao.PatientDao;

import com.lab2.entities.Visit;
import com.lab2.util.HibernateUtil;

public class VisitDao implements GenericDao<Visit> {

    private final SessionFactory sessionFactory;

    public VisitDao() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public Visit findById(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Visit.class, id);
        }
    }

    @Override
    public List<Visit> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Visit", Visit.class).list();
        }
    }

    @Override
    public void save(Visit visit) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(visit);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Visit visit) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(visit);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(Visit visit) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(visit);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Visit> findWithCriteria(String field, String value) {
        try (Session session = sessionFactory.openSession()) {
            String queryStr = "from Visit where " + field + " like :value";
            Query<Visit> query = session.createQuery(queryStr, Visit.class);
            query.setParameter("value", "%" + value + "%");
            return query.list();
        }
    }

	public List<Visit> findByExample(Visit exampleVisit) {
		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<Visit> cq = cb.createQuery(Visit.class);
			Root<Visit> root = cq.from(Visit.class);

			List<Predicate> predicates = new ArrayList<>();        

			if (exampleVisit.getIdPatient() != null) {
				PatientDao patientDao = new PatientDao();
				Patient chosePatient = patientDao.findById(exampleVisit.getIdPatient());
				predicates.add(cb.equal(root.get("idPatient"), chosePatient.getId()));
			}
			if (exampleVisit.getIdDoctor() != null) {
				DoctorDao doctorDao = new DoctorDao();
				Doctor chosenDoctor = doctorDao.findById(exampleVisit.getIdDoctor());
				predicates.add(cb.equal(root.get("idDoctor"), chosenDoctor.getId()));
			}
			if (exampleVisit.getComplaints() != null && !exampleVisit.getComplaints().isEmpty()) {
				predicates.add(cb.like(cb.lower(root.get("complaints")),
									   "%" + exampleVisit.getComplaints().toLowerCase() + "%"));
			}
			if (exampleVisit.getDateVisit() != null) {
				predicates.add(cb.equal(root.get("dateVisit"),
										exampleVisit.getDateVisit()));
			}
			if (exampleVisit.getDateDischarge() != null) {
				predicates.add(cb.equal(root.get("dateDischarge"),
										exampleVisit.getDateDischarge()));
			}
			if (exampleVisit.getDateClose() != null) {
				predicates.add(cb.equal(root.get("dateClose"),
										exampleVisit.getDateClose()));
			}

			cq.where(cb.and(predicates.toArray(new Predicate[0])));
			Query<Visit> query = session.createQuery(cq);
			return query.getResultList();
		}
	}
}
