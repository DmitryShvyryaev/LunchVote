package ru.topjava.lunchvote.service;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.topjava.lunchvote.exception.NotFoundException;
import ru.topjava.lunchvote.model.Restaurant;
import ru.topjava.lunchvote.repository.RestaurantRepository;
import ru.topjava.lunchvote.repository.VoteRepository;
import ru.topjava.lunchvote.to.RestaurantTo;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static ru.topjava.lunchvote.util.ValidationUtil.checkNew;
import static ru.topjava.lunchvote.util.ValidationUtil.checkNotFoundWithId;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final VoteRepository voteRepository;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, VoteRepository voteRepository) {
        this.restaurantRepository = restaurantRepository;
        this.voteRepository = voteRepository;
    }

    @Override
    public List<Restaurant> getAll() {
        return restaurantRepository.findAll();
    }

    @Override
    public List<RestaurantTo> getAllWithRating(LocalDate date) {
        return getAll().stream()
                .map(rest -> createTo(rest, voteRepository.countByDateAndRestaurantId(date, rest.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public Restaurant get(long id) {
        return restaurantRepository.findById(id).orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + id));
    }

    @Override
    public Restaurant create(Restaurant restaurant) {
        Assert.notNull(restaurant, "Restaurant must not bu null");
        checkNew(restaurant);
        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant update(Restaurant restaurant) {
        Assert.notNull(restaurant, "Restaurant must not bu null");
        return checkNotFoundWithId(restaurantRepository.save(restaurant), restaurant.getId());
    }

    @Override
    public void delete(long id) {
        restaurantRepository.deleteById(id);
    }

//    @Override
//    public boolean vote(long userId, long restaurantId, LocalDateTime dateTime) {
//        LocalDate today = dateTime.toLocalDate();
//        Vote vote = voteRepository.findByDateAndUserId(today, userId).orElse(null);
//        if (vote == null) {
//            voteRepository.save(new Vote(today, restaurantId, userId));
//            return true;
//        } else if (dateTime.toLocalTime().isAfter(limit)) {
//            return false;
//        } else {
//            vote.setRestaurantId(restaurantId);
//            voteRepository.save(vote);
//            return true;
//        }
//    }

    private RestaurantTo createTo(Restaurant restaurant, int voteCount) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName(), restaurant.getDescription(), voteCount);
    }
}
