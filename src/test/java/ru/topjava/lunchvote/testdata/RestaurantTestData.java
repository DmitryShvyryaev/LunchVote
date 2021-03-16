package ru.topjava.lunchvote.testdata;

import ru.topjava.lunchvote.model.Restaurant;
import ru.topjava.lunchvote.util.Matcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RestaurantTestData {
    public static final Matcher<Restaurant> RESTAURANT_MATCHER = Matcher.getComparator();

    public static final long START_SEQ_REST = 100005;

    public static final Restaurant rest1 = new Restaurant(START_SEQ_REST, "Тануки", "Ресторан японской кухни");
    public static final Restaurant rest2 = new Restaurant(START_SEQ_REST + 1, "Макдональдс", "Бургеры, наггетсы, кола");
    public static final Restaurant rest3 = new Restaurant(START_SEQ_REST + 2, "Папа Джонс", "Пицца так себе на дом");

    public static final List<Restaurant> restaurants = new ArrayList<>();

    static {
        Collections.addAll(restaurants, rest1, rest2, rest3);
    }

    public static Restaurant getCreated() {
        return new Restaurant("new Restaurant", "description");
    }

    public static Restaurant getUpdated() {
        Restaurant updated = new Restaurant(rest1);
        updated.setName("Обновленный");
        updated.setDescription("Обновленный ресторан");
        return updated;
    }

}
