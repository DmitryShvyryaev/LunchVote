package ru.topjava.lunchvote.service;

import ru.topjava.lunchvote.model.Vote;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface VoteService {
    boolean vote(LocalDateTime date, long userId, long restaurantId);

    List<Vote> getAllByDate(LocalDate date);

    List<Vote> getAllByDateAndRestaurant(LocalDate date, long restaurantId);

    void clear(LocalDate date);
}
