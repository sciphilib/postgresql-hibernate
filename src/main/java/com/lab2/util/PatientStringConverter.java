package com.lab2.util;

import javafx.util.StringConverter;
import com.lab2.entities.Patient;

public class PatientStringConverter extends StringConverter<Patient> {
    @Override
    public String toString(Patient patient) {
        if (patient != null) {
            return patient.getLastName() + " " + patient.getFirstName() + " " + patient.getMiddleName();
        } else {
            return null;
        }
    }

    @Override
    public Patient fromString(String string) {
        return null;
    }
}
