package ru.topjava.lunchvote.service.impl;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.topjava.lunchvote.exception.NotFoundException;
import ru.topjava.lunchvote.model.Role;
import ru.topjava.lunchvote.model.User;
import ru.topjava.lunchvote.service.AbstractServiceTest;
import ru.topjava.lunchvote.service.UserService;

import javax.validation.ConstraintViolationException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.topjava.lunchvote.testdata.UserTestData.*;

public class UserServiceImplTest extends AbstractServiceTest {

    @Autowired
    private UserService userService;

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
        assertNull(userService.getByEmail("notFoundEmail@email.ru"));
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
        assertThrows(NotFoundException.class, () -> userService.delete(10));
    }

    @Test
    void enable() {
        userService.enable(USER_ID, false);
        Assertions.assertFalse(userService.get(USER_ID).isEnabled());
        userService.enable(USER_ID, true);
        Assertions.assertTrue(userService.get(USER_ID).isEnabled());
    }

    @Test
    void createInvalid() {
        validateRootCause(ConstraintViolationException.class, () -> userService.create(
                new User(null, "  ", "User@email.ru", "password", Role.USER)));
        validateRootCause(ConstraintViolationException.class, () -> userService.create(
                new User(null, "User", "not email", "password", Role.USER)));
        validateRootCause(ConstraintViolationException.class, () -> userService.create(
                new User(null, "User", "User@email.ru", "  ", Role.USER)));
        validateRootCause(ConstraintViolationException.class, () -> userService.create(
                new User(null, "User", "User@email.ru", "password", true, null, Set.of())));
    }
}