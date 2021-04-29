package ru.topjava.lunchvote.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.topjava.lunchvote.exception.NotFoundException;
import ru.topjava.lunchvote.exception.RepeatVoteException;
import ru.topjava.lunchvote.service.AbstractServiceTest;
import ru.topjava.lunchvote.service.VoteService;
import ru.topjava.lunchvote.to.VoteTo;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.topjava.lunchvote.testdata.DateTestData.FIRST_DAY;
import static ru.topjava.lunchvote.testdata.DateTestData.THIRD_DAY;
import static ru.topjava.lunchvote.testdata.RestaurantTestData.rest3;
import static ru.topjava.lunchvote.testdata.UserTestData.admin;
import static ru.topjava.lunchvote.testdata.VoteTestData.*;

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
        VoteTo created = voteService.create(newVote);
        newVote.setId(created.id());
        VOTE_TO_MATCHER.assertMatch(created, newVote);
        VOTE_TO_MATCHER.assertMatch(voteService.get(newVote.getDateTime().toLocalDate(), newVote.getUserId()), newVote);
    }

    @Test
    void createRepeat() {
        VoteTo newVote = new VoteTo(null, FIRST_DAY.atTime(10, 0), rest3.id(), admin.id());
        VoteTo created = voteService.create(newVote);
        newVote.setId(created.id());
        VOTE_TO_MATCHER.assertMatch(created, newVote);
        VOTE_TO_MATCHER.assertMatch(voteService.get(FIRST_DAY, admin.id()), newVote);
    }

    @Test
    void createRepeatAfterLimit() {
        VoteTo newVote = new VoteTo(null, FIRST_DAY.atTime(11, 0, 1), rest3.id(), admin.id());
        assertThrows(RepeatVoteException.class, () -> voteService.create(newVote));
    }

    @Test
    void delete() {
        voteService.delete(FIRST_DAY, admin.id());
        assertThrows(NotFoundException.class, () -> voteService.get(FIRST_DAY, admin.id()));
    }

    @Test
    void getAllByUser() {
        List<VoteTo> actual = voteService.getAllByUser(admin.id());
        VOTE_TO_MATCHER.assertMatch(actual, List.of(firstDayVoteAdmin, secondDayVoteAdmin));
    }

    @Test
    void getAll() {
        List<VoteTo> expected = new ArrayList<>();
        expected.addAll(FIRST_DAY_VOTE);
        expected.addAll(SECOND_DAY_VOTE);
        VOTE_TO_MATCHER.assertMatch(voteService.getAll(), expected);
    }

    @Test
    void getAllByDate() {
        List<VoteTo> actual = voteService.getAllByDate(FIRST_DAY);
        VOTE_TO_MATCHER.assertMatch(actual, FIRST_DAY_VOTE);
    }
}