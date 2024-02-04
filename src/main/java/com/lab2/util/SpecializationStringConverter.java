package com.lab2.util;

import javafx.util.StringConverter;
import com.lab2.entities.Specialization;

public class SpecializationStringConverter extends StringConverter<Specialization> {
    @Override
    public String toString(Specialization specialization) {
        if (specialization != null) {
            return specialization.getName();
        } else {
            return null;
        }
    }

    @Override
    public Specialization fromString(String string) {
        return null;
    }
}
