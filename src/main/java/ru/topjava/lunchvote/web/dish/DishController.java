package ru.topjava.lunchvote.web.dish;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.topjava.lunchvote.model.Dish;

import java.util.List;

@RestController
@RequestMapping(value = DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class DishController extends AbstractDishController {

    static final String REST_URL = "rest/restaurants/{restaurantId}/dishes";

    @Override
    @GetMapping
    public List<Dish> getAll(@PathVariable long restaurantId) {
        return super.getAll(restaurantId);
    }

    @Override
    @GetMapping("/{id}")
    public Dish get(@PathVariable long restaurantId, @PathVariable long id) {
        return super.get(id, restaurantId);
    }
}
