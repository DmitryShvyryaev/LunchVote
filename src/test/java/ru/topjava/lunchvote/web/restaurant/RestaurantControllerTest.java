package ru.topjava.lunchvote.web.restaurant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.topjava.lunchvote.model.Dish;
import ru.topjava.lunchvote.model.Restaurant;
import ru.topjava.lunchvote.util.JsonConverter;
import ru.topjava.lunchvote.web.AbstractControllerTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.topjava.lunchvote.testdata.DishTestData.DISH_MATCHER;
import static ru.topjava.lunchvote.testdata.RestaurantTestData.*;
import static ru.topjava.lunchvote.web.restaurant.RestaurantController.REST_URL;

class RestaurantControllerTest extends AbstractControllerTest {

    @Autowired
    private JsonConverter jsonConverter;

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

        List<Restaurant> actual = jsonConverter.readValuesFromJson(result.getResponse().getContentAsString(), Restaurant.class);
        RESTAURANT_MATCHER.assertMatch(actual, restaurants);
    }

    @Test
    void get() throws Exception {
        MvcResult result = perform(MockMvcRequestBuilders.get(REST_URL + "/" + START_SEQ_REST))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        Restaurant actual = jsonConverter.readValueFromJson(result.getResponse().getContentAsString(), Restaurant.class);
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

        List<Restaurant> actual = jsonConverter.readValuesFromJson(result.getResponse().getContentAsString(), Restaurant.class);
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

        Restaurant actual = jsonConverter.readValueFromJson(result.getResponse().getContentAsString(), Restaurant.class);
        RESTAURANT_MATCHER.assertMatch(actual, rest1);
        DISH_MATCHER.assertMatch(actual.getMenu(), expectedMenu);
    }
}