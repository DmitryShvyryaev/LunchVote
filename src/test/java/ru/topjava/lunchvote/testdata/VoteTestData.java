package ru.topjava.lunchvote.testdata;

import ru.topjava.lunchvote.model.Vote;
import ru.topjava.lunchvote.util.Matcher;

import java.util.List;

import static ru.topjava.lunchvote.testdata.RestaurantTestData.*;
import static ru.topjava.lunchvote.testdata.DateTestData.*;
import static ru.topjava.lunchvote.testdata.UserTestData.*;

public class VoteTestData {
    public static final Matcher<Vote> VOTE_MATCHER = Matcher.getComparator();

    public static final List<Vote> FIRST_DAY_VOTE = List.of(new Vote(FIRST_DAY, rest1, admin),
            new Vote(FIRST_DAY, rest1, user1),
            new Vote(FIRST_DAY, rest1, user2),
            new Vote(FIRST_DAY, rest2, user3),
            new Vote(FIRST_DAY, rest3, user4));

    public static final List<Vote> SECOND_DAY_VOTE = List.of(new Vote(SECOND_DAY, rest2, admin),
            new Vote(SECOND_DAY, rest2, user1),
            new Vote(SECOND_DAY, rest3, user2),
            new Vote(SECOND_DAY, rest3, user3),
            new Vote(SECOND_DAY, rest3, user4));
}
