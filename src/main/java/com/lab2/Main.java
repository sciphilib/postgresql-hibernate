package com.lab2;

import java.util.*;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.lab2.entities.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();

        String hql = "FROM Doctor";
        Query query = session.createQuery(hql);
        List<Doctor> doctors = query.list();
        for (Doctor doctor : doctors) {
            System.out.println(doctor.toString());
        }

        hql = "FROM Specialization";
        query = session.createQuery(hql);
        List<Specialization> specs = query.list();
        for (Specialization spec : specs) {
            System.out.println(spec.toString());
        }

        hql = "FROM Appointment";
        query = session.createQuery(hql);
        List<Appointment> apps = query.list();
        for (Appointment app : apps) {
            System.out.println(app.toString());
        }

        hql = "FROM Visit";
        query = session.createQuery(hql);
        List<Visit> visits = query.list();
        for (Visit visit : visits) {
            System.out.println(visit.toString());
        }

        hql = "FROM Patient";
        query = session.createQuery(hql);
        List<Patient> patients = query.list();
        for (Patient patient : patients) {
            System.out.println(patient.toString());
        }

        hql = "FROM Medication";
        query = session.createQuery(hql);
        List<Medication> meds = query.list();
        for (Medication med : meds) {
            System.out.println(med.toString());
        }

        hql = "FROM PrescribedMedication";
        query = session.createQuery(hql);
        List<PrescribedMedication> prmeds = query.list();
        for (PrescribedMedication prmed : prmeds) {
            System.out.println(prmed.toString());
        }

        hql = "FROM Procedure";
        query = session.createQuery(hql);
        List<Procedure> procs = query.list();
        for (Procedure proc : procs) {
            System.out.println(proc.toString());
        }

        hql = "FROM PrescribedProcedure";
        query = session.createQuery(hql);
        List<PrescribedProcedure> prprocs = query.list();
        for (PrescribedProcedure prproc : prprocs) {
            System.out.println(prproc.toString());
        }

        hql = "FROM Test";
        query = session.createQuery(hql);
        List<Test> tests = query.list();
        for (Test test : tests) {
            System.out.println(test.toString());
        }

        hql = "FROM TestResult";
        query = session.createQuery(hql);
        List<TestResult> testResults = query.list();
        for (TestResult testResult : testResults) {
            System.out.println(testResult.toString());
        }

        hql = "FROM Diagnosis";
        query = session.createQuery(hql);
        List<Diagnosis> diagnosis = query.list();
        for (Diagnosis diag : diagnosis) {
            System.out.println(diag.toString());
        }

        session.close();
        sessionFactory.close();
    }
}