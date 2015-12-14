package ru.javawebinar.topjava;

import ru.javawebinar.topjava.util.UserMealsUtil;

/**
 * GKislin
 * 06.03.2015.
 */
public class LoggedUser {

    private static int userId;

    public static int id() {
        return getUserId();
    }

    public static int getUserId() {
        return userId;
    }

    public static void setUserId(int userId) {
        LoggedUser.userId = userId;
    }

    public static int getCaloriesPerDay() {
        return UserMealsUtil.DEFAULT_CALORIES_PER_DAY;
    }
}
