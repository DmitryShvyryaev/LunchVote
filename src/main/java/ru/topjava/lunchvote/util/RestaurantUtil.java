package ru.topjava.lunchvote.util;

import ru.topjava.lunchvote.model.Dish;
import ru.topjava.lunchvote.model.Restaurant;
import ru.topjava.lunchvote.to.RestaurantWithMenu;

import java.util.List;

public class RestaurantUtil {
    private RestaurantUtil() {
    }

    public static RestaurantWithMenu createRestWithMenu(Restaurant restaurant, List<Dish> menu) {
        return new RestaurantWithMenu(restaurant.getId(), restaurant.getName(), restaurant.getDescription(), menu);
    }
}
