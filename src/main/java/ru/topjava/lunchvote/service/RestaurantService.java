package ru.topjava.lunchvote.service;

import ru.topjava.lunchvote.model.Restaurant;
import ru.topjava.lunchvote.to.RestaurantWithRating;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface RestaurantService {
    List<Restaurant> getAll();

    Restaurant get(long id);

    Restaurant create(Restaurant restaurant);

    Restaurant update(Restaurant restaurant);

    void delete(long id);
}
