package com.lab2.util;

import javafx.util.StringConverter;
import com.lab2.entities.Weekday;

public class WeekdayStringConverter extends StringConverter<Weekday> {
    @Override
    public String toString(Weekday weekday) {
        if (weekday != null) {
            return weekday.getName();
        } else {
            return null;
        }
    }

    @Override
    public Weekday fromString(String string) {
        return null;
    }
}
