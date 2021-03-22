package ru.topjava.lunchvote.testdata;

import ru.topjava.lunchvote.model.Vote;
import ru.topjava.lunchvote.util.Matcher;

import java.util.List;

import static ru.topjava.lunchvote.testdata.RestaurantTestData.*;
import static ru.topjava.lunchvote.testdata.DateTestData.*;
import static ru.topjava.lunchvote.testdata.UserTestData.*;

public class VoteTestData {
    public static final Matcher<Vote> VOTE_MATCHER = Matcher.getComparator("user");

    public static final long START_VOTE_ID = 100016L;

    public static final List<Vote> FIRST_DAY_VOTE = List.of(new Vote(START_VOTE_ID ,FIRST_DAY, rest1, admin),
            new Vote(START_VOTE_ID + 1, FIRST_DAY, rest1, user1),
            new Vote(START_VOTE_ID + 2, FIRST_DAY, rest1, user2),
            new Vote(START_VOTE_ID + 3, FIRST_DAY, rest2, user3),
            new Vote(START_VOTE_ID + 4, FIRST_DAY, rest3, user4));

    public static final List<Vote> SECOND_DAY_VOTE = List.of(new Vote(START_VOTE_ID  +5, SECOND_DAY, rest2, admin),
            new Vote(START_VOTE_ID + 6, SECOND_DAY, rest2, user1),
            new Vote(START_VOTE_ID + 7, SECOND_DAY, rest3, user2),
            new Vote(START_VOTE_ID + 8, SECOND_DAY, rest3, user3),
            new Vote(START_VOTE_ID + 9, SECOND_DAY, rest3, user4));
}
