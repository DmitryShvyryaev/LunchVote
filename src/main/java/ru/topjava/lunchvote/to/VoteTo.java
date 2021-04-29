package ru.topjava.lunchvote.to;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class VoteTo extends BaseTo {

    private LocalDateTime dateTime;

    private Long restaurantId;

    private Long userId;

    public VoteTo() {}

    public VoteTo(Long id, LocalDateTime dateTime, Long restaurantId, Long userId) {
        super(id);
        this.dateTime = dateTime;
        this.restaurantId = restaurantId;
        this.userId = userId;
    }

    public VoteTo(VoteTo voteTo) {
        this(voteTo.id, voteTo.dateTime, voteTo.restaurantId, voteTo.userId);
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
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
                ", date=" + dateTime +
                ", restaurantId=" + restaurantId +
                ", userId=" + userId +
                '}';
    }
}
