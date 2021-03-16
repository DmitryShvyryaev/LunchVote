package ru.topjava.lunchvote.service;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.topjava.lunchvote.model.User;
import ru.topjava.lunchvote.repository.UserRepository;

import java.util.List;

import static ru.topjava.lunchvote.util.ValidationUtil.checkNotFound;
import static ru.topjava.lunchvote.util.ValidationUtil.checkNotFoundWithId;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<User> getAll() {
        return repository.findAll();
    }

    @Override
    public User get(long id) {
        return checkNotFoundWithId(repository.findById(id).orElse(null), id);
    }

    @Override
    public User getByEmail(String email) {
        return checkNotFound(repository.findByEmail(email).orElse(null), "email: " + email);
    }

    @Override
    public User create(User user) {
        Assert.notNull(user, "User must not bu null.");
        return repository.save(user);
    }

    @Override
    public User update(User user) {
        Assert.notNull(user, "User must not bu null.");
        return checkNotFoundWithId(repository.save(user), user.getId());
    }

    @Override
    public void delete(long id) {
        repository.deleteById(id);
    }
}
