package ru.topjava.lunchvote.service;

import ru.topjava.lunchvote.model.Dish;

import java.time.LocalDate;
import java.util.List;

public interface DishService {
    List<Dish> getAll(LocalDate date, long restaurantId);

    Dish get(long id, long restaurantId);

    Dish create(long restaurantId, Dish dish);

    Dish update(long restaurantId, Dish dish);

    void delete(long restaurantId, long id);
}
