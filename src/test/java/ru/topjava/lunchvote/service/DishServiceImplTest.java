package ru.topjava.lunchvote.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.topjava.lunchvote.exception.NotFoundException;
import ru.topjava.lunchvote.model.Dish;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.topjava.lunchvote.testdata.DateTestData.FIRST_DAY;
import static ru.topjava.lunchvote.testdata.DateTestData.SECOND_DAY;
import static ru.topjava.lunchvote.testdata.DishTestData.*;
import static ru.topjava.lunchvote.testdata.RestaurantTestData.START_SEQ_REST;

public class DishServiceImplTest extends AbstractServiceTest {

    @Autowired
    private DishService dishService;

    @Test
    public void getAll() {
        DISH_MATCHER.assertMatch(dishService.getAll(FIRST_DAY, START_SEQ_REST), tanukiFirstDay);
        DISH_MATCHER.assertMatch(dishService.getAll(SECOND_DAY, START_SEQ_REST), tanukiSecondDay);
        DISH_MATCHER.assertMatch(dishService.getAll(FIRST_DAY, START_SEQ_REST + 1), macFirstDay);
        DISH_MATCHER.assertMatch(dishService.getAll(SECOND_DAY, START_SEQ_REST + 1), macSecondDay);
        DISH_MATCHER.assertMatch(dishService.getAll(FIRST_DAY, START_SEQ_REST + 2), Collections.EMPTY_LIST);
    }

    @Test
    public void get() {
        DISH_MATCHER.assertMatch(dishService.get(START_SEQ_DISH), tanukiFirstDayDish1);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> dishService.get(10));
    }

    @Test
    public void create() {
        Dish created = dishService.create(getCreated(), START_SEQ_REST + 2);
        Dish newDish = getCreated();
        newDish.setId(created.getId());
        DISH_MATCHER.assertMatch(newDish, created);
        DISH_MATCHER.assertMatch(dishService.get(created.getId()), created);
        DISH_MATCHER.assertMatch(dishService.getAll(FIRST_DAY, START_SEQ_REST + 2), List.of(newDish));
    }

    @Test
    public void createDuplicateName() {
        assertThrows(DataAccessException.class, () -> dishService.create(new Dish("Мисо-суп", 99.99, FIRST_DAY), START_SEQ_REST));
    }

    @Test
    public void update() {
        dishService.update(getUpdated());
        DISH_MATCHER.assertMatch(dishService.get(START_SEQ_DISH), getUpdated());
        DISH_MATCHER.assertMatch(dishService.getAll(FIRST_DAY, START_SEQ_REST), List.of(tanukiFirstDayDish2));
    }

    @Test
    public void delete() {
        dishService.delete(START_SEQ_DISH);
        assertThrows(NotFoundException.class, () -> dishService.get(START_SEQ_DISH));
        DISH_MATCHER.assertMatch(dishService.getAll(FIRST_DAY, START_SEQ_REST), List.of(tanukiFirstDayDish2));
    }
}