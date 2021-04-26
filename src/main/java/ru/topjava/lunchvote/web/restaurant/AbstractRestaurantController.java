package ru.topjava.lunchvote.web.restaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import ru.topjava.lunchvote.model.Restaurant;
import ru.topjava.lunchvote.service.RestaurantService;

import java.time.LocalDate;
import java.util.List;

import static ru.topjava.lunchvote.util.ValidationUtil.assureIdConsistent;
import static ru.topjava.lunchvote.util.ValidationUtil.checkNew;

public abstract class AbstractRestaurantController {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    protected RestaurantService restaurantService;

    public List<Restaurant> getAll() {
        log.info("Get all restaurants");
        return restaurantService.getAll();
    }

    public Restaurant get(long id) {
        log.info("Get restaurant with id {}", id);
        return restaurantService.get(id);
    }

    public List<Restaurant> getAllWithMenu() {
        LocalDate today = LocalDate.now();
        log.info("Get all restaurants with menu for date {}", today);
        return restaurantService.getAllWithMenu(today);
    }

    public Restaurant getWithMenu(long id) {
        LocalDate today = LocalDate.now();
        log.info("Get simple restaurant with menu with id {} for date {}", id, today);
        return restaurantService.getWithMenu(id, today);
    }

    public Restaurant create(Restaurant restaurant) {
        log.info("Create restaurant {}", restaurant);
        checkNew(restaurant);
        return restaurantService.create(restaurant);
    }

    public void update(Restaurant restaurant, long id) {
        log.info("Update restaurant {} with id {}", restaurant, id);
        assureIdConsistent(restaurant, id);
        restaurantService.update(restaurant);
    }

    public void delete(long id) {
        log.info("Delete restaurant with id {}", id);
        restaurantService.delete(id);
    }
}
