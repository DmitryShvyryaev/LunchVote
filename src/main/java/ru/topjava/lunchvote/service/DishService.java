package ru.topjava.lunchvote.service;

import ru.topjava.lunchvote.model.Dish;

import java.time.LocalDate;
import java.util.List;

public interface DishService {
    List<Dish> getAll(LocalDate date, long restaurantId);

    Dish get(long id);

    Dish create(Dish dish, long restaurantId);

    Dish update(Dish dish);

    void delete(long id);
}
