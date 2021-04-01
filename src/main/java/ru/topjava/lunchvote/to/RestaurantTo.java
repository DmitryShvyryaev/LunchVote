package ru.topjava.lunchvote.to;

import ru.topjava.lunchvote.model.Dish;
import ru.topjava.lunchvote.model.Restaurant;

import java.util.List;

public class RestaurantTo {
    private final Long id;

    private final String name;

    private final String description;

    private final List<Dish> menu;

    private final Integer rating;

    public RestaurantTo(Long id, String name, String description, List<Dish> menu, Integer rating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.menu = menu;
        this.rating = rating;
    }

    public RestaurantTo(Restaurant restaurant, int rating) {
        this(restaurant.getId(), restaurant.getName(), restaurant.getDescription(), restaurant.getMenu(), rating);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Dish> getMenu() {
        return menu;
    }

    public Integer getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return "RestarauntTo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", menu=" + menu +
                ", rating=" + rating +
                '}';
    }
}
