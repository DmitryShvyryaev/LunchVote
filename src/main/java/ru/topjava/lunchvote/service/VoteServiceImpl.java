package ru.topjava.lunchvote.service;

import ru.topjava.lunchvote.model.Vote;
import ru.topjava.lunchvote.repository.RestaurantRepository;
import ru.topjava.lunchvote.repository.UserRepository;
import ru.topjava.lunchvote.repository.VoteRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class VoteServiceImpl implements VoteService {
    private static final LocalTime limit = LocalTime.of(11, 0, 0);

    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    public VoteServiceImpl(VoteRepository voteRepository, UserRepository userRepository, RestaurantRepository restaurantRepository) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public boolean vote(LocalDateTime date, long userId, long restaurantId) {
        Vote vote = voteRepository.findByDateAndUserId(date.toLocalDate(), userId).orElse(null);
        if (vote == null) {
            voteRepository.save(new Vote(date.toLocalDate(), restaurantRepository.getOne(restaurantId), userRepository.getOne(userId)));
            return true;
        } else if (date.toLocalTime().isAfter(limit)) {
            return false;
        } else {
            vote.setRestaurant(restaurantRepository.getOne(restaurantId));
            voteRepository.save(vote);
            return true;
        }
    }

    @Override
    public List<Vote> getAllByDate(LocalDate date) {
        return voteRepository.findAllByDate(date);
    }

    @Override
    public List<Vote> getAllByDateAndRestaurant(LocalDate date, long restaurantId) {
        return voteRepository.findAllByDateAndRestaurantId(date, restaurantId);
    }

    @Override
    public void clear(LocalDate date) {
        voteRepository.deleteAllByDate(date);
    }
}
