package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.LoggedUser;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.repository.UserMealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

/**
 * GKislin
 * 06.03.2015.
 */
@Service
public class UserMealServiceImpl implements UserMealService {

    @Autowired
    private UserMealRepository repository;

    @Override
    public UserMeal save(UserMeal userMeal) {
        return repository.save(userMeal);
    }

    @Override
    public void delete(int id) throws NotFoundException {
        repository.delete(id);
    }

    @Override
    public UserMeal get(int id) throws NotFoundException {
        return repository.get(id);
    }

    @Override
    public List<UserMeal> getAllByUserId(int userId) throws NotFoundException {
        if (LoggedUser.id() != userId) throw new NotFoundException("You can not see meals another user!");
        return repository.getAllByUserId(userId);
    }


    @Override
    public List<UserMeal> getAll() {
        return repository.getAll();
    }

    @Override
    public void update(UserMeal userMeal) {
        if (LoggedUser.id() != userMeal.getId()) throw new NotFoundException("You can not modify meals another user!");
        repository.save(userMeal);
    }
}
