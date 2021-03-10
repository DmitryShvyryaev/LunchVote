package ru.topjava.lunchvote.service;

import ru.topjava.lunchvote.model.User;

import java.util.List;

public interface UserService {
    List<User> getAll();

    User get(long id);

    User getByEmail(String email);

    User create(User user);

    User update(User user);

    void delete(long id);
}
