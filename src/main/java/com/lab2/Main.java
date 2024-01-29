package com.lab2;

import java.util.*;
import com.lab2.entities.Doctor;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

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

        session.close();
        sessionFactory.close();
    }
}