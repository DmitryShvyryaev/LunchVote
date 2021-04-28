package ru.topjava.lunchvote.service;

import ru.topjava.lunchvote.to.VoteTo;

import java.time.LocalDate;
import java.util.List;

public interface VoteService {
    VoteTo create(VoteTo voteTo, long userId);

    boolean delete(long id);

    VoteTo get(LocalDate date, long userId);

    List<VoteTo> getAllByDate(LocalDate date);

    void clear(LocalDate startDate, LocalDate endDate);
}
