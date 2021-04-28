package ru.topjava.lunchvote.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public boolean vote(LocalDateTime dateTime, long userId, long restaurantId) {
        Vote vote = voteRepository.findByDateAndUserId(dateTime.toLocalDate(), userId).orElse(null);
        if (vote == null) {
            voteRepository.save(new Vote(dateTime.toLocalDate(), restaurantRepository.getOne(restaurantId), userRepository.getOne(userId)));
        } else if (dateTime.toLocalTime().isAfter(limit)) {
            return false;
        } else {
            vote.setRestaurant(restaurantRepository.getOne(restaurantId));
            voteRepository.save(vote);
        }
        return true;
    }

    public List<Vote> getAllByD123ate(LocalDate date) {
        return voteRepository.findAllByDate(date);
    }

    public Integer getCountByDate(LocalDate date, long restaurantId) {
        return voteRepository.countByDateAndRestaurantId(date, restaurantId);
    }


    @Override
    public VoteTo create(VoteTo voteTo, long userId) {
        Vote existingVote = voteRepository.findByDateAndUserId(voteTo.getDateTime().toLocalDate(), userId).orElse(null);
        if (existingVote != null && voteTo.getDateTime().toLocalTime().isAfter(limit)) {
            throw new RepeatVoteException("User with id " + userId + " has already vote on this date " + voteTo.getDateTime().toLocalDate());
        }
        Vote createdVote =  voteRepository.save(getFromTo(voteTo));
        return getToFromVote(createdVote);
    }

    @Override
    public boolean delete(long id) {
        return false;
    }

    @Override
    public VoteTo get(LocalDate date, long userId) {
        return null;
    }

    @Override
    public List<VoteTo> getAllByDate(LocalDate date) {
        return null;
    }

    @Override
    public void clear(LocalDate startDate, LocalDate endDate) {

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
}
