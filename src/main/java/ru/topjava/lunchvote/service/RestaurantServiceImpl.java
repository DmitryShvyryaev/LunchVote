package ru.topjava.lunchvote.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.topjava.lunchvote.exception.NotFoundException;
import ru.topjava.lunchvote.model.Restaurant;
import ru.topjava.lunchvote.model.Vote;
import ru.topjava.lunchvote.repository.RestaurantRepository;
import ru.topjava.lunchvote.repository.UserRepository;
import ru.topjava.lunchvote.repository.VoteRepository;
import ru.topjava.lunchvote.to.RestaurantTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.topjava.lunchvote.util.ValidationUtil.checkNew;
import static ru.topjava.lunchvote.util.ValidationUtil.checkNotFoundWithId;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    private static final LocalTime limit = LocalTime.of(11, 0, 0);

    private final RestaurantRepository restaurantRepository;
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;

    @Autowired
    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, VoteRepository voteRepository, UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
    }

    @Cacheable("restaurants")
    @Override
    public List<Restaurant> getAll() {
        return restaurantRepository.findAll();
    }

    @Override
    public List<RestaurantTo> getAllWithRating(LocalDate date) {
        List<Restaurant> restaurants = getAll();
        List<Vote> votes = voteRepository.findAllByDate(date);
        Map<Restaurant, Long> voteCount =  votes.stream().collect(Collectors.groupingBy(Vote::getRestaurant, Collectors.counting()));
        return restaurants.stream().
                map(restaurant -> createTo(restaurant, Math.toIntExact(voteCount.get(restaurant) == null ? 0 : voteCount.get(restaurant)))).
                collect(Collectors.toList());
    }

    @Override
    public RestaurantTo getSimpleWithRating(LocalDate date, long restaurantId) {
        Restaurant restaurant = get(restaurantId);
        return createTo(restaurant, voteRepository.countByDateAndRestaurantId(date, restaurantId));
    }

    @Override
    public Restaurant get(long id) {
        return restaurantRepository.findById(id).orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + id));
    }

    @Transactional
    @CacheEvict("restaurants")
    @Override
    public Restaurant create(Restaurant restaurant) {
        Assert.notNull(restaurant, "Restaurant must not bu null");
        checkNew(restaurant);
        return restaurantRepository.save(restaurant);
    }

    @Transactional
    @CacheEvict("restaurants")
    @Override
    public Restaurant update(Restaurant restaurant) {
        Assert.notNull(restaurant, "Restaurant must not bu null");
        return checkNotFoundWithId(restaurantRepository.save(restaurant), restaurant.id());
    }

    @Transactional
    @CacheEvict("restaurants")
    @Override
    public void delete(long id) {
        restaurantRepository.deleteById(id);
    }

    @Transactional
    @Override
    public boolean vote(long userId, long restaurantId, LocalDateTime now) {
        Vote vote = voteRepository.findByDateAndUserId(now.toLocalDate(), userId).orElse(null);
        if (vote == null) {
            voteRepository.save(new Vote(now.toLocalDate(), restaurantRepository.getOne(restaurantId), userRepository.getOne(userId)));
        } else if (now.toLocalTime().isAfter(limit)) {
            return false;
        } else {
            vote.setRestaurant(restaurantRepository.getOne(restaurantId));
            voteRepository.save(vote);
        }
        return true;
    }

    private RestaurantTo createTo(Restaurant restaurant, int voteCount) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName(), restaurant.getDescription(), voteCount);
    }
}
