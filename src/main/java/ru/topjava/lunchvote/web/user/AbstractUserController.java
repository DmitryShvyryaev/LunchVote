package ru.topjava.lunchvote.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.topjava.lunchvote.model.User;
import ru.topjava.lunchvote.service.UserService;
import ru.topjava.lunchvote.to.UserTo;

import java.util.List;

import static ru.topjava.lunchvote.util.ValidationUtil.assureIdConsistent;
import static ru.topjava.lunchvote.util.ValidationUtil.checkNew;

public abstract class AbstractUserController {

    @Autowired
    protected UserService userService;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public List<User> getAll() {
        log.info("Get all users");
        return userService.getAll();
    }

    public User get(long id) {
        log.info("Get user with id {}", id);
        return userService.get(id);
    }

    public User getByEmail(String email) {
        log.info("Get user with email {}", email);
        return userService.getByEmail(email);
    }

    public User create(User user) {
        log.info("Create user {}", user);
        checkNew(user);
        return userService.create(user);
    }

    public User create(UserTo userTo) {
        log.info("Create user {}", userTo);
        checkNew(userTo);
        return userService.create(userTo);
    }

    public void update(User user, long id) {
        log.info("Update user {} with id {}", user, id);
        assureIdConsistent(user, id);
        userService.update(user);
    }

    public void update(UserTo userTo, long id) {
        log.info("Update user {} with id {}", userTo, id);
        assureIdConsistent(userTo, id);
        userService.update(userTo);
    }

    public void delete(long id) {
        log.info("Delete user with id {}", id);
        userService.delete(id);
    }

    public void enable(long id, boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", id);
        userService.enable(id, enabled);
    }
}
