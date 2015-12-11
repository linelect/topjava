package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;
import ru.javawebinar.topjava.util.UserMealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class MealsStorage {
    private static MealsStorage ourInstance = new MealsStorage();
    private HashMap<Integer, UserMeal> mealsMap = new HashMap<>();
    private int index = 1;

    public static MealsStorage getInstance() {
        return ourInstance;
    }

    private MealsStorage() {
        add(new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500));
        add(new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000));
        add(new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
        add(new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000));
        add(new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500));
        add(new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510));
        add(new UserMeal(LocalDateTime.of(2015, Month.MAY, 29, 10, 0), "Завтрак", 500));
        add(new UserMeal(LocalDateTime.of(2015, Month.MAY, 29, 13, 0), "Обед", 1000));
        add(new UserMeal(LocalDateTime.of(2015, Month.MAY, 29, 20, 0), "Ужин", 500));
        add(new UserMeal(LocalDateTime.of(2015, Month.MAY, 28, 10, 0), "Завтрак", 1000));
        add(new UserMeal(LocalDateTime.of(2015, Month.MAY, 28, 13, 0), "Обед", 500));
        add(new UserMeal(LocalDateTime.of(2015, Month.MAY, 28, 20, 0), "Ужин", 510));
    }

    public void add(UserMeal userMeals) {
        setById(index, userMeals);
        index++;
    }

    private void setById(int id, UserMeal userMeals) {
        userMeals.setId(id);
        mealsMap.put(id, userMeals);
    }

    public void edit(int id, UserMeal userMeals) {
        setById(id, userMeals);
    }

    public void addAll(List<UserMeal> userMealList) {
        for (UserMeal um : userMealList) add(um);
    }

    public UserMeal getById(int id) {
        return mealsMap.get(id);
    }

    public void remove(int id) {
        mealsMap.remove(id);
    }

    public List<UserMeal> getAll() {
        return new ArrayList<UserMeal>(mealsMap.values());
    }

    public List<UserMealWithExceed> listWithExceed() {
        List<UserMeal> mealList = getAll();
        Map<LocalDate, Integer> caloriesSumByDate = mealList.stream().collect(Collectors.groupingBy(um -> um.getDateTime().toLocalDate(),
                Collectors.summingInt(UserMeal::getCalories)));

        return mealList.stream()
                .map(um -> {
                    UserMealWithExceed ume = new UserMealWithExceed(um.getDateTime(), um.getDescription(), um.getCalories(),
                        caloriesSumByDate.get(um.getDateTime().toLocalDate()) > UserMealsUtil.caloriesPerDay);
                    ume.setId(um.getId());
                    return ume;}
                )
                .collect(Collectors.toList());
    }

}
