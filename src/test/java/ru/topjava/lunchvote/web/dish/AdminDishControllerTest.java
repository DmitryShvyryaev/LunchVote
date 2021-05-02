package ru.topjava.lunchvote.web.dish;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.topjava.lunchvote.exception.NotFoundException;
import ru.topjava.lunchvote.model.Dish;
import ru.topjava.lunchvote.util.JsonConverter;
import ru.topjava.lunchvote.web.AbstractControllerTest;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.topjava.lunchvote.exception.ErrorType.*;
import static ru.topjava.lunchvote.testdata.DishTestData.*;
import static ru.topjava.lunchvote.testdata.RestaurantTestData.START_SEQ_REST;
import static ru.topjava.lunchvote.testdata.UserTestData.admin;
import static ru.topjava.lunchvote.testdata.UserTestData.user1;
import static ru.topjava.lunchvote.testdata.DateTestData.*;

class AdminDishControllerTest extends AbstractControllerTest {

    private final static String REST_URL = "/rest/admin/restaurants/" + START_SEQ_REST + "/dishes/";
    @Autowired
    private JsonConverter jsonConverter;

    @BeforeEach
    public void evictCache() {
        cacheManager.getCache("restaurants").clear();
    }

    @Test
    void createWithLocation() throws Exception {
        Dish newDish = getCreated();
        MvcResult result = perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.writeValue(newDish)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        Dish created = jsonConverter.readValueFromJson(result, Dish.class);
        long newId = created.id();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(dishService.get(newId, START_SEQ_REST), newDish);
    }

    @Test
    void update() throws Exception {
        Dish updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + updated.id())
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.writeValue(updated)))
                .andExpect(status().isNoContent());

        Dish actual = dishService.get(updated.id(), START_SEQ_REST);
        DISH_MATCHER.assertMatch(actual, updated);
    }

    @Test
    void delete() throws Exception {
        List<Dish> dishes = populateMenu(START_SEQ_REST, 1);
        long id = dishes.get(0).id();
        perform(MockMvcRequestBuilders.delete(REST_URL + id)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        Assertions.assertThrows(NotFoundException.class, () -> dishService.get(id, START_SEQ_REST));
    }

    @Test
    void createUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createForbidden() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(user1)))
                .andExpect(status().isForbidden());
    }

    @Test
    void createInvalid() throws Exception {
        Dish newDish = new Dish(" ", 2L, null);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.writeValue(newDish)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Test
    void createDuplicateName() throws Exception {
        Dish newDish = new Dish("Калифорния", 200002L, FIRST_DAY);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.writeValue(newDish)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Test
    void updateInvalid() throws Exception {
        Dish updated = new Dish(tanukiFirstDayDish1.id(), " ", 5L, null);
        perform(MockMvcRequestBuilders.put(REST_URL + updated.id())
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Test
    void updateDuplicateName() throws Exception {
        Dish updated = new Dish(tanukiFirstDayDish1.id(), "Калифорния", 100001L, tanukiFirstDayDish1.getDate());
        perform(MockMvcRequestBuilders.put(REST_URL + updated.id())
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessage("exception.dish.duplicateName"));
    }

    @Test
    void createNotExistRestaurant() throws Exception {
        Dish newDish = getCreated();
        perform(MockMvcRequestBuilders.post("/rest/admin/restaurants/" + 15L + "/dishes/")
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.writeValue(newDish)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(DATA_NOT_FOUND))
                .andExpect(detailMessage("exception.restaurant.notFound"));
    }

    @Test
    void updateNotExistRestaurant() throws Exception {
        Dish updated = getUpdated();
        perform(MockMvcRequestBuilders.put("/rest/admin/restaurants/" + 15L + "/dishes/" + updated.id())
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(DATA_NOT_FOUND))
                .andExpect(detailMessage("exception.restaurant.notFound"));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + 100100)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(DATA_NOT_FOUND));
    }
}