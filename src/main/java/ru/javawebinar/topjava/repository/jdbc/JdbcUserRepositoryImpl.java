package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * User: gkislin
 * Date: 26.08.2014
 */

@Repository
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepositoryImpl(DataSource dataSource) {
        this.insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public User save(User user) {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", user.getId())
                .addValue("name", user.getName())
                .addValue("email", user.getEmail())
                .addValue("password", user.getPassword())
                .addValue("registered", user.getRegistered())
                .addValue("enabled", user.isEnabled())
                .addValue("caloriesPerDay", user.getCaloriesPerDay());

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(map);
            user.setId(newKey.intValue());
        } else {
            namedParameterJdbcTemplate.update(
                    "UPDATE users SET name=:name, email=:email, password=:password, " +
                            "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", map);

            MapSqlParameterSource mapRole = new MapSqlParameterSource()
                    .addValue("user_id", user.getId());

            jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());
        }

        for (Role r : user.getRoles()) {
            MapSqlParameterSource mapRole = new MapSqlParameterSource()
                    .addValue("user_id", user.getId())
                    .addValue("role", r.name());
            namedParameterJdbcTemplate.update(
                    "INSERT INTO user_roles (role, user_id) VALUES (:role, :user_id)", mapRole);
        }

        return user;
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
    }

    @Override
    public List<User> getAll() {

        List<User> userList = jdbcTemplate.query("SELECT DISTINCT * FROM users u left outer join user_roles r on u.id=r.user_id ORDER BY name, email, role Desc", new ResultSetExtractor<List<User>>() {
            @Override
            public List<User> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                Map<Integer, User> usersMap = new HashMap<Integer, User>();
                while(resultSet.next()) {
                    Integer userId = resultSet.getInt("id");
                    User user = usersMap.get(userId);
                    if (user == null) {
                        user = ROW_MAPPER.mapRow(resultSet, resultSet.getRow());
                        usersMap.put(userId,user);
                    }

                    String roleName = resultSet.getString("role");
                    if (roleName != null) {
                        Role role = Role.valueOf(roleName);
                        if (user.getRoles() == null) {
                            user.setRoles(new HashSet<>());
                        }
                        user.addRole(role);
                    }
                }
                return new ArrayList<User>(usersMap.values());
            }
        });

        Collections.sort(userList, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                int nameVal = o1.getName().compareTo(o2.getName()) * 10;
                int emailval = o1.getEmail().compareTo(o2.getEmail());
                return nameVal + emailval;
            }
        });

        return userList;
    }
}
