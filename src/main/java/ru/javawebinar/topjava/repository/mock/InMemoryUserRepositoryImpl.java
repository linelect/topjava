package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.LoggerWrapper;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.UserMealsUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * GKislin
 * 15.06.2015.
 */
@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {
    private static final LoggerWrapper LOG = LoggerWrapper.get(InMemoryUserRepositoryImpl.class);
    private AtomicInteger counter = new AtomicInteger(0);
    private Map<Integer, User> repository = new ConcurrentHashMap<>();

    {
        UserMealsUtil.USER_LIST.forEach(this::save);
    }

    @Override
    public boolean delete(int id) {
        LOG.info("delete " + id);
        return repository.remove(id, get(id));
    }

    @Override
    public User save(User user) {
        LOG.info("save " + user);
        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
        }
        repository.put(user.getId(), user);
        return user;
    }

    @Override
    public User get(int id) {
        LOG.info("get " + id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        LOG.info("getAll");
        List<User> resultList = new ArrayList<>(repository.values());
        resultList.sort((User u1, User u2) -> u1.getName().compareTo(u2.getName()));
        return resultList;
    }

    @Override
    public User getByEmail(String email) {
        LOG.info("getByEmail " + email);
        for (User user : getAll()) {
            if (user.getEmail().equals(email)) {
                return user;
            }

        }
        return null;
    }
}
