package ru.topjava.lunchvote.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.topjava.lunchvote.exception.NotFoundException;
import ru.topjava.lunchvote.model.Dish;
import ru.topjava.lunchvote.model.Restaurant;
import ru.topjava.lunchvote.service.DishService;
import ru.topjava.lunchvote.service.RestaurantService;
import ru.topjava.lunchvote.util.JsonReader;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.topjava.lunchvote.testdata.RestaurantTestData.*;
import static ru.topjava.lunchvote.web.RestaurantRestController.REST_URL;
import static ru.topjava.lunchvote.testdata.DishTestData.DISH_MATCHER;

class RestaurantRestControllerTest extends AbstractControllerTest {

    @Autowired
    private JsonReader jsonReader;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private DishService dishService;

    @BeforeEach
    public void evictCache() {
        cacheManager.getCache("restaurants").clear();
    }

    @Test
    void getAll() throws Exception {
        MvcResult result = perform(MockMvcRequestBuilders.get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        List<Restaurant> actual = jsonReader.readValuesFromJson(result.getResponse().getContentAsString(), Restaurant.class);
        RESTAURANT_MATCHER.assertMatch(actual, restaurants);
    }

    @Test
    void get() throws Exception {
        MvcResult result = perform(MockMvcRequestBuilders.get(REST_URL + "/" + START_SEQ_REST))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        Restaurant actual = jsonReader.readValueFromJson(result.getResponse().getContentAsString(), Restaurant.class);
        RESTAURANT_MATCHER.assertMatch(actual, rest1);
    }

    @Test
    void getAllWithMenu() throws Exception {
        Map<Long, List<Dish>> expectedMenu = new HashMap<>();
        expectedMenu.put(START_SEQ_REST, populateMenu(START_SEQ_REST, 3));
        expectedMenu.put(START_SEQ_REST + 1, populateMenu(START_SEQ_REST + 1, 5));
        expectedMenu.put(START_SEQ_REST + 2, populateMenu(START_SEQ_REST + 2, 4));
        MvcResult result = perform(MockMvcRequestBuilders.get(REST_URL + "/with-menu"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        List<Restaurant> actual = jsonReader.readValuesFromJson(result.getResponse().getContentAsString(), Restaurant.class);
        RESTAURANT_MATCHER.assertMatch(actual, restaurants);
        for (Restaurant restaurant : actual) {
            List<Dish> currentMenu = expectedMenu.get(restaurant.id());
            DISH_MATCHER.assertMatch(restaurant.getMenu(), currentMenu);
        }
    }

    @Test
    void getWithMenu() throws Exception {
        List<Dish> expectedMenu = populateMenu(START_SEQ_REST, 3);
        MvcResult result = perform(MockMvcRequestBuilders.get(REST_URL + "/" + START_SEQ_REST + "/with-menu"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        Restaurant actual = jsonReader.readValueFromJson(result.getResponse().getContentAsString(), Restaurant.class);
        RESTAURANT_MATCHER.assertMatch(actual, rest1);
        DISH_MATCHER.assertMatch(actual.getMenu(), expectedMenu);
    }

    @Test
    void create() throws Exception {
        Restaurant newRestaurant = getCreated();
        MvcResult result = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonReader.writeValue(newRestaurant)))
                .andExpect(status().isCreated())
                .andReturn();

        Restaurant created = jsonReader.readValueFromJson(result.getResponse().getContentAsString(), Restaurant.class);
        long id = created.id();
        newRestaurant.setId(id);
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(restaurantService.get(id), newRestaurant);
    }

    @Test
    void update() throws Exception {
        Restaurant updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + "/" + updated.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonReader.writeValue(updated)))
                .andExpect(status().isNoContent());

        Restaurant actual = restaurantService.get(updated.id());
        RESTAURANT_MATCHER.assertMatch(actual, updated);
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + "/" + START_SEQ_REST))
                .andDo(print())
                .andExpect(status().isNoContent());
        Assertions.assertThrows(NotFoundException.class, () -> restaurantService.get(START_SEQ_REST));
    }

    private List<Dish> populateMenu(long restaurantId, int countOfDishes) {
        LocalDate today = LocalDate.now();
        List<Dish> result = new ArrayList<>();
        for (int i = 0; i < countOfDishes; i++) {
            Dish dish = new Dish("name" + restaurantId + (i * 10), restaurantId + (i * 100) + 0.1, today);
            result.add(dish);
            dishService.create(dish, restaurantId);
        }
        return result;
    }
}