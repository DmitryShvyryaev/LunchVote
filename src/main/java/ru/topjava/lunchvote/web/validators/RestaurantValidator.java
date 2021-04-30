package ru.topjava.lunchvote.web.validators;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.topjava.lunchvote.model.Restaurant;
import ru.topjava.lunchvote.repository.RestaurantRepository;

@Component
public class RestaurantValidator implements Validator {

    private final RestaurantRepository repository;

    public RestaurantValidator(RestaurantRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Restaurant.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Restaurant restaurant = (Restaurant) o;
        if (StringUtils.hasLength(restaurant.getName())) {
            Restaurant dbRestaurant = repository.findByName(restaurant.getName()).orElse(null);
            if (dbRestaurant != null && !dbRestaurant.getId().equals(restaurant.getId()))
                errors.rejectValue("name", "exception.restaurant.duplicateName");
        }
    }
}
