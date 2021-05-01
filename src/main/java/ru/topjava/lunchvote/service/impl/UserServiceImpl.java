package ru.topjava.lunchvote.service.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import ru.topjava.lunchvote.model.User;
import ru.topjava.lunchvote.repository.UserRepository;
import ru.topjava.lunchvote.service.UserService;
import ru.topjava.lunchvote.to.UserTo;
import ru.topjava.lunchvote.web.security.AuthorizedUser;

import java.util.List;

import static ru.topjava.lunchvote.util.UserUtil.createFromTo;
import static ru.topjava.lunchvote.util.UserUtil.updateFromTo;
import static ru.topjava.lunchvote.util.ValidationUtil.checkNotFound;
import static ru.topjava.lunchvote.util.ValidationUtil.checkNotFoundWithId;

@Service("userService")
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Cacheable(value = "users")
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
        return repository.findByEmail(email).orElse(null);
    }

    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    @Override
    public User create(User user) {
        Assert.notNull(user, "User must not bu null.");
        return prepareAndSave(user);
    }

    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    @Override
    public User create(UserTo userTo) {
        Assert.notNull(userTo, "User must not bu null.");
        User user = createFromTo(userTo);
        return prepareAndSave(user);
    }

    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    @Override
    public User update(User user) {
        Assert.notNull(user, "User must not bu null.");
        return checkNotFoundWithId(prepareAndSave(user), user.id());
    }

    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    @Override
    public User update(UserTo userTo) {
        User user = get(userTo.getId());
        return prepareAndSave(updateFromTo(user, userTo));
    }

    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    @Override
    public void delete(long id) {
        repository.deleteById(id);
    }

    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    @Override
    public void enable(long id, boolean enable) {
        User user = get(id);
        user.setEnabled(enable);
    }

    @Override
    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmail(email.toLowerCase()).orElseThrow(() -> new UsernameNotFoundException("User " + email + " is not found"));
        return new AuthorizedUser(user);
    }

    private User prepareAndSave(User user) {
        String password = user.getPassword();
        user.setPassword(StringUtils.hasText(password) ? encoder.encode(password) : password);
        return repository.save(user);
    }
}
