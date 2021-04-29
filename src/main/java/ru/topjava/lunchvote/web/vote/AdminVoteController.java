package ru.topjava.lunchvote.web.vote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.topjava.lunchvote.service.VoteService;
import ru.topjava.lunchvote.to.VoteTo;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = AdminVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminVoteController {

    static final String REST_URL = "/rest/admin/votes";

    private final VoteService voteService;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public AdminVoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @GetMapping
    public List<VoteTo> getAll() {
        log.info("Get all votes");
        return voteService.getAll();
    }

    @GetMapping("/byDate")
    public List<VoteTo> getAllByDate(@RequestBody LocalDate date) {
        log.info("Get all votes by date {}", date);
        return voteService.getAllByDate(date);
    }

    @DeleteMapping("/byDate")
    public void deleteByDate(@RequestBody LocalDate date) {
        log.info("Delete all votes by date {}", date);
        voteService.deleteByDate(date);
    }
}
