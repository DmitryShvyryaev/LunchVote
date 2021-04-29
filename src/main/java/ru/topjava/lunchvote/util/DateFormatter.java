package ru.topjava.lunchvote.util;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Locale;

public class DateFormatter implements Formatter {

    @Override
    public Object parse(String s, Locale locale) throws ParseException {
        return LocalDate.parse(s);
    }

    @Override
    public String print(Object o, Locale locale) {
        return null;
    }
}
