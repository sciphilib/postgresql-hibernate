package com.lab2.util;

import javafx.util.StringConverter;
import com.lab2.entities.Visit;
import com.lab2.entities.Doctor;
import com.lab2.entities.Patient;
import com.lab2.dao.PatientDao;
import com.lab2.dao.DoctorDao;

public class VisitStringConverter extends StringConverter<Visit> {
	private static DoctorDao doctorDao = new DoctorDao();
	private static PatientDao patientDao = new PatientDao();
    @Override
    public String toString(Visit visit) {
        if (visit != null) {
			Doctor doctor = doctorDao.findById(visit.getIdDoctor());
			Patient patient = patientDao.findById(visit.getIdPatient());
			String result = String.format("Patient: %s %s %s, Doctor: %s %s %s, Complaints: %s", 
										  patient.getLastName(),patient.getFirstName(),patient.getMiddleName(),
										  doctor.getLastName(),doctor.getFirstName(),doctor.getMiddleName(),
										  visit.getComplaints());
            return result;
        } else {
            return null;
        }
    }

    @Override
    public Visit fromString(String string) {
        return null;
    }
}
