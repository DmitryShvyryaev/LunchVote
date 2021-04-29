package ru.topjava.lunchvote.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.topjava.lunchvote.exception.NotFoundException;
import ru.topjava.lunchvote.service.AbstractServiceTest;
import ru.topjava.lunchvote.service.VoteService;
import ru.topjava.lunchvote.to.VoteTo;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static ru.topjava.lunchvote.testdata.DateTestData.*;
import static ru.topjava.lunchvote.testdata.VoteTestData.*;
import static ru.topjava.lunchvote.testdata.UserTestData.*;

class VoteServiceImplTest extends AbstractServiceTest {

    @Autowired
    private VoteService voteService;

    @Test
    void get() {
        VoteTo actual = voteService.get(FIRST_DAY, admin.id());
        VOTE_TO_MATCHER.assertMatch(actual, firstDayVoteAdmin);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> voteService.get(THIRD_DAY, admin.id()));
    }

    @Test
    void create() {
        VoteTo newVote = new VoteTo(getCreatedVote(LocalTime.of(9, 0)));
        VoteTo created = voteService.create(newVote, newVote.getUserId());
        newVote.setId(created.id());
        VOTE_TO_MATCHER.assertMatch(created, newVote);
        VOTE_TO_MATCHER.assertMatch(voteService.get(newVote.getDateTime().toLocalDate(), newVote.getUserId()), newVote);
    }

    @Test
    void delete() {
    }

    @Test
    void getAllByUser() {
    }

    @Test
    void getAllByDate() {
    }

    @Test
    void clear() {
    }
}