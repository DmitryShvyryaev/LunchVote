package ru.topjava.lunchvote.web.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.topjava.lunchvote.model.User;

import java.io.Serial;
import java.util.Collection;

public class AuthorizedUser implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;

    private final User user;

    public AuthorizedUser(User user) {
        this.user = user;
    }

    public long getId() {
        return user.id();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.isEnabled();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isEnabled();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.isEnabled();
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }
}
