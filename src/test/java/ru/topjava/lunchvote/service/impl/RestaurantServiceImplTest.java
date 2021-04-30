package ru.topjava.lunchvote.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.topjava.lunchvote.exception.NotFoundException;
import ru.topjava.lunchvote.model.Restaurant;
import ru.topjava.lunchvote.service.AbstractServiceTest;
import ru.topjava.lunchvote.service.RestaurantService;
import ru.topjava.lunchvote.testdata.DishTestData;
import ru.topjava.lunchvote.util.Matcher;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.topjava.lunchvote.testdata.DateTestData.FIRST_DAY;
import static ru.topjava.lunchvote.testdata.RestaurantTestData.*;

public class RestaurantServiceImplTest extends AbstractServiceTest {

    @Autowired
    private RestaurantService restaurantService;

    @BeforeEach
    public void evictCache() {
        cacheManager.getCache("restaurants").clear();
    }

    @Test
    void getAll() {
        RESTAURANT_MATCHER.assertMatch(restaurantService.getAll(), restaurants);
    }

    @Test
    void get() {
        Restaurant restaurant = restaurantService.get(START_SEQ_REST);
        RESTAURANT_MATCHER.assertMatch(restaurant, rest1);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> restaurantService.get(10));
    }

    @Test
    void create() {
        Restaurant created = restaurantService.create(getCreated());
        Restaurant newRest = getCreated();
        newRest.setId(created.id());
        RESTAURANT_MATCHER.assertMatch(created, newRest);
        RESTAURANT_MATCHER.assertMatch(restaurantService.get(created.id()), newRest);
    }

    @Test
    void createDuplicateName() {
        assertThrows(DataAccessException.class, () -> restaurantService.create(new Restaurant("Тануки", "дубликат Тануки")));
    }

    @Test
    void update() {
        restaurantService.update(getUpdated());
        RESTAURANT_MATCHER.assertMatch(restaurantService.get(START_SEQ_REST), getUpdated());
    }

    @Test
    void delete() {
        restaurantService.delete(START_SEQ_REST);
        assertThrows(NotFoundException.class, () -> restaurantService.get(START_SEQ_REST));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> restaurantService.get(10));
    }

    @Test
    void getAllWithMenu() {
        List<Restaurant> actual = restaurantService.getAllWithMenu(FIRST_DAY);
        RESTAURANT_MATCHER.assertMatch(actual, restWithMenuFirstDay);
        for (Restaurant restaurant : actual) {
            if (restaurant.getName().equals(rest1.getName()))
                DishTestData.DISH_MATCHER.assertMatch(restaurant.getMenu(), DishTestData.tanukiFirstDay);
            else if (restaurant.getName().equals(rest2.getName()))
                DishTestData.DISH_MATCHER.assertMatch(restaurant.getMenu(), DishTestData.macFirstDay);
            else
                DishTestData.DISH_MATCHER.assertMatch(restaurant.getMenu(), Collections.emptyList());
        }
    }

    @Test
    void getWithMenu() {
        Restaurant actual = restaurantService.getWithMenu(START_SEQ_REST, FIRST_DAY);
        RESTAURANT_MATCHER.assertMatch(actual, rest1);
        DishTestData.DISH_MATCHER.assertMatch(actual.getMenu(), DishTestData.tanukiFirstDay);
    }

    @Test
    void getWithMenuEmpty() {
        Restaurant actual = restaurantService.getWithMenu(START_SEQ_REST + 2, FIRST_DAY);
        RESTAURANT_MATCHER.assertMatch(actual, rest3);
        DishTestData.DISH_MATCHER.assertMatch(actual.getMenu(), Collections.emptyList());
    }

    @Test
    void createInvalid() {
        validateRootCause(ConstraintViolationException.class, () -> restaurantService.create(new Restaurant("22", "description123")));
        validateRootCause(ConstraintViolationException.class, () -> restaurantService.create(new Restaurant("name", "descr")));
    }
}