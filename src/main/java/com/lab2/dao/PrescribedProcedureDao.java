package com.lab2.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import com.lab2.entities.Visit;
import com.lab2.entities.Procedure;
import com.lab2.dao.VisitDao;
import com.lab2.dao.ProcedureDao;
import com.lab2.entities.PrescribedProcedure;
import com.lab2.util.HibernateUtil;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import java.util.ArrayList;
import java.util.List;

public class PrescribedProcedureDao implements GenericDao<PrescribedProcedure> {

    private final SessionFactory sessionFactory;

    public PrescribedProcedureDao() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public PrescribedProcedure findById(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(PrescribedProcedure.class, id);
        }
    }

    @Override
    public List<PrescribedProcedure> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from PrescribedProcedure", PrescribedProcedure.class).list();
        }
    }

    @Override
    public void save(PrescribedProcedure prescribedProcedure) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(prescribedProcedure);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(PrescribedProcedure prescribedProcedure) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(prescribedProcedure);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(PrescribedProcedure prescribedProcedure) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(prescribedProcedure);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<PrescribedProcedure> findWithCriteria(String field, String value) {
        try (Session session = sessionFactory.openSession()) {
            String queryStr = "from PrescribedProcedure where " + field + " like :value";
            Query<PrescribedProcedure> query = session.createQuery(queryStr, PrescribedProcedure.class);
            query.setParameter("value", "%" + value + "%");
            return query.list();
        }
    }

	public List<PrescribedProcedure> findByExample(PrescribedProcedure examplePrescribedProcedure) {
		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<PrescribedProcedure> cq = cb.createQuery(PrescribedProcedure.class);
			Root<PrescribedProcedure> root = cq.from(PrescribedProcedure.class);
			List<Predicate> predicates = new ArrayList<>();

			if (examplePrescribedProcedure.getIdVisit() != null) {
				VisitDao visitDao = new VisitDao();
				Visit chosenVisit = visitDao.findById(examplePrescribedProcedure.getIdVisit());
				predicates.add(cb.equal(root.get("idVisit"), chosenVisit.getId()));
			}
			if (examplePrescribedProcedure.getIdProcedure() != null) {
				ProcedureDao procedureDao = new ProcedureDao();
				Procedure chosenProcedure = procedureDao.
					findById(examplePrescribedProcedure.getIdProcedure());
				predicates.add(cb.equal(root.get("idProcedure"), chosenProcedure.getId()));
			}
			if (examplePrescribedProcedure.getCount() != null) {
				predicates.add(cb.equal(root.get("count"),
										examplePrescribedProcedure.getCount()));
			}

			cq.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
			Query<PrescribedProcedure> query = session.createQuery(cq);
			return query.getResultList();
		}
	}
}
