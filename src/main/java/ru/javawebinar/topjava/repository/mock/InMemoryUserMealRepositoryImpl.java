package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.repository.UserMealRepository;
import ru.javawebinar.topjava.util.UserMealsUtil;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * GKislin
 * 15.09.2015.
 */
@Repository
public class InMemoryUserMealRepositoryImpl implements UserMealRepository {
    private Map<Integer, UserMeal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        UserMealsUtil.MEAL_LIST.forEach(this::save);
    }

    @Override
    public UserMeal save(UserMeal userMeal) {
        if (userMeal.isNew()) {
            userMeal.setId(counter.incrementAndGet());
        }
        return repository.put(userMeal.getId(), userMeal);
    }

    @Override
    public void delete(int id) {
        repository.remove(id);
    }

    @Override
    public UserMeal get(int id) {
        return repository.get(id);
    }

    @Override
    public List<UserMeal> getAllByUserId(int id) {
        return getAll().stream()
                .filter(um -> um.getUserId() == id)
                .sorted((UserMeal um1, UserMeal um2) -> {
                    if (um1.getDateTime().isAfter(um2.getDateTime())) {
                        return -1;
                    } else if (um1.getDateTime().isBefore(um2.getDateTime())) {
                        return 1;
                    }
                    return 0;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<UserMeal> getAll() {
        return repository.values().stream().sorted((UserMeal um1, UserMeal um2) -> {
            if (um1.getDateTime().isAfter(um2.getDateTime())) {
                return -1;
            } else if (um1.getDateTime().isBefore(um2.getDateTime())) {
                return 1;
            }
            return 0;
        }).collect(Collectors.toList());
    }
}

