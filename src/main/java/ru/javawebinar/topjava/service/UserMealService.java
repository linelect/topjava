package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import java.util.List;

/**
 * GKislin
 * 15.06.2015.
 */
public interface UserMealService {
    UserMeal save(UserMeal user);

    void delete(int id) throws NotFoundException;

    UserMeal get(int id) throws NotFoundException;

    List<UserMeal> getAllByUserId(int userId) throws NotFoundException;

    List<UserMeal> getAll();

    void update(UserMeal userMeal);
}
