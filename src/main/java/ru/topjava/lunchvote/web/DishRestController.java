package ru.topjava.lunchvote.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.topjava.lunchvote.model.Dish;
import ru.topjava.lunchvote.service.DishService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = DishRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class DishRestController {

    @Autowired
    private DishService dishService;
    static final String REST_URL = "/rest/dishes";
    private final Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping
    public List<Dish> getMenuForToday(@PathVariable long restaurantId) {
        LocalDate today = LocalDate.now();
        log.info("Get menu for restaurant {}", restaurantId);
        return dishService.getAll(today, restaurantId);
    }

}
