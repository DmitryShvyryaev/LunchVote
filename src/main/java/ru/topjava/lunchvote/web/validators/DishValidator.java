package ru.topjava.lunchvote.web.validators;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.topjava.lunchvote.model.Dish;
import ru.topjava.lunchvote.repository.DishRepository;

import java.time.LocalDate;

public class DishValidator implements Validator {

    private final DishRepository repository;

    public DishValidator(DishRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Dish.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
//        Dish dish = (Dish) o;
//        if (StringUtils.hasLength(dish.getName())) {
//            repository.findByNameAndDateAndRestaurantId(dish.getName(), LocalDate.now(), dish.)
//        }
    }
}
