package com.lab2.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.lab2.entities.Appointment;
import com.lab2.entities.Doctor;
import com.lab2.entities.Weekday;
import com.lab2.dao.DoctorDao;
import com.lab2.dao.WeekdayDao;
import com.lab2.util.HibernateUtil;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDao implements GenericDao<Appointment> {

    private final SessionFactory sessionFactory;

    public AppointmentDao() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public Appointment findById(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Appointment.class, id);
        }
    }

    @Override
    public List<Appointment> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Appointment", Appointment.class).list();
        }
    }

    @Override
    public void save(Appointment appointment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(appointment);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Appointment appointment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(appointment);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(Appointment appointment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(appointment);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Appointment> findWithCriteria(String field, String value) {
        try (Session session = sessionFactory.openSession()) {
            String queryStr = "from Appointment where " + field + " like :value";
            Query<Appointment> query = session.createQuery(queryStr, Appointment.class);
            query.setParameter("value", "%" + value + "%");
            return query.list();
        }
    }

	public List<Appointment> findByExample(Appointment exampleAppointment) {
		try (Session session = sessionFactory.openSession()) {
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<Appointment> cq = cb.createQuery(Appointment.class);
			Root<Appointment> root = cq.from(Appointment.class);

			List<Predicate> predicates = new ArrayList<>();        

			if (exampleAppointment.getIdDoctor() != null) {
				DoctorDao doctorDao = new DoctorDao();
				Doctor chosenDoctor = doctorDao.findById(exampleAppointment.getIdDoctor());
				predicates.add(cb.equal(root.get("idDoctor"), chosenDoctor.getId()));
			}
			if (exampleAppointment.getIdWeekday() != null) {
				WeekdayDao weekdayDao = new WeekdayDao();
				Weekday chosenWeekday = weekdayDao.findById(exampleAppointment.getIdWeekday());
				predicates.add(cb.equal(root.get("idWeekday"), chosenWeekday.getId()));
			}
			if (exampleAppointment.getBeginDate() != null) {
				predicates.add(cb.equal(root.get("beginDate"),
										exampleAppointment.getBeginDate()));
			}
			if (exampleAppointment.getEndDate() != null) {
				predicates.add(cb.equal(root.get("endDate"),
										exampleAppointment.getEndDate()));
			}
			if (exampleAppointment.getOffice() != null) {
				predicates.add(cb.equal(root.get("office"),
										exampleAppointment.getOffice()));
			}
			if (exampleAppointment.getDistrict() != null) {
				predicates.add(cb.equal(root.get("district"),
										exampleAppointment.getDistrict()));
			}

			cq.where(cb.and(predicates.toArray(new Predicate[0])));
			Query<Appointment> query = session.createQuery(cq);
			return query.getResultList();
		}
	}
}
