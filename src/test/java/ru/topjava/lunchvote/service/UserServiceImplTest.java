package ru.topjava.lunchvote.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.topjava.lunchvote.exception.NotFoundException;
import ru.topjava.lunchvote.model.Restaurant;
import ru.topjava.lunchvote.model.Role;
import ru.topjava.lunchvote.model.User;

import static org.junit.Assert.*;
import static ru.topjava.lunchvote.testdata.UserTestData.*;

public class UserServiceImplTest extends AbstractServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void getAll() {
        USER_MATCHER.assertMatch(userService.getAll(), users);
    }

    @Test
    public void get() {
        USER_MATCHER.assertMatch(userService.get(USER_ID), admin);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> userService.get(10));
    }

    @Test
    public void getByEmail() {
        User actual = userService.getByEmail(admin.getEmail());
        USER_MATCHER.assertMatch(actual, admin);
    }

    @Test
    public void getByEmailNotFound() {
        assertThrows(NotFoundException.class, () -> userService.getByEmail("notFoundEmail@email.ru"));
    }

    @Test
    public void create() {
        User created = userService.create(getCreated());
        User newUser = getCreated();
        newUser.setId(created.getId());
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(userService.get(created.getId()), newUser);
    }

    @Test
    public void createDuplicate() {
        assertThrows(DataAccessException.class, () -> userService.create(new User(null, "Vasek", "user1@email.com", "password", Role.USER)));
    }

    @Test
    public void update() {
        userService.update(getUpdated());
        USER_MATCHER.assertMatch(userService.get(USER_ID + 1), getUpdated());
    }

    @Test
    public void delete() {
        userService.delete(USER_ID);
        assertThrows(NotFoundException.class, () -> userService.get(USER_ID));
    }
}