package ru.topjava.lunchvote.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.lunchvote.model.Vote;
import ru.topjava.lunchvote.repository.RestaurantRepository;
import ru.topjava.lunchvote.repository.UserRepository;
import ru.topjava.lunchvote.repository.VoteRepository;
import ru.topjava.lunchvote.service.VoteService;

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
    @Override
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

    @Override
    public List<Vote> getAllByDate(LocalDate date) {
        return voteRepository.findAllByDate(date);
    }

    @Override
    public Integer getCountByDate(LocalDate date, long restaurantId) {
        return voteRepository.countByDateAndRestaurantId(date, restaurantId);
    }
}
