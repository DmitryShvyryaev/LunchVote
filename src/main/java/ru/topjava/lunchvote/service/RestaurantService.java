package ru.topjava.lunchvote.service;

import ru.topjava.lunchvote.model.Restaurant;
import ru.topjava.lunchvote.to.RestaurantWithMenu;

import java.time.LocalDate;
import java.util.List;

public interface RestaurantService {
    List<Restaurant> getAll();

    Restaurant get(long id);

    Restaurant create(Restaurant restaurant);

    Restaurant update(Restaurant restaurant);

    void delete(long id);

    List<RestaurantWithMenu> getAllWithMenu(LocalDate date);

    RestaurantWithMenu getWithMenu(long id, LocalDate date);
}
