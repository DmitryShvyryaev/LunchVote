package ru.topjava.lunchvote.web.dish;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.topjava.lunchvote.model.Dish;
import ru.topjava.lunchvote.service.DishService;

import java.net.URI;
import java.time.LocalDate;

import static ru.topjava.lunchvote.util.ValidationUtil.assureIdConsistent;
import static ru.topjava.lunchvote.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminDishController {

    static final String REST_URL = "/rest/admin/restaurants/{restaurantId}/dishes";

    private final DishService dishService;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public AdminDishController(DishService dishService) {
        this.dishService = dishService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@PathVariable long restaurantId, @RequestBody Dish dish) {
        LocalDate today = LocalDate.now();
        log.info("Create dish {} for restaurant {}", dish, restaurantId);
        checkNew(dish);
        Dish created = dishService.create(restaurantId, dish);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uri).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable long restaurantId, @PathVariable long id, @RequestBody Dish dish) {
        log.info("Update dish {} with id {}", dish, id);
        assureIdConsistent(dish, id);
        dishService.update(restaurantId, dish);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long restaurantId, @PathVariable long id) {
        log.info("Delete dish with id {}", id);
        dishService.delete(restaurantId, id);
    }
}
