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
import static ru.topjava.lunchvote.exception.ErrorType.VALIDATION_ERROR;
import static ru.topjava.lunchvote.testdata.UserTestData.*;
import static ru.topjava.lunchvote.web.user.ProfileUserController.REST_URL;

class ProfileUserControllerTest extends AbstractControllerTest {

    @Autowired
    private UserService userService;

    @Autowired
    private JsonConverter jsonConverter;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user2)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.checkJson(user2));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL)
                .with(userHttpBasic(user1)))
                .andDo(print())
                .andExpect(status().isNoContent());
        Assertions.assertThrows(NotFoundException.class, () -> userService.get(USER_ID + 1));
    }

    @Test
    void update() throws Exception {
        UserTo updated = new UserTo(null, "updated", "update@yahoo.com", "updatePass");
        perform(MockMvcRequestBuilders.put(REST_URL)
                .with(userHttpBasic(user1))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        USER_MATCHER.assertMatch(userService.get(USER_ID + 1), UserUtil.updateFromTo(new User(user1), updated));
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

        User created = jsonConverter.readValueFromJson(result, User.class);
        long newId = created.id();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(userService.get(newId), newUser);
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void registerAuth() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL + "/register")
                .with(userHttpBasic(user1)))
                .andExpect(status().isForbidden());
    }

    @Test
    void registerInvalid() throws Exception {
        UserTo newUser = new UserTo(null, " ", "this is not email", "");
        perform(MockMvcRequestBuilders.post(REST_URL + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.writeValue(newUser)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Test
    void registerDuplicateEmail() throws Exception {
        UserTo newUser = new UserTo(null, "User", "user1@email.com", "Password");
        perform(MockMvcRequestBuilders.post(REST_URL + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.writeValue(newUser)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessage("exception.user.duplicateEmail"));
    }

    @Test
    void updateInvalid() throws Exception {
        UserTo updated = new UserTo(user2.id(), " ", " ", " ");
        perform(MockMvcRequestBuilders.put(REST_URL)
                .with(userHttpBasic(user2))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Test
    void updateDuplicateEmail() throws Exception {
        UserTo updated = new UserTo(user2.id(), user2.getName(), user3.getEmail(), user2.getPassword());
        perform(MockMvcRequestBuilders.put(REST_URL)
                .with(userHttpBasic(user2))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessage("exception.user.duplicateEmail"));
    }
}