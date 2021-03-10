package ru.topjava.lunchvote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.lunchvote.model.Dish;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface DishRepository extends JpaRepository<Dish, Long> {

    List<Dish> findAllByDateAndRestaurantId(LocalDate date, long restaurantId);
}
