package ru.topjava.lunchvote.web.dish;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.topjava.lunchvote.model.Dish;
import ru.topjava.lunchvote.service.DishService;
import ru.topjava.lunchvote.web.validators.RestaurantPathVariableValidator;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class DishController {

    static final String REST_URL = "rest/restaurants/{restaurantId}/dishes";

    private final DishService dishService;
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final RestaurantPathVariableValidator pathVariableValidator;

    public DishController(DishService dishService, RestaurantPathVariableValidator pathVariableValidator) {
        this.dishService = dishService;
        this.pathVariableValidator = pathVariableValidator;
    }

    @GetMapping
    public List<Dish> getAll(@PathVariable long restaurantId) {
        LocalDate today = LocalDate.now();
        log.info("Get all dishes for date {} for restaurant with id {}", today, restaurantId);
        pathVariableValidator.validatePathVariable(restaurantId);
        return dishService.getAll(today, restaurantId);
    }

    @GetMapping("/{id}")
    public Dish get(@PathVariable long restaurantId, @PathVariable long id) {
        log.info("Get dish with id {} for restaurant with id {}", id, restaurantId);
        pathVariableValidator.validatePathVariable(restaurantId);
        return dishService.get(id, restaurantId);
    }
}
