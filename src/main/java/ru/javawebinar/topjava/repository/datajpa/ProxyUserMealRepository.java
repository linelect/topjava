package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.model.UserMeal;

import java.time.LocalDateTime;
import java.util.List;

/**
 * GKislin
 * 27.03.2015.
 */
@Transactional(readOnly = true)
public interface ProxyUserMealRepository extends JpaRepository<UserMeal, Integer> {

    @Transactional
    @Modifying
    @Query(name = UserMeal.DELETE)
    int delete(@Param("id") int id, @Param("userId") int userId);

    @Modifying
    @Transactional
    @Query("UPDATE UserMeal set dateTime=:dateTime, calories=:calories, description=:description WHERE id=:id and user.id=:userId")
    int update(@Param("id") int id,
                    @Param("userId") int userId,
                    @Param("dateTime") LocalDateTime dateTime,
                    @Param("calories") int calories,
                    @Param("description") String description);

    @Transactional
    @Override
    UserMeal save(UserMeal userMeal);

    @Query(name = UserMeal.GET)
    UserMeal findOne(@Param("id") int id, @Param("userId") int userId);

    @Query(name = UserMeal.ALL_SORTED)
    List<UserMeal> findAll(@Param("userId") int userId);

    @Query(name = UserMeal.GET_BETWEEN)
    List<UserMeal> getBetween(@Param("userId") int userId, @Param("startDate")LocalDateTime startDate, @Param("endDate")LocalDateTime endDate);

}
