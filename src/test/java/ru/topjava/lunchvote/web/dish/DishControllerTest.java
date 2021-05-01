package ru.topjava.lunchvote.web.dish;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.topjava.lunchvote.exception.ErrorType;
import ru.topjava.lunchvote.model.Dish;
import ru.topjava.lunchvote.web.AbstractControllerTest;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.topjava.lunchvote.testdata.DishTestData.DISH_MATCHER;
import static ru.topjava.lunchvote.testdata.DishTestData.getCreated;
import static ru.topjava.lunchvote.testdata.RestaurantTestData.START_SEQ_REST;
import static ru.topjava.lunchvote.testdata.UserTestData.user1;

class DishControllerTest extends AbstractControllerTest {

    private final static String REST_URL = "/rest/restaurants/" + START_SEQ_REST + "/dishes/";

    @Test
    void getAll() throws Exception {
        List<Dish> expectedMenu = populateMenu(START_SEQ_REST, 5);
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.checkJson(expectedMenu));
    }

    @Test
    void get() throws Exception {
        Dish created = dishService.create(START_SEQ_REST, getCreated());
        perform(MockMvcRequestBuilders.get(REST_URL + created.id())
                .with(userHttpBasic(user1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.checkJson(created));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAllNotExistRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get("/rest/restaurants/" + 15L + "/dishes/")
                .with(userHttpBasic(user1)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(ErrorType.DATA_NOT_FOUND))
                .andExpect(detailMessage("exception.restaurant.notFound"));
    }

    @Test
    void getNotExistRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get("/rest/restaurants/" + 15L + "/dishes/100034")
                .with(userHttpBasic(user1)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(ErrorType.DATA_NOT_FOUND))
                .andExpect(detailMessage("exception.restaurant.notFound"));
    }
}