package ru.topjava.lunchvote.util;

import static org.assertj.core.api.Assertions.assertThat;

public class Matcher<T> {
    private final String[] ignoredFields;

    private Matcher(String[] ignoredFields) {
        this.ignoredFields = ignoredFields;
    }

    public static <T> Matcher<T> getComparator(String... fieldsToIgnore) {
        return new Matcher<>(fieldsToIgnore);
    }

    public void assertMatch(T actual, T expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields(ignoredFields).isEqualTo(expected);
    }

    public void assertMatch(Iterable<T> actual, Iterable<T> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields(ignoredFields).isEqualTo(expected);
    }

}
