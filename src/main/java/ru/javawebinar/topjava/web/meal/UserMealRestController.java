package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.LoggerWrapper;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.service.UserMealServiceImpl;

import java.util.List;

/**
 * GKislin
 * 06.03.2015.
 */
@Controller
public class UserMealRestController {
    protected final LoggerWrapper LOG = LoggerWrapper.get(getClass());

    @Autowired
    private UserMealServiceImpl service;

    public List<UserMeal> getAll() {
        LOG.info("getAll");
        return service.getAll();
    }

    public UserMeal get(int id) {
        LOG.info("get " + id);
        return service.get(id);
    }

    public UserMeal create(UserMeal userMeal) {
        userMeal.setId(null);
        LOG.info("create " + userMeal);
        return service.save(userMeal);
    }

    public void delete(int id) {
        LOG.info("delete UserMeal" + id);
        service.delete(id);
    }

    public void update(UserMeal userMeal, int id) {
        userMeal.setId(id);
        LOG.info("update UserMeal" + userMeal);
        service.update(userMeal);
    }

    public List<UserMeal> getAllByUserId(int userId) {
        LOG.info("getAllByUserId " + userId);
        return service.getAllByUserId(userId);
    }

    public UserMeal save(UserMeal userMeal) {
        return service.save(userMeal);
    }

}
