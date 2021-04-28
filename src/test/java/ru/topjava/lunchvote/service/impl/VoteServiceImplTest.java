package ru.topjava.lunchvote.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.topjava.lunchvote.model.Restaurant;
import ru.topjava.lunchvote.model.Vote;
import ru.topjava.lunchvote.service.AbstractServiceTest;
import ru.topjava.lunchvote.service.VoteService;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static ru.topjava.lunchvote.testdata.DateTestData.*;
import static ru.topjava.lunchvote.testdata.RestaurantTestData.START_SEQ_REST;
import static ru.topjava.lunchvote.testdata.UserTestData.USER_MATCHER;
import static ru.topjava.lunchvote.testdata.UserTestData.users;
import static ru.topjava.lunchvote.testdata.VoteTestData.FIRST_DAY_VOTE;
import static ru.topjava.lunchvote.testdata.VoteTestData.VOTE_MATCHER;

public class VoteServiceImplTest extends AbstractServiceTest {

    @Autowired
    private VoteService voteService;

//    @Test
//    void getAllByDate() {
//        List<Vote> actual = voteService.getAllByDate(FIRST_DAY);
//        VOTE_MATCHER.assertMatch(actual, FIRST_DAY_VOTE);
//        USER_MATCHER.assertMatch(actual.stream().map(Vote::getUser).collect(Collectors.toList()), users);
//    }
//
//    @Test
//    void getAllWithoutVotes() {
//        VOTE_MATCHER.assertMatch(voteService.getAllByDate(THIRD_DAY), Collections.emptyList());
//    }
//
//    @Test
//    void getCountByDate() {
//        assertEquals(3, (int) voteService.getCountByDate(FIRST_DAY, START_SEQ_REST));
//        assertEquals(1, (int) voteService.getCountByDate(FIRST_DAY, START_SEQ_REST + 1));
//        assertEquals(1, (int) voteService.getCountByDate(FIRST_DAY, START_SEQ_REST + 2));
//        assertEquals(0, (int) voteService.getCountByDate(SECOND_DAY, START_SEQ_REST));
//        assertEquals(2, (int) voteService.getCountByDate(SECOND_DAY, START_SEQ_REST + 1));
//        assertEquals(3, (int) voteService.getCountByDate(SECOND_DAY, START_SEQ_REST + 2));
//    }
//
//    @Test
//    void vote() {
//        assertTrue(voteService.vote(LocalDateTime.of(THIRD_DAY, LocalTime.of(11, 20)), 100000, START_SEQ_REST));
//        assertEquals(1, (int) voteService.getCountByDate(THIRD_DAY, START_SEQ_REST));
//    }
//
//    @Test
//    void voteAgainBeforeEleven() {
//        assertTrue(voteService.vote(LocalDateTime.of(FIRST_DAY, LocalTime.of(11, 0)), 100000,
//                START_SEQ_REST + 1));
//        assertEquals(2, (int) voteService.getCountByDate(FIRST_DAY, START_SEQ_REST));
//        assertEquals(2, (int) voteService.getCountByDate(FIRST_DAY, START_SEQ_REST + 1));
//    }
//
//    @Test
//    void voteAgainAfterEleven() {
//        assertFalse(voteService.vote(LocalDateTime.of(FIRST_DAY, LocalTime.of(11, 10)), 100000,
//                START_SEQ_REST + 1));
//        assertEquals(3, (int) voteService.getCountByDate(FIRST_DAY, START_SEQ_REST));
//        assertEquals(1, (int) voteService.getCountByDate(FIRST_DAY, START_SEQ_REST + 1));
//    }

    @Test
    void createInvalid() {
    }
}