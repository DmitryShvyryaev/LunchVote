package ru.topjava.lunchvote.web.validators;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import ru.topjava.lunchvote.exception.NotFoundException;
import ru.topjava.lunchvote.repository.RestaurantRepository;

@Component
public class RestaurantPathVariableValidator {

    private final RestaurantRepository repository;

    private final MessageSourceAccessor messageSourceAccessor;

    public RestaurantPathVariableValidator(RestaurantRepository repository, MessageSourceAccessor messageSourceAccessor) {
        this.repository = repository;
        this.messageSourceAccessor = messageSourceAccessor;
    }

    public void validatePathVariable(long pathVariable) {
        if (!repository.existsById(pathVariable)) {
            throw new NotFoundException(messageSourceAccessor.getMessage("exception.restaurant.notFound"));
        }
    }
}
