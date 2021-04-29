package ru.topjava.lunchvote.service.impl;

import org.hibernate.mapping.Collection;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.lunchvote.exception.NotFoundException;
import ru.topjava.lunchvote.exception.RepeatVoteException;
import ru.topjava.lunchvote.model.Vote;
import ru.topjava.lunchvote.repository.RestaurantRepository;
import ru.topjava.lunchvote.repository.UserRepository;
import ru.topjava.lunchvote.repository.VoteRepository;
import ru.topjava.lunchvote.service.VoteService;
import ru.topjava.lunchvote.to.VoteTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.topjava.lunchvote.util.ValidationUtil.*;

@Service
public class VoteServiceImpl implements VoteService {
    private static final LocalTime limit = LocalTime.of(11, 0, 0);

    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public VoteServiceImpl(VoteRepository voteRepository, RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public VoteTo create(VoteTo voteTo, long userId) {
        Vote existingVote = voteRepository.findByDateAndUserId(voteTo.getDateTime().toLocalDate(), userId).orElse(null);
        if (existingVote != null && voteTo.getDateTime().toLocalTime().isAfter(limit)) {
            throw new RepeatVoteException("User with id " + userId + " has already vote on this date " + voteTo.getDateTime().toLocalDate());
        }
        Vote createdVote =  voteRepository.save(getFromTo(voteTo));
        return getToFromVote(createdVote);
    }

    @Override
    @Transactional
    public void delete(long id, long userId) {
        checkNotFoundWithId(voteRepository.delete(id, userId) != 0, id);
    }

    @Override
    public VoteTo get(LocalDate date, long userId) {
        Vote vote = voteRepository.findByDateAndUserId(date, userId).orElseThrow(() -> new NotFoundException("User has not vote yet."));
        return getToFromVote(vote);
    }

    @Override
    public List<VoteTo> getAllByUser(long userId) {
        return getTosFromVotes(voteRepository.findAllByUserId(userId));
    }

    @Override
    public List<VoteTo> getAllByDate(LocalDate date) {
        return getTosFromVotes(voteRepository.findAllByDate(date));
    }

    @Override
    @Transactional
    public void clear(LocalDate startDate, LocalDate endDate) {
        voteRepository.clear(startDate, endDate);
    }

    private Vote getFromTo(VoteTo voteTo) {
        Vote vote = new Vote();
        vote.setDate(voteTo.getDateTime().toLocalDate());
        vote.setRestaurant(restaurantRepository.getOne(voteTo.getRestaurantId()));
        vote.setUser(userRepository.getOne(voteTo.getUserId()));
        return vote;
    }

    private VoteTo getToFromVote(Vote vote) {
        VoteTo voteTo = new VoteTo();
        voteTo.setId(vote.id());
        voteTo.setDateTime(vote.getDate().atStartOfDay());
        voteTo.setRestaurantId(vote.getRestaurant().id());
        voteTo.setUserId(vote.getUser().id());
        return voteTo;
    }

    private List<VoteTo> getTosFromVotes(List<Vote> votes) {
        return votes.stream().map(this::getToFromVote).collect(Collectors.toList());
    }
}
