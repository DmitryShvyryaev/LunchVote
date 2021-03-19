package ru.topjava.lunchvote.to;

public class RestaurantWithRating extends AbstractRestaurantTo{

    private final Integer voteCount;

    public RestaurantWithRating(Long id, String name, String description, Integer voteCount) {
        super(id, name, description);
        this.voteCount = voteCount;
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
