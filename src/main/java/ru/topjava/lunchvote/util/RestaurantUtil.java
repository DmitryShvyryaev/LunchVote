package ru.topjava.lunchvote.util;

import ru.topjava.lunchvote.model.Dish;
import ru.topjava.lunchvote.model.Restaurant;
import ru.topjava.lunchvote.to.RestaurantWithMenu;

import java.util.List;

public class RestaurantUtil {
    private RestaurantUtil() {
    }

    public static Restaurant createRestWithMenu(Restaurant restaurant, List<Dish> menu) {
        Restaurant result = new Restaurant(restaurant);
        result.setMenu(menu);
        return result;
    }
}
