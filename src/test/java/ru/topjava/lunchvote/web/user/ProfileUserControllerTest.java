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
import ru.topjava.lunchvote.to.UserTo;
import ru.topjava.lunchvote.util.JsonConverter;
import ru.topjava.lunchvote.util.UserUtil;
import ru.topjava.lunchvote.web.AbstractControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.topjava.lunchvote.testdata.UserTestData.*;
import static ru.topjava.lunchvote.web.user.ProfileUserController.REST_URL;

class ProfileUserControllerTest extends AbstractControllerTest {

    @Autowired
    private UserService userService;

    @Autowired
    private JsonConverter jsonConverter;

    @BeforeEach
    public void evictCache() {
        cacheManager.getCache("users").clear();
    }

    @Test
    void get() throws Exception {
        MvcResult result = perform(MockMvcRequestBuilders.get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        User actual = jsonConverter.readValueFromJson(result.getResponse().getContentAsString(), User.class);
        USER_MATCHER.assertMatch(actual, admin);
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL))
                .andDo(print())
                .andExpect(status().isNoContent());
        Assertions.assertThrows(NotFoundException.class, () -> userService.get(USER_ID));
    }

    @Test
    void update() throws Exception {
        UserTo updated = new UserTo(null, "updated", "update@yahoo.com", "updatePass");
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        USER_MATCHER.assertMatch(userService.get(USER_ID), UserUtil.updateFromTo(new User(admin), updated));
    }

    @Test
    void register() throws Exception {
        UserTo userTo = new UserTo(null, "newName", "mewEmail@gamil.com", "newPassword");
        User newUser = UserUtil.createFromTo(userTo);
        MvcResult result = perform(MockMvcRequestBuilders.post(REST_URL + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.writeValue(userTo)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        User created = jsonConverter.readValueFromJson(result.getResponse().getContentAsString(), User.class);
        long newId = created.id();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(userService.get(newId), newUser);
    }
}