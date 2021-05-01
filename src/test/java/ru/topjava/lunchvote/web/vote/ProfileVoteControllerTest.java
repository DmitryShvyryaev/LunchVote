package ru.topjava.lunchvote.web.vote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.topjava.lunchvote.exception.NotFoundException;
import ru.topjava.lunchvote.service.VoteService;
import ru.topjava.lunchvote.to.VoteTo;
import ru.topjava.lunchvote.util.JsonConverter;
import ru.topjava.lunchvote.web.AbstractControllerTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.topjava.lunchvote.testdata.RestaurantTestData.rest1;
import static ru.topjava.lunchvote.testdata.RestaurantTestData.rest2;
import static ru.topjava.lunchvote.testdata.UserTestData.*;
import static ru.topjava.lunchvote.testdata.VoteTestData.*;
import static ru.topjava.lunchvote.web.vote.ProfileVoteController.REST_URL;

class ProfileVoteControllerTest extends AbstractControllerTest {

    @Autowired
    private VoteService voteService;

    @Autowired
    private JsonConverter jsonConverter;

    @Test
    void get() throws Exception {
        VoteTo newVote = new VoteTo(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), rest1.id(), user1.id());
        voteService.create(newVote);

        MvcResult result = perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        VoteTo actual = jsonConverter.readValueFromJson(result, VoteTo.class);
        newVote.setId(actual.getId());
        VOTE_TO_MATCHER.assertMatch(actual, newVote);
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/all")
                .with(userHttpBasic(user1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.checkJson(List.of(firstDayVoteUser1, secondDayVoteUser1)));
    }

    @Test
    void create() throws Exception {
        MvcResult result = perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(user2))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.writeValue(rest2.id())))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        VoteTo actual = jsonConverter.readValueFromJson(result, VoteTo.class);
        VoteTo expected = new VoteTo(actual.getId(), actual.getDateTime(), rest2.id(), user2.id());
        VOTE_TO_MATCHER.assertMatch(actual, expected);
        VOTE_TO_MATCHER.assertMatch(voteService.get(expected.getDateTime().toLocalDate(), user2.id()), expected);
    }

    @Test
    void delete() throws Exception {
        LocalDate today = LocalDate.now();
        VoteTo newVote = new VoteTo(today.atTime(10, 0), rest1.id(), user3.id());
        VoteTo created = voteService.create(newVote);
        newVote.setId(created.id());
        VOTE_TO_MATCHER.assertMatch(voteService.get(today, user3.id()), newVote);
        perform(MockMvcRequestBuilders.delete(REST_URL)
                .with(userHttpBasic(user3)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> voteService.get(today, user3.id()));
    }

    @Test
    void voteUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void voteNotExist() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(user2))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonConverter.writeValue(rest2.id() + 79)))
                .andDo(print());
    }
}