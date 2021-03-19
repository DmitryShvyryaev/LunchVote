package ru.topjava.lunchvote.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.topjava.lunchvote.exception.NotFoundException;
import ru.topjava.lunchvote.model.Restaurant;
import ru.topjava.lunchvote.repository.RestaurantRepository;
import ru.topjava.lunchvote.repository.UserRepository;
import ru.topjava.lunchvote.repository.VoteRepository;
import ru.topjava.lunchvote.service.RestaurantService;
import ru.topjava.lunchvote.to.RestaurantWithRating;

import java.util.List;

import static ru.topjava.lunchvote.util.ValidationUtil.checkNew;
import static ru.topjava.lunchvote.util.ValidationUtil.checkNotFoundWithId;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, VoteRepository voteRepository, UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Restaurant> getAll() {
        return restaurantRepository.findAll();
    }

//    @Override
//    public List<RestaurantWithRating> getAllWithRating(LocalDate date) {
//        List<Restaurant> restaurants = getAll();
//        List<Vote> votes = voteRepository.findAllByDate(date);
//        Map<Restaurant, Long> voteCount =  votes.stream().collect(Collectors.groupingBy(Vote::getRestaurant, Collectors.counting()));
//        return restaurants.stream().
//                map(restaurant -> createTo(restaurant, Math.toIntExact(voteCount.get(restaurant) == null ? 0 : voteCount.get(restaurant)))).
//                collect(Collectors.toList());
//    }
//
//    @Override
//    public RestaurantWithRating getSimpleWithRating(LocalDate date, long restaurantId) {
//        Restaurant restaurant = get(restaurantId);
//        return createTo(restaurant, voteRepository.countByDateAndRestaurantId(date, restaurantId));
//    }

    @Override
    public Restaurant get(long id) {
        return restaurantRepository.findById(id).orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + id));
    }

    @Transactional
    @Override
    public Restaurant create(Restaurant restaurant) {
        Assert.notNull(restaurant, "Restaurant must not bu null");
        checkNew(restaurant);
        return restaurantRepository.save(restaurant);
    }

    @Transactional
    @Override
    public Restaurant update(Restaurant restaurant) {
        Assert.notNull(restaurant, "Restaurant must not bu null");
        return checkNotFoundWithId(restaurantRepository.save(restaurant), restaurant.id());
    }

    @Transactional
    @Override
    public void delete(long id) {
        restaurantRepository.deleteById(id);
    }

    private RestaurantWithRating createTo(Restaurant restaurant, int voteCount) {
        return new RestaurantWithRating(restaurant.getId(), restaurant.getName(), restaurant.getDescription(), voteCount);
    }
}
