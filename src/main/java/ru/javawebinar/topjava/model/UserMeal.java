package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;

/**
 * GKislin
 * 11.01.2015.
 */
public class UserMeal {
    protected Integer id;

    protected final LocalDateTime dateTime;

    protected final String description;

    protected final int calories;

    protected final int userId;

    public UserMeal(LocalDateTime dateTime, String description, int calories, int userId) {
        this(null, dateTime, description, calories, userId);
    }

    public int getUserId() {
        return userId;
    }

    public UserMeal(Integer id, LocalDateTime dateTime, String description, int calories, int userId) {
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.userId = userId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public Integer getId() {
        return id;
    }

    public boolean isNew() {
        return id == null;
    }

    @Override
    public String toString() {
        return "UserMeal{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}
