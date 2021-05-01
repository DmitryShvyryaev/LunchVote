package ru.topjava.lunchvote.service.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.topjava.lunchvote.exception.NotFoundException;
import ru.topjava.lunchvote.model.Restaurant;
import ru.topjava.lunchvote.model.User;
import ru.topjava.lunchvote.repository.RestaurantRepository;
import ru.topjava.lunchvote.service.RestaurantService;

import java.time.LocalDate;
import java.util.List;

import static ru.topjava.lunchvote.util.ValidationUtil.*;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public List<Restaurant> getAll() {
        return restaurantRepository.findAll();
    }

    @Override
    public Restaurant get(long id) {
        return restaurantRepository.findById(id).orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + id));
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    @Transactional
    @Override
    public Restaurant create(Restaurant restaurant) {
        Assert.notNull(restaurant, "Restaurant must not bu null");
        checkNew(restaurant);
        return restaurantRepository.save(restaurant);
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    @Transactional
    @Override
    public Restaurant update(Restaurant restaurant) {
        Assert.notNull(restaurant, "Restaurant must not bu null");
        return checkNotFoundWithId(restaurantRepository.save(restaurant), restaurant.id(), Restaurant.class);
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    @Transactional
    @Override
    public void delete(long id) {
        checkNotFound(restaurantRepository.delete(id) != 0, "id = " + id, Restaurant.class);
    }

    @Cacheable(value = "restaurants")
    @Override
    public List<Restaurant> getAllWithMenu(LocalDate date) {
        return restaurantRepository.findAllWithMenu(date);
    }

    @Override
    public Restaurant getWithMenu(long id, LocalDate date) {
        return restaurantRepository.findWithMenu(id, date).orElseThrow(() -> new NotFoundException("Restaurant not found with id: " + id));
    }
}
