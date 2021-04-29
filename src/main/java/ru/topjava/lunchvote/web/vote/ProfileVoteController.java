package ru.topjava.lunchvote.web.vote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.topjava.lunchvote.service.VoteService;
import ru.topjava.lunchvote.to.VoteTo;
import ru.topjava.lunchvote.web.security.AuthorizedUser;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = ProfileVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileVoteController {

    static final String REST_URL = "/rest/profile/vote";

    private final VoteService voteService;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public ProfileVoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @GetMapping
    public VoteTo get(@AuthenticationPrincipal AuthorizedUser authorizedUser) {
        LocalDate today = LocalDate.now();
        log.info("Get vote for date {} and user {}", today, authorizedUser.getUsername());
        return voteService.get(today, authorizedUser.getId());
    }

    @GetMapping("/all")
    public List<VoteTo> getAll(@AuthenticationPrincipal AuthorizedUser authorizedUser) {
        log.info("Get all votes for user {}", authorizedUser.getUsername());
        return voteService.getAllByUser(authorizedUser.getId());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<VoteTo> create(@RequestBody Long restaurantId,
                                         @AuthenticationPrincipal AuthorizedUser authorizedUser) {
        log.info("Vote for restaurant with id {} by user {}", restaurantId, authorizedUser.getUsername());
        VoteTo voteTo = new VoteTo(LocalDateTime.now(), restaurantId, authorizedUser.getId());
        VoteTo created = voteService.create(voteTo);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthorizedUser authorizedUser) {
        LocalDate today = LocalDate.now();
        log.info("Delete vote for a day {} by user {}", today, authorizedUser.getUsername());
        voteService.delete(today, authorizedUser.getId());
    }
}
