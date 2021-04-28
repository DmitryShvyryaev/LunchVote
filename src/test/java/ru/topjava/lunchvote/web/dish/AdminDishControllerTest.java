package ru.topjava.lunchvote.web.dish;

import org.junit.jupiter.api.Assertions;
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
import static ru.topjava.lunchvote.testdata.DishTestData.*;
import static ru.topjava.lunchvote.testdata.RestaurantTestData.START_SEQ_REST;
import static ru.topjava.lunchvote.testdata.UserTestData.admin;
import static ru.topjava.lunchvote.testdata.UserTestData.user1;

class AdminDishControllerTest extends AbstractControllerTest {

    private final static String REST_URL = "/rest/admin/restaurants/" + START_SEQ_REST + "/dishes/";
    @Autowired
    private JsonConverter jsonConverter;

    @Test
    void createWithLocation() throws Exception {
        Dish newDish = getCreated();
        MvcResult result = perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.writeValue(newDish)))
                .andExpect(status().isCreated())
                .andReturn();

        Dish created = jsonConverter.readValueFromJson(result.getResponse().getContentAsString(), Dish.class);
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
                .andExpect(status().isNoContent())
                .andReturn();

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
}