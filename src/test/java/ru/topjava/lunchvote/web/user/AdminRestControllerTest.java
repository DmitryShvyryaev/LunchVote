package ru.topjava.lunchvote.web.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.topjava.lunchvote.exception.NotFoundException;
import ru.topjava.lunchvote.model.User;
import ru.topjava.lunchvote.service.UserService;
import ru.topjava.lunchvote.util.JsonConverter;
import ru.topjava.lunchvote.web.AbstractControllerTest;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.topjava.lunchvote.testdata.UserTestData.*;
import static ru.topjava.lunchvote.web.user.AdminRestController.REST_URL;

class AdminRestControllerTest extends AbstractControllerTest {

    @Autowired
    private UserService userService;

    @Autowired
    private JsonConverter jsonConverter;

    @BeforeEach
    public void evictCache() {
        cacheManager.getCache("users").clear();
    }

    @Test
    void getAll() throws Exception {
        MvcResult result = perform(MockMvcRequestBuilders.get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        List<User> actual = jsonConverter.readValuesFromJson(result.getResponse().getContentAsString(), User.class);
        USER_MATCHER.assertMatch(actual, users);
    }

    @Test
    void get() throws Exception {
        MvcResult result = perform(MockMvcRequestBuilders.get(REST_URL + "/" + USER_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        User actual = jsonConverter.readValueFromJson(result.getResponse().getContentAsString(), User.class);
        USER_MATCHER.assertMatch(actual, admin);
    }

    @Test
    void getByEmail() throws Exception {
        MvcResult result = perform(MockMvcRequestBuilders.get(REST_URL + "/by?email=" + admin.getEmail()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        User actual = jsonConverter.readValueFromJson(result.getResponse().getContentAsString(), User.class);
        USER_MATCHER.assertMatch(actual, admin);
    }

    @Test
    void createWithLocation() throws Exception {
        User newUser = getCreated();
        MvcResult result = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.writeAdditionProperties(newUser, "password", newUser.getPassword())))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        User created = jsonConverter.readValueFromJson(result.getResponse().getContentAsString(), User.class);
        long newId = created.id();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(userService.get(newId), newUser);
    }

    @Test
    void update() throws Exception {
        User updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + "/" + updated.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.writeAdditionProperties(updated, "password", updated.getPassword())))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();

        USER_MATCHER.assertMatch(userService.get(updated.getId()), updated);
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + "/" + USER_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        Assertions.assertThrows(NotFoundException.class, () -> userService.get(USER_ID));
    }

    @Test
    void enable() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL + "/" + USER_ID)
                .param("enabled", "false")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        Assertions.assertFalse(userService.get(USER_ID).isEnabled());
    }
}