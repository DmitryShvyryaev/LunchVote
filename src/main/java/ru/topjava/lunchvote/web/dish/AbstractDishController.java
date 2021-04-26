package ru.topjava.lunchvote.web.dish;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.topjava.lunchvote.model.Dish;
import ru.topjava.lunchvote.service.DishService;

import java.time.LocalDate;
import java.util.List;

import static ru.topjava.lunchvote.util.ValidationUtil.checkNew;
import static ru.topjava.lunchvote.util.ValidationUtil.assureIdConsistent;

public abstract class AbstractDishController {

    @Autowired
    protected DishService dishService;
    protected final Logger log = LoggerFactory.getLogger(getClass());

    public List<Dish> getAll(long restaurantId) {
        LocalDate today = LocalDate.now();
        log.info("Get all dishes for date {} for restaurant with id {}", today, restaurantId);
        return dishService.getAll(today, restaurantId);
    }

    public Dish get(long id, long restaurantId) {
        log.info("Get dish with id {} for restaurant with id {}", id, restaurantId);
        return dishService.get(id, restaurantId);
    }

    public Dish create(Dish dish, long restaurantId) {
        LocalDate today = LocalDate.now();
        log.info("Create dish {} for restaurant {}", dish, restaurantId);
        checkNew(dish);
        return dishService.create(restaurantId, dish);
    }

    public void update(long restaurantId, long id, Dish dish) {
        log.info("Update dish {} with id {}", dish, id);
        assureIdConsistent(dish, id);
        dishService.update(restaurantId, dish);
    }

    public void delete(long restaurantId, long id) {
        log.info("Delete dish with id {}", id);
        dishService.delete(restaurantId, id);
    }
}
