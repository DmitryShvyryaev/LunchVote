package ru.topjava.lunchvote.web.restaurant;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.topjava.lunchvote.exception.NotFoundException;
import ru.topjava.lunchvote.model.Restaurant;
import ru.topjava.lunchvote.service.RestaurantService;
import ru.topjava.lunchvote.util.JsonConverter;
import ru.topjava.lunchvote.web.AbstractControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.topjava.lunchvote.testdata.RestaurantTestData.*;
import static ru.topjava.lunchvote.testdata.UserTestData.admin;
import static ru.topjava.lunchvote.testdata.UserTestData.user1;
import static ru.topjava.lunchvote.web.restaurant.AdminRestaurantController.REST_URL;

class AdminRestaurantControllerTest extends AbstractControllerTest {

    @Autowired
    private JsonConverter jsonConverter;

    @Autowired
    private RestaurantService restaurantService;

    @BeforeEach
    public void evictCache() {
        cacheManager.getCache("restaurants").clear();
    }

    @Test
    void create() throws Exception {
        Restaurant newRestaurant = getCreated();
        MvcResult result = perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.writeValue(newRestaurant)))
                .andExpect(status().isCreated())
                .andReturn();

        Restaurant created = jsonConverter.readValueFromJson(result, Restaurant.class);
        long id = created.id();
        newRestaurant.setId(id);
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(restaurantService.get(id), newRestaurant);
    }

    @Test
    void update() throws Exception {
        Restaurant updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + "/" + updated.getId())
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.writeValue(updated)))
                .andExpect(status().isNoContent());

        Restaurant actual = restaurantService.get(updated.id());
        RESTAURANT_MATCHER.assertMatch(actual, updated);
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + "/" + START_SEQ_REST)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        Assertions.assertThrows(NotFoundException.class, () -> restaurantService.get(START_SEQ_REST));
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
        Restaurant newRestaurant = new Restaurant("  ", "  ");
        perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.writeValue(newRestaurant)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    void createDuplicateName() throws Exception {
        Restaurant newRestaurant = new Restaurant("Тануки", "55555555555555555555555");
            MvcResult result = perform(MockMvcRequestBuilders.post(REST_URL)
                    .with(userHttpBasic(admin))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonConverter.writeValue(newRestaurant)))
                    .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }
}