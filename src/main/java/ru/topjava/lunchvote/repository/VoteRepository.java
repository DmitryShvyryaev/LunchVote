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
    @Query("DELETE FROM Vote v WHERE v.id=:id AND v.user.id=:userId")
    int delete(@Param("id") long id, @Param("userId") long userId);

    @Transactional
    @Query("DELETE FROM Vote v WHERE v.date>=:startDate AND v.date<:endDate")
    int clear(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    List<Vote> findAllByUserId(Long userId);

    List<Vote> findAllByDate(@Param("date") LocalDate date);

    Optional<Vote> findByDateAndUserId(LocalDate date, Long userId);

}
