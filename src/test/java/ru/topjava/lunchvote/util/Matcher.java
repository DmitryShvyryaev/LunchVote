package ru.topjava.lunchvote.util;

import org.springframework.test.web.servlet.ResultMatcher;

import static org.assertj.core.api.Assertions.assertThat;

public class Matcher<T> {

    private final String[] ignoredFields;
    private final JsonConverter converter = JsonConverter.getConverter();
    private final Class<T> clazz;

    private Matcher(String[] ignoredFields, Class<T> clazz) {
        this.ignoredFields = ignoredFields;
        this.clazz = clazz;
    }

    public static <T> Matcher<T> getComparator(Class<T> clazz, String... fieldsToIgnore) {
        return new Matcher<>(fieldsToIgnore, clazz);
    }

    public void assertMatch(T actual, T expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields(ignoredFields).isEqualTo(expected);
    }

    public void assertMatch(Iterable<T> actual, Iterable<T> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields(ignoredFields).isEqualTo(expected);
    }

    public ResultMatcher checkJson(T expected) {
        return result -> assertMatch(converter.readValueFromJson(result, clazz), expected);
    }

    public ResultMatcher checkJson(Iterable<T> expected) {
        return result -> assertMatch(converter.readValuesFromJson(result, clazz), expected);
    }
}
