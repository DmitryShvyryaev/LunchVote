package ru.topjava.lunchvote.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.topjava.lunchvote.model.User;
import ru.topjava.lunchvote.to.UserTo;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> getAll();

    User get(long id);

    User getByEmail(String email);

    User create(User user);

    User create(UserTo userTo);

    User update(User user);

    User update(UserTo userTo);

    void delete(long id);

    void enable(long id, boolean enable);
}
