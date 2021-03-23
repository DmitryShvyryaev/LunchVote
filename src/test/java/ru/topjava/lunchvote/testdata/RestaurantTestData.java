package ru.topjava.lunchvote.testdata;

import ru.topjava.lunchvote.model.Restaurant;
import ru.topjava.lunchvote.to.RestaurantWithMenu;
import ru.topjava.lunchvote.to.RestaurantWithRating;
import ru.topjava.lunchvote.util.Matcher;

import java.util.Collections;
import java.util.List;

import static ru.topjava.lunchvote.util.RestaurantUtil.*;
import static ru.topjava.lunchvote.testdata.DishTestData.*;

public class RestaurantTestData {
    public static final Matcher<Restaurant> RESTAURANT_MATCHER = Matcher.getComparator("menu");
    public static final Matcher<RestaurantWithMenu> RESTAURANT_WITH_MENU_MATCHER = Matcher.getComparator();

    public static final long START_SEQ_REST = 100005L;

    public static final Restaurant rest1 = new Restaurant(START_SEQ_REST, "Тануки", "Ресторан японской кухни");
    public static final Restaurant rest2 = new Restaurant(START_SEQ_REST + 1, "Макдональдс", "Бургеры, наггетсы, кола");
    public static final Restaurant rest3 = new Restaurant(START_SEQ_REST + 2, "Папа Джонс", "Пицца так себе на дом");

    public static final Restaurant rest1WithMenuDay1 = createRestWithMenu(rest1, tanukiFirstDay);
    public static final Restaurant rest1WithMenuDay2 = createRestWithMenu(rest1, tanukiSecondDay);
    public static final Restaurant rest2WithMenuDay1 = createRestWithMenu(rest2, macFirstDay);
    public static final Restaurant rest2WithMenuDay2 = createRestWithMenu(rest2, macSecondDay);
    public static final Restaurant rest3WithMenuDay1 = createRestWithMenu(rest3, Collections.emptyList());
    public static final Restaurant rest3WithMenuDay2 = createRestWithMenu(rest3, Collections.emptyList());

    public static final List<Restaurant> restaurants = List.of(rest1, rest2, rest3);

    public static final List<Restaurant> restWithMenuFirstDay = List.of(rest1WithMenuDay1, rest2WithMenuDay1, rest3WithMenuDay1);
    public static final List<Restaurant> restWithMenuSecondDay = List.of(rest1WithMenuDay2, rest2WithMenuDay2, rest3WithMenuDay2);

    public static Restaurant getCreated() {
        return new Restaurant(null, "Новый ресторан", "Новейший ресторран");
    }

    public static Restaurant getUpdated() {
        Restaurant updated = new Restaurant(rest1);
        updated.setName("Обновленный");
        updated.setDescription("Обновленный ресторан");
        return updated;
    }
}
