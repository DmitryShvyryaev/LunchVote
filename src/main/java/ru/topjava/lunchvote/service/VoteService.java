package ru.topjava.lunchvote.service;

import ru.topjava.lunchvote.model.Vote;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface VoteService {
    boolean vote(LocalDateTime dateTime, long userId, long restaurantId);

    List<Vote> getAllByDate(LocalDate date);

    Integer getCountByDate(LocalDate date, long restaurantId);
}
