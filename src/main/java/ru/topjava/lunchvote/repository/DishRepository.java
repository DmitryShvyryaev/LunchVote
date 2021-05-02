package ru.topjava.lunchvote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.lunchvote.model.Dish;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends JpaRepository<Dish, Long> {

    List<Dish> findAllByDateAndRestaurantId(LocalDate date, Long restaurantId);

    Optional<Dish> findByIdAndRestaurantId(long id, Long restaurantId);

    @Modifying
    @Query("DELETE FROM Dish d WHERE d.id=:id AND d.restaurant.id=:restaurantId")
    int delete(@Param("id") long id, @Param("restaurantId") long restaurantId);
}
