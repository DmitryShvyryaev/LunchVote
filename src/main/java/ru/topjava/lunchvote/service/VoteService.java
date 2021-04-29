package ru.topjava.lunchvote.service;

import ru.topjava.lunchvote.to.VoteTo;

import java.time.LocalDate;
import java.util.List;

public interface VoteService {
    VoteTo create(VoteTo voteTo);

    void delete(LocalDate date, long userId);

    VoteTo get(LocalDate date, long userId);

    List<VoteTo> getAllByUser(long userId);

    List<VoteTo> getAll();

    List<VoteTo> getAllByDate(LocalDate date);

    void deleteByDate(LocalDate date);
}
