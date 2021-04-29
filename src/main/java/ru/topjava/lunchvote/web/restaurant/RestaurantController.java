package ru.topjava.lunchvote.web.restaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.topjava.lunchvote.model.Restaurant;
import ru.topjava.lunchvote.service.RestaurantService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {

    static final String REST_URL = "/rest/restaurants";

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public List<Restaurant> getAll() {
        log.info("Get all restaurants");
        return restaurantService.getAll();
    }

    @GetMapping("/{id}")
    public Restaurant get(@PathVariable long id) {
        log.info("Get restaurant with id {}", id);
        return restaurantService.get(id);
    }

    @GetMapping("with-menu")
    public List<Restaurant> getAllWithMenu() {
        LocalDate today = LocalDate.now();
        log.info("Get all restaurants with menu for date {}", today);
        return restaurantService.getAllWithMenu(today);
    }

    @GetMapping("/{id}/with-menu")
    public Restaurant getWithMenu(@PathVariable long id) {
        LocalDate today = LocalDate.now();
        log.info("Get simple restaurant with menu with id {} for date {}", id, today);
        return restaurantService.getWithMenu(id, today);
    }
}
