package ru.topjava.lunchvote.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.lunchvote.model.Restaurant;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @EntityGraph(attributePaths = {"menu"})
    @Query("SELECT DISTINCT r FROM Restaurant r LEFT OUTER JOIN r.menu m ON m.date=:date")
    List<Restaurant> findAllWithMenu(@Param("date") LocalDate date);

    @EntityGraph(attributePaths = {"menu"})
    @Query("SELECT DISTINCT r FROM Restaurant r LEFT OUTER JOIN r.menu m ON m.date=:date WHERE r.id=:id")
    Optional<Restaurant> findWithMenu(@Param("id") long id, @Param("date") LocalDate date);
}
