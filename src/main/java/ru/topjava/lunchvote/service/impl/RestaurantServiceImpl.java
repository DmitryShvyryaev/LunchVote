package ru.topjava.lunchvote.service.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.topjava.lunchvote.exception.NotFoundException;
import ru.topjava.lunchvote.model.Dish;
import ru.topjava.lunchvote.model.Restaurant;
import ru.topjava.lunchvote.repository.DishRepository;
import ru.topjava.lunchvote.repository.RestaurantRepository;
import ru.topjava.lunchvote.repository.VoteRepository;
import ru.topjava.lunchvote.service.RestaurantService;
import ru.topjava.lunchvote.to.RestaurantWithMenu;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.topjava.lunchvote.util.ValidationUtil.checkNew;
import static ru.topjava.lunchvote.util.ValidationUtil.checkNotFoundWithId;
import static ru.topjava.lunchvote.util.RestaurantUtil.*;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final VoteRepository voteRepository;
    private final DishRepository dishRepository;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, VoteRepository voteRepository, DishRepository dishRepository) {
        this.restaurantRepository = restaurantRepository;
        this.voteRepository = voteRepository;
        this.dishRepository = dishRepository;
    }

//    @Cacheable(value = "restaurants")
    @Override
    public List<Restaurant> getAll() {
        return restaurantRepository.findAll();
    }

    @Override
    public Restaurant get(long id) {
        return restaurantRepository.findById(id).orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + id));
    }

//    @CacheEvict(value = "restaurants")
    @Transactional
    @Override
    public Restaurant create(Restaurant restaurant) {
        Assert.notNull(restaurant, "Restaurant must not bu null");
        checkNew(restaurant);
        return restaurantRepository.save(restaurant);
    }

//    @CacheEvict(value = "restaurants")
    @Transactional
    @Override
    public Restaurant update(Restaurant restaurant) {
        Assert.notNull(restaurant, "Restaurant must not bu null");
        return checkNotFoundWithId(restaurantRepository.save(restaurant), restaurant.id());
    }

//    @CacheEvict(value = "restaurants")
    @Transactional
    @Override
    public void delete(long id) {
        restaurantRepository.deleteById(id);
    }

    @Override
    public List<RestaurantWithMenu> getAllWithMenu(LocalDate date) {
        List<Restaurant> restaurants = getAll();
        List<Dish> menu = dishRepository.findAllByDate(date);
        Map<Restaurant, List<Dish>> map = menu.stream().collect(Collectors.groupingBy(Dish::getRestaurant));
        return restaurants.stream().
                map(rest -> createRestWithMenu(rest, map.get(rest))).
                collect(Collectors.toList());
    }

    @Override
    public RestaurantWithMenu getWithMenu(long id, LocalDate date) {
        Restaurant restaurant = get(id);
        return createRestWithMenu(restaurant, dishRepository.findAllByDateAndRestaurant(date, restaurant));
    }
}
