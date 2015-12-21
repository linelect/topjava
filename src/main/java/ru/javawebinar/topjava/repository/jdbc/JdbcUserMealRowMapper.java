package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.jdbc.core.RowMapper;
import ru.javawebinar.topjava.model.UserMeal;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcUserMealRowMapper implements RowMapper<UserMeal> {
    @Override
    public UserMeal mapRow(ResultSet resultSet, int i) throws SQLException {
        return new UserMeal(resultSet.getInt("id"),
                resultSet.getTimestamp("dateTime").toLocalDateTime(),
                resultSet.getString("description"),
                resultSet.getInt("calories"));
    }
}
