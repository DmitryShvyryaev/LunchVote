package ru.topjava.lunchvote.to;

public class AbstractRestaurantTo {
    protected final Long id;

    protected final String name;

    protected final String description;

    public AbstractRestaurantTo(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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
}
