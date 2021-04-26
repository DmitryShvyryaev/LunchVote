package ru.topjava.lunchvote.web.restaurant;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.topjava.lunchvote.model.Restaurant;

import java.util.List;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController extends AbstractRestaurantController {

    static final String REST_URL = "/rest/restaurants";

    @Override
    @GetMapping
    public List<Restaurant> getAll() {
        return super.getAll();
    }

    @Override
    @GetMapping("/{id}")
    public Restaurant get(@PathVariable long id) {
        return super.get(id);
    }

    @Override
    @GetMapping("with-menu")
    public List<Restaurant> getAllWithMenu() {
        return super.getAllWithMenu();
    }

    @Override
    @GetMapping("/{id}/with-menu")
    public Restaurant getWithMenu(@PathVariable long id) {
        return super.getWithMenu(id);
    }
}
