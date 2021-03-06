package ru.topjava.lunchvote.service.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.topjava.lunchvote.model.Dish;
import ru.topjava.lunchvote.repository.DishRepository;
import ru.topjava.lunchvote.repository.RestaurantRepository;
import ru.topjava.lunchvote.service.DishService;

import java.time.LocalDate;
import java.util.List;

import static ru.topjava.lunchvote.util.ValidationUtil.checkNotFoundWithId;

@Service
public class DishServiceImpl implements DishService {

    private final DishRepository repository;
    private final RestaurantRepository restaurantRepository;

    public DishServiceImpl(DishRepository repository, RestaurantRepository restaurantRepository) {
        this.repository = repository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    @Cacheable(value = "menu")
    public List<Dish> getAll(LocalDate date, long restaurantId) {
        return repository.findAllByDateAndRestaurantId(date, restaurantId);
    }

    @Override
    public Dish get(long id, long restaurantId) {
        return checkNotFoundWithId(repository.findByIdAndRestaurantId(id, restaurantId).orElse(null), id, Dish.class);
    }

    @Transactional
    @CacheEvict(value = {"restaurants", "menu"}, allEntries = true)
    @Override
    public Dish create(long restaurantId, Dish dish) {
        Assert.notNull(dish, "Dish must not be null.");
        dish.setRestaurant(restaurantRepository.getOne(restaurantId));
        return repository.save(dish);
    }

    @Transactional
    @CacheEvict(value = {"restaurants", "menu"}, allEntries = true)
    @Override
    public Dish update(long restaurantId, Dish dish) {
        Assert.notNull(dish, "Dish must not be null.");
        get(dish.id(), restaurantId);
        dish.setRestaurant(restaurantRepository.getOne(restaurantId));
        return repository.save(dish);
    }

    @Transactional
    @CacheEvict(value = {"restaurants", "menu"}, allEntries = true)
    @Override
    public void delete(long restaurantId, long id) {
        checkNotFoundWithId(repository.delete(id, restaurantId) != 0, id, Dish.class);
    }
}
