package com.lab2.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import com.lab2.entities.Visit;
import com.lab2.entities.Medication;
import com.lab2.dao.VisitDao;
import com.lab2.dao.MedicationDao;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import java.util.ArrayList;
import java.util.List;
import com.lab2.entities.PrescribedMedication;
import com.lab2.util.HibernateUtil;

public class PrescribedMedicationDao implements GenericDao<PrescribedMedication> {

    private final SessionFactory sessionFactory;

    public PrescribedMedicationDao() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public PrescribedMedication findById(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(PrescribedMedication.class, id);
        }
    }

    @Override
    public List<PrescribedMedication> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from PrescribedMedication", PrescribedMedication.class).list();
        }
    }

    @Override
    public void save(PrescribedMedication prescribedMedication) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(prescribedMedication);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(PrescribedMedication prescribedMedication) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(prescribedMedication);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(PrescribedMedication prescribedMedication) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(prescribedMedication);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<PrescribedMedication> findWithCriteria(String field, String value) {
        try (Session session = sessionFactory.openSession()) {
            String queryStr = "from PrescribedMedication where " + field + " like :value";
            Query<PrescribedMedication> query = session.createQuery(queryStr, PrescribedMedication.class);
            query.setParameter("value", "%" + value + "%");
            return query.list();
        }
    }

	public List<PrescribedMedication> findByExample(PrescribedMedication examplePrescribedMedication) {
		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<PrescribedMedication> cq = cb.createQuery(PrescribedMedication.class);
			Root<PrescribedMedication> root = cq.from(PrescribedMedication.class);
			List<Predicate> predicates = new ArrayList<>();

			if (examplePrescribedMedication.getIdVisit() != null) {
				VisitDao visitDao = new VisitDao();
				Visit chosenVisit = visitDao.findById(examplePrescribedMedication.getIdVisit());
				predicates.add(cb.equal(root.get("idVisit"), chosenVisit.getId()));
			}
			if (examplePrescribedMedication.getIdMedication() != null) {
				MedicationDao medicationDao = new MedicationDao();
				Medication chosenMedication =
					medicationDao.findById(examplePrescribedMedication.getIdMedication());
				predicates.add(cb.equal(root.get("idMedication"), chosenMedication.getId()));
			}

			cq.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
			Query<PrescribedMedication> query = session.createQuery(cq);
			return query.getResultList();
		}
	}
}
