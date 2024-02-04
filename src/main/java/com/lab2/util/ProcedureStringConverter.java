package com.lab2.util;

import javafx.util.StringConverter;
import com.lab2.entities.Procedure;
import com.lab2.entities.Doctor;
import com.lab2.entities.Patient;
import com.lab2.dao.PatientDao;
import com.lab2.dao.DoctorDao;

public class ProcedureStringConverter extends StringConverter<Procedure> {
    @Override
    public String toString(Procedure procedure) {
        if (procedure != null) {
            return procedure.getName();
        } else {
            return null;
        }
    }

    @Override
    public Procedure fromString(String string) {
        return null;
    }
}
