package ru.topjava.lunchvote.to;

import java.time.LocalDate;

public class VoteTo extends BaseTo {

    private LocalDate date;

    private Long restaurantId;

    private Long userId;

    public VoteTo() {}

    public VoteTo(Long id, LocalDate date, Long restaurantId, Long userId) {
        super(id);
        this.date = date;
        this.restaurantId = restaurantId;
        this.userId = userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "VoteTo{" +
                "id=" + id +
                ", date=" + date +
                ", restaurantId=" + restaurantId +
                ", userId=" + userId +
                '}';
    }
}
