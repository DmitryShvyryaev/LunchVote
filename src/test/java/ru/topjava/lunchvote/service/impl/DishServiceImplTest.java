package ru.topjava.lunchvote.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.topjava.lunchvote.exception.NotFoundException;
import ru.topjava.lunchvote.model.Dish;
import ru.topjava.lunchvote.service.AbstractServiceTest;
import ru.topjava.lunchvote.service.DishService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.topjava.lunchvote.testdata.DateTestData.FIRST_DAY;
import static ru.topjava.lunchvote.testdata.DateTestData.SECOND_DAY;
import static ru.topjava.lunchvote.testdata.DishTestData.*;
import static ru.topjava.lunchvote.testdata.RestaurantTestData.START_SEQ_REST;

public class DishServiceImplTest extends AbstractServiceTest {

    @Autowired
    private DishService dishService;

    @Test
    void getAllForRestaurant() {
        DISH_MATCHER.assertMatch(dishService.getAll(FIRST_DAY, START_SEQ_REST), tanukiFirstDay);
        DISH_MATCHER.assertMatch(dishService.getAll(SECOND_DAY, START_SEQ_REST), tanukiSecondDay);
        DISH_MATCHER.assertMatch(dishService.getAll(FIRST_DAY, START_SEQ_REST + 1), macFirstDay);
        DISH_MATCHER.assertMatch(dishService.getAll(SECOND_DAY, START_SEQ_REST + 1), macSecondDay);
        DISH_MATCHER.assertMatch(dishService.getAll(FIRST_DAY, START_SEQ_REST + 2), Collections.emptyList());
    }

    @Test
    void get() {
        DISH_MATCHER.assertMatch(dishService.get(START_SEQ_DISH, START_SEQ_REST), tanukiFirstDayDish1);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> dishService.get(10, START_SEQ_REST));
    }

    @Test
    void getNotOwn() {
        assertThrows(NotFoundException.class, () -> dishService.get(START_SEQ_DISH, START_SEQ_REST + 1));
    }

    @Test
    void create() {
        Dish created = dishService.create(START_SEQ_REST + 2, getCreated());
        Dish newDish = getCreated();
        newDish.setId(created.id());
        DISH_MATCHER.assertMatch(newDish, created);
        DISH_MATCHER.assertMatch(dishService.get(created.id(), START_SEQ_REST + 2), created);
        DISH_MATCHER.assertMatch(dishService.getAll(FIRST_DAY, START_SEQ_REST + 2), List.of(newDish));
    }

    @Test
    void createDuplicateName() {
        assertThrows(DataAccessException.class, () -> dishService.create(START_SEQ_REST, new Dish("Мисо-суп", 99.99, FIRST_DAY)));
    }

    @Test
    void update() {
        dishService.update(START_SEQ_REST, getUpdated());
        DISH_MATCHER.assertMatch(dishService.get(START_SEQ_DISH, START_SEQ_REST + 1), getUpdated());
        DISH_MATCHER.assertMatch(dishService.getAll(FIRST_DAY, START_SEQ_REST), List.of(tanukiFirstDayDish2));
    }

    @Test
    void delete() {
        dishService.delete(START_SEQ_REST, START_SEQ_DISH);
        assertThrows(NotFoundException.class, () -> dishService.get(START_SEQ_DISH, START_SEQ_REST));
        DISH_MATCHER.assertMatch(dishService.getAll(FIRST_DAY, START_SEQ_REST), List.of(tanukiFirstDayDish2));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> dishService.get(10, START_SEQ_REST));
    }
}