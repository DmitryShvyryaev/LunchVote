package ru.topjava.lunchvote.testdata;

import ru.topjava.lunchvote.model.Restaurant;
import ru.topjava.lunchvote.to.RestaurantTo;
import ru.topjava.lunchvote.util.Matcher;

import java.util.List;

public class RestaurantTestData {
    public static final Matcher<Restaurant> RESTAURANT_MATCHER = Matcher.getComparator();
    public static final Matcher<RestaurantTo> RESTAURANT_TO_MATCHER = Matcher.getComparator();

    public static final long START_SEQ_REST = 100005L;

    public static final Restaurant rest1 = new Restaurant(START_SEQ_REST, "Тануки", "Ресторан японской кухни");
    public static final Restaurant rest2 = new Restaurant(START_SEQ_REST + 1, "Макдональдс", "Бургеры, наггетсы, кола");
    public static final Restaurant rest3 = new Restaurant(START_SEQ_REST + 2, "Папа Джонс", "Пицца так себе на дом");

    public static final RestaurantTo restTo1Day1 = getTo(rest1, 3);
    public static final RestaurantTo restTo1Day2 = getTo(rest1, 0);
    public static final RestaurantTo restTo2Day1 = getTo(rest2, 1);
    public static final RestaurantTo restTo2Day2 = getTo(rest2, 2);
    public static final RestaurantTo restTo3Day1 = getTo(rest3, 1);
    public static final RestaurantTo restTo3Day2 = getTo(rest3, 3);

    public static final List<RestaurantTo> firstDayRestTo = List.of(restTo1Day1, restTo2Day1, restTo3Day1);
    public static final List<RestaurantTo> secondDayRestTo = List.of(restTo1Day2, restTo2Day2, restTo3Day2);

    public static final List<Restaurant> restaurants = List.of(rest1, rest2, rest3);

    public static Restaurant getCreated() {
        return new Restaurant("Новый ресторан", "Новейший ресторран");
    }

    public static Restaurant getUpdated() {
        Restaurant updated = new Restaurant(rest1);
        updated.setName("Обновленный");
        updated.setDescription("Обновленный ресторан");
        return updated;
    }

    public static RestaurantTo getTo(Restaurant restaurant, int rating) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName(), restaurant.getDescription(), rating);
    }
}
