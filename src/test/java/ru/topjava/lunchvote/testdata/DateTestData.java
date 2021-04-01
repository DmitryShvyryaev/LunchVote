package ru.topjava.lunchvote.testdata;

import java.time.LocalDate;
import java.time.Month;

public class DateTestData {

    private DateTestData() {
    }

    public static final LocalDate FIRST_DAY = LocalDate.of(2021, Month.MARCH, 15);
    public static final LocalDate SECOND_DAY = LocalDate.of(2021, Month.MARCH, 16);
    public static final LocalDate THIRD_DAY = LocalDate.of(2021, Month.MARCH, 1);
}
