package com.lab2.util;

import javafx.util.StringConverter;
import com.lab2.entities.Doctor;

public class DoctorStringConverter extends StringConverter<Doctor> {
    @Override
    public String toString(Doctor doctor) {
        if (doctor != null) {
            return doctor.getLastName() + " " + doctor.getFirstName() + " " + doctor.getMiddleName();
        } else {
            return null;
        }
    }

    @Override
    public Doctor fromString(String string) {
        return null;
    }
}
