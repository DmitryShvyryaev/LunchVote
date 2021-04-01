package ru.topjava.lunchvote.service.impl;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.topjava.lunchvote.exception.NotFoundException;
import ru.topjava.lunchvote.model.Role;
import ru.topjava.lunchvote.model.User;
import ru.topjava.lunchvote.service.AbstractServiceTest;
import ru.topjava.lunchvote.service.UserService;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.topjava.lunchvote.testdata.UserTestData.*;

public class UserServiceImplTest extends AbstractServiceTest {

    @Autowired
    private UserService userService;

    @BeforeEach
    public void evictCache() {
        cacheManager.getCache("users").clear();
    }

    @Test
    void getAll() {
        USER_MATCHER.assertMatch(userService.getAll(), users);
    }

    @Test
    void get() {
        USER_MATCHER.assertMatch(userService.get(USER_ID), admin);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> userService.get(10));
    }

    @Test
    void getByEmail() {
        User actual = userService.getByEmail(admin.getEmail());
        USER_MATCHER.assertMatch(actual, admin);
    }

    @Test
    void getByEmailNotFound() {
        assertThrows(NotFoundException.class, () -> userService.getByEmail("notFoundEmail@email.ru"));
    }

    @Test
    void create() {
        User created = userService.create(getCreated());
        User newUser = getCreated();
        newUser.setId(created.id());
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(userService.get(created.id()), newUser);
    }

    @Test
    void createDuplicate() {
        assertThrows(DataAccessException.class, () -> userService.create(new User(null, "Vasek", "user1@email.com", "password", Role.USER)));
    }

    @Test
    void update() {
        userService.update(getUpdated());
        USER_MATCHER.assertMatch(userService.get(USER_ID + 1), getUpdated());
    }

    @Test
    void delete() {
        userService.delete(USER_ID);
        assertThrows(NotFoundException.class, () -> userService.get(USER_ID));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> userService.get(10));
    }
}