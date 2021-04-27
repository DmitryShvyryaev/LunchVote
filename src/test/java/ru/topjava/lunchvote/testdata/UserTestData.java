package ru.topjava.lunchvote.testdata;

import ru.topjava.lunchvote.model.Role;
import ru.topjava.lunchvote.model.User;
import ru.topjava.lunchvote.util.Matcher;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class UserTestData {

    private UserTestData() {
    }

    public static final Matcher<User> USER_MATCHER = Matcher.getComparator("registered", "password");

    public static final long USER_ID = 100000L;

    public static final User admin = new User(USER_ID, "admin", "admin@email.com", "xxx111", Role.ADMIN, Role.USER);
    public static final User user1 = new User(USER_ID + 1, "user1", "user1@email.com", "xxx222", Role.USER);
    public static final User user2 = new User(USER_ID + 2, "user2", "user2@email.com", "xxx333", Role.USER);
    public static final User user3 = new User(USER_ID + 3, "user3", "user3@email.com", "xxx444", Role.USER);
    public static final User user4 = new User(USER_ID + 4, "user4", "user4@email.com", "xxx555", Role.USER);

    public static final List<User> users = List.of(admin, user1, user2, user3, user4);

    public static User getCreated() {
        return new User(null, "Boris", "boris@email.ru", "borya1993", true, new Date(), Collections.singleton(Role.USER));
    }

    public static User getUpdated() {
        User updated = new User(user1);
        updated.setName("updated");
        updated.setEmail("newEmail@ya.ru");
        updated.setPassword("password");
        updated.setEnabled(false);
        updated.setRoles(Collections.singleton(Role.ADMIN));
        return updated;
    }
}
