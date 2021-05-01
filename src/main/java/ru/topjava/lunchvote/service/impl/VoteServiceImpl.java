package ru.topjava.lunchvote.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.lunchvote.exception.NotFoundException;
import ru.topjava.lunchvote.exception.RepeatVoteException;
import ru.topjava.lunchvote.model.Restaurant;
import ru.topjava.lunchvote.model.Vote;
import ru.topjava.lunchvote.repository.RestaurantRepository;
import ru.topjava.lunchvote.repository.UserRepository;
import ru.topjava.lunchvote.repository.VoteRepository;
import ru.topjava.lunchvote.service.VoteService;
import ru.topjava.lunchvote.to.VoteTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.topjava.lunchvote.util.ValidationUtil.checkNotFoundWithId;

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
    public VoteTo create(VoteTo voteTo) {
        int countOfVote = voteRepository.countByDateAndUserId(voteTo.getDateTime().toLocalDate(), voteTo.getUserId());
        if (countOfVote != 0 && voteTo.getDateTime().toLocalTime().isAfter(limit)) {
            throw new RepeatVoteException("User with id " + voteTo.getUserId() + " has already vote on this date " + voteTo.getDateTime().toLocalDate());
        } else if (countOfVote != 0)
            delete(voteTo.getDateTime().toLocalDate(), voteTo.getUserId());
        Vote createdVote = voteRepository.save(getFromTo(voteTo));
        return getToFromVote(createdVote);
    }

    @Override
    @Transactional
    public void delete(LocalDate date, long userId) {
        voteRepository.delete(date, userId);
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
    public List<VoteTo> getAll() {
        List<Vote> votes = voteRepository.findAll();
        return getTosFromVotes(votes);
    }

    @Override
    public List<VoteTo> getAllByDate(LocalDate date) {
        return getTosFromVotes(voteRepository.findAllByDate(date));
    }

    private Vote getFromTo(VoteTo voteTo) {
        Vote vote = new Vote();
        vote.setDate(voteTo.getDateTime().toLocalDate());
        vote.setTime(voteTo.getDateTime().toLocalTime());
        vote.setRestaurant(restaurantRepository.getOne(voteTo.getRestaurantId()));
        vote.setUser(userRepository.getOne(voteTo.getUserId()));
        return vote;
    }

    private VoteTo getToFromVote(Vote vote) {
        VoteTo voteTo = new VoteTo();
        voteTo.setId(vote.id());
        voteTo.setDateTime(vote.getDate().atTime(vote.getTime()));
        voteTo.setRestaurantId(vote.getRestaurant().id());
        voteTo.setUserId(vote.getUser().id());
        return voteTo;
    }

    private List<VoteTo> getTosFromVotes(List<Vote> votes) {
        return votes.stream().map(this::getToFromVote).collect(Collectors.toList());
    }
}
