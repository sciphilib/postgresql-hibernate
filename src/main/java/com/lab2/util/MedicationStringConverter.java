package com.lab2.util;

import javafx.util.StringConverter;
import com.lab2.entities.Medication;
import com.lab2.entities.Doctor;
import com.lab2.entities.Patient;
import com.lab2.dao.PatientDao;
import com.lab2.dao.DoctorDao;

public class MedicationStringConverter extends StringConverter<Medication> {
    @Override
    public String toString(Medication medication) {
        if (medication != null) {
            return medication.getName();
        } else {
            return null;
        }
    }

    @Override
    public Medication fromString(String string) {
        return null;
    }
}
