package ru.topjava.lunchvote.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.lunchvote.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Long> {

    Integer countByDateAndRestaurantId(LocalDate date, Long restaurantId);

    @EntityGraph(attributePaths = {"restaurant"})
    List<Vote> findAllByDate(LocalDate date);

    Optional<Vote> findByDateAndUserId(LocalDate date, Long userId);

}
