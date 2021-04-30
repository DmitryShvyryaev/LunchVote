package ru.topjava.lunchvote.web.restaurant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.topjava.lunchvote.model.Dish;
import ru.topjava.lunchvote.model.Restaurant;
import ru.topjava.lunchvote.web.AbstractControllerTest;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.topjava.lunchvote.testdata.RestaurantTestData.*;
import static ru.topjava.lunchvote.testdata.UserTestData.user1;
import static ru.topjava.lunchvote.web.restaurant.RestaurantController.REST_URL;

class RestaurantControllerTest extends AbstractControllerTest {

    @BeforeEach
    public void evictCache() {
        cacheManager.getCache("restaurants").clear();
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.checkJson(restaurants));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/" + START_SEQ_REST)
                .with(userHttpBasic(user1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.checkJson(rest1));
    }

    @Test
    void getAllWithMenu() throws Exception {
        List<Restaurant> expected = new ArrayList<>();
        expected.add(new Restaurant(rest1, populateMenu(rest1.id(), 5)));
        expected.add(new Restaurant(rest2, populateMenu(rest2.id(), 6)));
        expected.add(new Restaurant(rest3, populateMenu(rest3.id(), 7)));
        perform(MockMvcRequestBuilders.get(REST_URL + "/with-menu")
                .with(userHttpBasic(user1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MENU_MATCHER.checkJson(expected));
    }

    @Test
    void getWithMenu() throws Exception {
        List<Dish> expectedMenu = populateMenu(START_SEQ_REST, 3);
        Restaurant expected = new Restaurant(rest1);
        expected.setMenu(expectedMenu);
        perform(MockMvcRequestBuilders.get(REST_URL + "/" + START_SEQ_REST + "/with-menu")
                .with(userHttpBasic(user1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MENU_MATCHER.checkJson(expected));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }
}