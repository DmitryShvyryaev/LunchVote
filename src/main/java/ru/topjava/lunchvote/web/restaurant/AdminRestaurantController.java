package ru.topjava.lunchvote.web.restaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.topjava.lunchvote.model.Restaurant;
import ru.topjava.lunchvote.service.RestaurantService;
import ru.topjava.lunchvote.web.validators.RestaurantValidator;

import javax.validation.Valid;
import java.net.URI;

import static ru.topjava.lunchvote.util.ValidationUtil.assureIdConsistent;
import static ru.topjava.lunchvote.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestaurantController {

    static final String REST_URL = "/rest/admin/restaurants";

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final RestaurantService restaurantService;

    private final RestaurantValidator restaurantValidator;

    private final Validator validator;

    public AdminRestaurantController(RestaurantService restaurantService, @Qualifier("defaultValidator") Validator validator, RestaurantValidator restaurantValidator) {
        this.restaurantService = restaurantService;
        this.validator = validator;
        this.restaurantValidator = restaurantValidator;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(restaurantValidator);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createWithLocation(@Valid @RequestBody Restaurant restaurant) {
        log.info("Create restaurant {}", restaurant);
        checkNew(restaurant);
        Restaurant created = restaurantService.create(restaurant);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uri).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable long id) {
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
