package ru.topjava.lunchvote.web.validators;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.topjava.lunchvote.HasIdAndEmail;
import ru.topjava.lunchvote.model.User;
import ru.topjava.lunchvote.repository.UserRepository;

@Component
public class UserValidator implements Validator {

    private final UserRepository repository;

    public UserValidator(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return HasIdAndEmail.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        HasIdAndEmail user = (HasIdAndEmail) o;
        if (StringUtils.hasLength(user.getEmail())) {
            User dbUser = repository.findByEmail(user.getEmail()).orElse(null);
            if (dbUser != null && !dbUser.getId().equals(user.getId())) {
                errors.rejectValue("email", "exception.user.duplicateEmail");
            }
        }
    }
}
