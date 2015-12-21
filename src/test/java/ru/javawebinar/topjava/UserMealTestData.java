package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.UserMeal;

import java.sql.Timestamp;

import static ru.javawebinar.topjava.model.BaseEntity.START_SEQ;

/**
 * GKislin
 * 24.09.2015.
 */
public class UserMealTestData {
    public static final int USER_MEAL_ID = START_SEQ+2;
    public static final int USER_MEAL_USERID = 100000;
    public static final UserMeal USER_MEAL = new UserMeal(USER_MEAL_ID, Timestamp.valueOf("2015-12-21 23:59:59.610000").toLocalDateTime(), "Завтрак", 500);

}
