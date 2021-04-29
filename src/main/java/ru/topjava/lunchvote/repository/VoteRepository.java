package ru.topjava.lunchvote.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.topjava.lunchvote.model.User;
import ru.topjava.lunchvote.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Vote v WHERE v.date=:date AND v.user.id=:userId")
    int delete(@Param("date") LocalDate date, @Param("userId") long userId);

    @Transactional
    void deleteAllByDate(LocalDate date);

    @EntityGraph(attributePaths = {"restaurant", "user"})
    List<Vote> findAllByUserId(Long userId);

    @Modifying
    @EntityGraph(attributePaths = {"restaurant", "user"})
    List<Vote> findAll();

    @EntityGraph(attributePaths = {"restaurant", "user"})
    List<Vote> findAllByDate(@Param("date") LocalDate date);

    @EntityGraph(attributePaths = {"restaurant", "user"})
    Optional<Vote> findByDateAndUserId(LocalDate date, Long userId);

    int countByDateAndUserId(LocalDate date, Long userId);
}
