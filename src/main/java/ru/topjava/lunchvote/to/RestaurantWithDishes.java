package ru.topjava.lunchvote.to;

import org.springframework.util.CollectionUtils;
import ru.topjava.lunchvote.model.Dish;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RestaurantWithDishes extends AbstractRestaurantTo {

    private final List<Dish> dishes;

    public RestaurantWithDishes(Long id, String name, String description, List<Dish> dishes) {
        super(id, name, description);
        if (CollectionUtils.isEmpty(dishes))
            this.dishes = Collections.EMPTY_LIST;
        else {
            this.dishes = new ArrayList<>();
            Collections.copy(this.dishes, dishes);
        }
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    @Override
    public String toString() {
        return "RestaurantWithDishes{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dishes=" + dishes +
                '}';
    }
}
