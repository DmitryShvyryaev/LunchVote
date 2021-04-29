package ru.topjava.lunchvote.testdata;

import ru.topjava.lunchvote.model.Vote;
import ru.topjava.lunchvote.to.VoteTo;
import ru.topjava.lunchvote.util.Matcher;

import java.time.LocalTime;
import java.util.List;

import static ru.topjava.lunchvote.testdata.RestaurantTestData.*;
import static ru.topjava.lunchvote.testdata.DateTestData.*;
import static ru.topjava.lunchvote.testdata.UserTestData.*;

public class VoteTestData {

    private VoteTestData() {
    }

    public static final Matcher<VoteTo> VOTE_TO_MATCHER = Matcher.getComparator();

    public static final long START_VOTE_ID = 100016L;

    public static final VoteTo firstDayVoteAdmin = new VoteTo(START_VOTE_ID, FIRST_DAY.atTime(9, 0), rest1.id(), admin.id());
    public static final VoteTo firstDayVoteUser1 = new VoteTo(START_VOTE_ID + 1, FIRST_DAY.atTime(9, 0), rest1.id(), user1.id());
    public static final VoteTo firstDayVoteUser2 = new VoteTo(START_VOTE_ID + 2, FIRST_DAY.atTime(9, 0), rest1.id(), user2.id());
    public static final VoteTo firstDayVoteUser3 = new VoteTo(START_VOTE_ID + 3, FIRST_DAY.atTime(9, 0), rest2.id(), user3.id());
    public static final VoteTo firstDayVoteUser4 = new VoteTo(START_VOTE_ID + 4, FIRST_DAY.atTime(9, 0), rest3.id(), user4.id());

    public static final VoteTo secondDayVoteAdmin = new VoteTo(START_VOTE_ID + 5, SECOND_DAY.atTime(9, 0), rest2.id(), admin.id());
    public static final VoteTo secondDayVoteUser1 = new VoteTo(START_VOTE_ID + 6, SECOND_DAY.atTime(9, 0), rest2.id(), user1.id());
    public static final VoteTo secondDayVoteUser2 = new VoteTo(START_VOTE_ID + 7, SECOND_DAY.atTime(9, 0), rest3.id(), user2.id());
    public static final VoteTo secondDayVoteUser3 = new VoteTo(START_VOTE_ID + 8, SECOND_DAY.atTime(9, 0), rest3.id(), user3.id());
    public static final VoteTo secondDayVoteUser4 = new VoteTo(START_VOTE_ID + 9, SECOND_DAY.atTime(9, 0), rest3.id(), user4.id());

    public static final List<VoteTo> FIRST_DAY_VOTE = List.of(firstDayVoteAdmin,
            firstDayVoteUser1, firstDayVoteUser2, firstDayVoteUser3, firstDayVoteUser4);

    public static final List<VoteTo> SECOND_DAY_VOTE = List.of(secondDayVoteAdmin,
            secondDayVoteUser1, secondDayVoteUser2, secondDayVoteUser3, secondDayVoteUser4);

    public static VoteTo getCreatedVote(LocalTime time) {
        return new VoteTo(null, THIRD_DAY.atTime(time), rest1.id(), admin.id());
    }
}
