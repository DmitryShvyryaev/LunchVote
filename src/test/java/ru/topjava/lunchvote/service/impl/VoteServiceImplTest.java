package ru.topjava.lunchvote.service.impl;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.topjava.lunchvote.model.Vote;
import ru.topjava.lunchvote.service.AbstractServiceTest;
import ru.topjava.lunchvote.service.VoteService;
import ru.topjava.lunchvote.testdata.UserTestData;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.topjava.lunchvote.testdata.RestaurantTestData.*;
import static ru.topjava.lunchvote.testdata.DateTestData.*;
import static ru.topjava.lunchvote.testdata.VoteTestData.*;
import static ru.topjava.lunchvote.testdata.UserTestData.*;

public class VoteServiceImplTest extends AbstractServiceTest {

    @Autowired
    private VoteService voteService;

    @Test
    public void getAllByDate() {
        List<Vote> actual = voteService.getAllByDate(FIRST_DAY);
        VOTE_MATCHER.assertMatch(actual, FIRST_DAY_VOTE);
        USER_MATCHER.assertMatch(actual.stream().map(Vote::getUser).collect(Collectors.toList()), users);
    }

    @Test
    public void getAllWithoutVotes() {
        VOTE_MATCHER.assertMatch(voteService.getAllByDate(THIRD_DAY), Collections.EMPTY_LIST);
    }

    @Test
    public void getCountByDate() {
        Assert.assertEquals(3, (int) voteService.getCountByDate(FIRST_DAY, START_SEQ_REST));
        Assert.assertEquals(1, (int) voteService.getCountByDate(FIRST_DAY, START_SEQ_REST + 1));
        Assert.assertEquals(1, (int) voteService.getCountByDate(FIRST_DAY, START_SEQ_REST + 2));
        Assert.assertEquals(0, (int) voteService.getCountByDate(SECOND_DAY, START_SEQ_REST));
        Assert.assertEquals(2, (int) voteService.getCountByDate(SECOND_DAY, START_SEQ_REST + 1));
        Assert.assertEquals(3, (int) voteService.getCountByDate(SECOND_DAY, START_SEQ_REST + 2));
    }

    @Test
    public void vote() {
        Assert.assertTrue(voteService.vote(LocalDateTime.of(THIRD_DAY, LocalTime.of(11, 20)), 100000, START_SEQ_REST));
        Assert.assertEquals(1, (int) voteService.getCountByDate(THIRD_DAY, START_SEQ_REST));
    }

    @Test
    public void voteAgainBeforeEleven() {
        Assert.assertTrue(voteService.vote(LocalDateTime.of(FIRST_DAY, LocalTime.of(11, 0)), 100000,
                START_SEQ_REST + 1));
        Assert.assertEquals(2, (int) voteService.getCountByDate(FIRST_DAY, START_SEQ_REST));
        Assert.assertEquals(2, (int) voteService.getCountByDate(FIRST_DAY, START_SEQ_REST + 1));
    }

    @Test
    public void voteAgainAfterEleven() {
        Assert.assertFalse(voteService.vote(LocalDateTime.of(FIRST_DAY, LocalTime.of(11, 10)), 100000,
                START_SEQ_REST + 1));
        Assert.assertEquals(3, (int) voteService.getCountByDate(FIRST_DAY, START_SEQ_REST));
        Assert.assertEquals(1, (int) voteService.getCountByDate(FIRST_DAY, START_SEQ_REST + 1));
    }

}