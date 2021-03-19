package ru.topjava.lunchvote.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.topjava.lunchvote.model.Dish;
import ru.topjava.lunchvote.model.Restaurant;
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
    public List<Dish> getAll(LocalDate date, long restaurantId) {
        Restaurant owner = restaurantRepository.getOne(restaurantId);
        return repository.findAllByDateAndRestaurant(date, owner);
    }

    @Override
    public Dish get(long id) {
        return checkNotFoundWithId(repository.findById(id).orElse(null), id);
    }

    @Transactional
    @Override
    public Dish create(Dish dish, long restaurantId) {
        Assert.notNull(dish, "Dish must not be null.");
        dish.setRestaurant(restaurantRepository.getOne(restaurantId));
        return repository.save(dish);
    }

    @Transactional
    @Override
    public Dish update(Dish dish) {
        Assert.notNull(dish, "Dish must not be null.");
        return checkNotFoundWithId(repository.save(dish), dish.id());
    }

    @Transactional
    @Override
    public void delete(long id) {
        repository.deleteById(id);
    }
}
