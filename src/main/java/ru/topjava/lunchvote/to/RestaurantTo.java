package ru.topjava.lunchvote.to;

public class RestaurantTo {
    private final Long id;

    private final String name;

    private final String description;

    private final Integer voteCount;

    public RestaurantTo(Long id, String name, String description, Integer voteCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.voteCount = voteCount;
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

    public Integer getVoteCount() {
        return voteCount;
    }

    @Override
    public String toString() {
        return "RestaurantTo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", voteCount=" + voteCount +
                '}';
    }
}
