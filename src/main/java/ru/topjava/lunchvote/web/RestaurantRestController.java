package ru.topjava.lunchvote.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.topjava.lunchvote.model.Restaurant;
import ru.topjava.lunchvote.service.RestaurantService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.topjava.lunchvote.util.ValidationUtil.assureIdConsistent;
import static ru.topjava.lunchvote.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = RestaurantRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantRestController {

    @Autowired
    private RestaurantService restaurantService;
    static final String REST_URL = "/rest/restaurants";
    private final Logger log = LoggerFactory.getLogger(getClass());

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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> create(@RequestBody Restaurant restaurant) {
        log.info("Create restaurant {}", restaurant);
        checkNew(restaurant);
        Restaurant created = restaurantService.create(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Restaurant restaurant, @PathVariable long id) {
        log.info("Update restaurant {} with id {}", restaurant, id);
        assureIdConsistent(restaurant, id);
        restaurantService.update(restaurant);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        log.info("Delete restaurant with id {}", id);
        restaurantService.delete(id);
    }
}
