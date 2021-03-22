package ru.topjava.lunchvote.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.topjava.lunchvote.exception.NotFoundException;
import ru.topjava.lunchvote.model.Restaurant;
import ru.topjava.lunchvote.service.AbstractServiceTest;
import ru.topjava.lunchvote.service.RestaurantService;
import ru.topjava.lunchvote.testdata.DishTestData;
import ru.topjava.lunchvote.to.RestaurantWithMenu;
import ru.topjava.lunchvote.util.Matcher;

import static org.junit.Assert.assertThrows;
import static ru.topjava.lunchvote.testdata.RestaurantTestData.*;
import static ru.topjava.lunchvote.testdata.DateTestData.*;

public class RestaurantServiceImplTest extends AbstractServiceTest {

    @Autowired
    private RestaurantService restaurantService;

    @Before
    public void evictCache() {
        cacheManager.getCache("restaurants").clear();
    }

    @Test
    public void getAll() {
        RESTAURANT_MATCHER.assertMatch(restaurantService.getAll(), restaurants);
    }

    @Test
    public void get() {
        Restaurant restaurant = restaurantService.get(START_SEQ_REST);
        RESTAURANT_MATCHER.assertMatch(restaurant, rest1);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> restaurantService.get(10));
    }

    @Test
    public void create() {
        Restaurant created = restaurantService.create(getCreated());
        Restaurant newRest = getCreated();
        newRest.setId(created.id());
        RESTAURANT_MATCHER.assertMatch(created, newRest);
        RESTAURANT_MATCHER.assertMatch(restaurantService.get(created.id()), newRest);
    }

    @Test
    public void createDuplicateName() {
        assertThrows(DataAccessException.class, () -> restaurantService.create(new Restaurant("Тануки", "дубликат Тануки")));
    }

    @Test
    public void update() {
        restaurantService.update(getUpdated());
        RESTAURANT_MATCHER.assertMatch(restaurantService.get(START_SEQ_REST), getUpdated());
    }

    @Test
    public void delete() {
        restaurantService.delete(START_SEQ_REST);
        assertThrows(NotFoundException.class, () -> restaurantService.get(START_SEQ_REST));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> restaurantService.get(10));
    }

    @Test
    public void getAllWithMenu() {
        RESTAURANT_WITH_MENU_MATCHER.assertMatch(restaurantService.getAllWithMenu(FIRST_DAY), restWithMenuFirstDay);
        RESTAURANT_WITH_MENU_MATCHER.assertMatch(restaurantService.getAllWithMenu(SECOND_DAY), restWithMenuSecondDay);
    }

    @Test
    public void getWithMenu() {
        RestaurantWithMenu actual = restaurantService.getWithMenu(START_SEQ_REST, FIRST_DAY);
        Matcher.getComparator("menu").assertMatch(actual, rest1);
        DishTestData.DISH_MATCHER.assertMatch(actual.getMenu(), DishTestData.tanukiFirstDay);
    }

    @Test
    public void getWithMenuEmpty() {
        RESTAURANT_WITH_MENU_MATCHER.assertMatch(restaurantService.getWithMenu(START_SEQ_REST + 2, FIRST_DAY), rest3WithMenuDay1);
    }

    @Test
    public void testCache() {
        System.out.println(restaurantService.getAll());
        System.out.println(restaurantService.getAll());
    }
}