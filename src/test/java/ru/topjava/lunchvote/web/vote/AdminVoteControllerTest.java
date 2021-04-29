package ru.topjava.lunchvote.web.vote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.topjava.lunchvote.to.VoteTo;
import ru.topjava.lunchvote.util.JsonConverter;
import ru.topjava.lunchvote.web.AbstractControllerTest;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.topjava.lunchvote.testdata.UserTestData.admin;
import static ru.topjava.lunchvote.testdata.UserTestData.user1;
import static ru.topjava.lunchvote.testdata.VoteTestData.*;
import static ru.topjava.lunchvote.web.vote.AdminVoteController.REST_URL;

class AdminVoteControllerTest extends AbstractControllerTest {

    @Autowired
    private JsonConverter jsonConverter;

    @Test
    void getAll() throws Exception {
        List<VoteTo> expected = new ArrayList<>();
        expected.addAll(FIRST_DAY_VOTE);
        expected.addAll(SECOND_DAY_VOTE);
        MvcResult result = perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        List<VoteTo> actual = jsonConverter.readValuesFromJson(result, VoteTo.class);
        VOTE_TO_MATCHER.assertMatch(actual, expected);
    }

    @Test
    void getAllByDate() throws Exception {
        MvcResult result = perform(MockMvcRequestBuilders.get(REST_URL + "/byDate")
                .with(userHttpBasic(admin))
                .param("date", "2021-03-15"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        List<VoteTo> actual = jsonConverter.readValuesFromJson(result, VoteTo.class);
        VOTE_TO_MATCHER.assertMatch(actual, FIRST_DAY_VOTE);
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user1)))
                .andExpect(status().isForbidden());
    }
}