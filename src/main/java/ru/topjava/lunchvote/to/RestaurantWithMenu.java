package ru.topjava.lunchvote.to;

import org.springframework.util.CollectionUtils;
import ru.topjava.lunchvote.model.Dish;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RestaurantWithMenu extends AbstractRestaurantTo {

    private final List<Dish> menu;

    public RestaurantWithMenu(Long id, String name, String description, List<Dish> dishes) {
        super(id, name, description);
        if (CollectionUtils.isEmpty(dishes))
            this.menu = Collections.EMPTY_LIST;
        else {
            this.menu = new ArrayList<>(dishes);
        }
    }

    public List<Dish> getMenu() {
        return menu;
    }

    @Override
    public String toString() {
        return "RestaurantWithDishes{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dishes=" + menu +
                '}';
    }
}
