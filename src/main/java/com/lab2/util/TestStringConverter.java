package com.lab2.util;

import javafx.util.StringConverter;
import com.lab2.entities.Test;
import com.lab2.entities.Doctor;
import com.lab2.entities.Patient;
import com.lab2.dao.PatientDao;
import com.lab2.dao.DoctorDao;

public class TestStringConverter extends StringConverter<Test> {
    @Override
    public String toString(Test test) {
        if (test != null) {
            return test.getName();
        } else {
            return null;
        }
    }

    @Override
    public Test fromString(String string) {
        return null;
    }
}
