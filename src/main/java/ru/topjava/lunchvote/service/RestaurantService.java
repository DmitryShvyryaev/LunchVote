package ru.topjava.lunchvote.service;

import ru.topjava.lunchvote.model.Restaurant;
import ru.topjava.lunchvote.to.RestaurantTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface RestaurantService {
    List<Restaurant> getAll();

    List<RestaurantTo> getAllWithRating(LocalDate date);

    RestaurantTo getSimpleWithRating(LocalDate date, long restaurantId);

    Restaurant get(long id);

    Restaurant create(Restaurant restaurant);

    Restaurant update(Restaurant restaurant);

    void delete(long id);

    boolean vote(long userId, long restaurantId, LocalDateTime now);
}
