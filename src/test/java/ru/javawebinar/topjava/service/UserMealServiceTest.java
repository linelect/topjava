package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.util.DbPopulator;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static ru.javawebinar.topjava.UserMealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN;


@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class UserMealServiceTest {

    @Autowired
    protected UserMealService service;

    @Autowired
    private DbPopulator dbPopulator;

    @Before
    public void setUp() throws Exception {
        dbPopulator.execute();
    }

    @Test
    public void testGet() throws Exception {
        UserMeal userMeal = service.get(USER_MEAL_ID, USER_MEAL_USERID);
        Assert.assertEquals(USER_MEAL.toString(), userMeal.toString());
    }

    @Test(expected = NotFoundException.class)
    public void testGetMealAnotherUser() throws Exception {
        UserMeal userMeal = service.get(USER_MEAL_ID, 1000001);
        Assert.assertEquals(USER_MEAL.toString(), userMeal.toString());
    }

    @Test
    public void testDelete() throws Exception {
        service.delete(USER_MEAL_ID, USER_MEAL_USERID);

    }

    @Test(expected = NotFoundException.class)
    public void testDeleteAnotherUserMeal() throws Exception {
        service.delete(100010, USER_MEAL_USERID);

    }

    @Test
    public void testGetBetweenDates() throws Exception {
        Collection<UserMeal> userMealList = service.getBetweenDates(USER_MEAL.getDateTime().toLocalDate(), USER_MEAL.getDateTime().toLocalDate(), USER_MEAL_USERID);
        Assert.assertEquals(USER_MEAL.toString(), userMealList.iterator().next().toString());
    }

    @Test
    public void testGetAll() throws Exception {
        Collection<UserMeal> userMealList = service.getAll(USER_MEAL_USERID);
        Assert.assertEquals(6, userMealList.size());
    }

    @Test
    public void testUpdate() throws Exception {
        UserMeal userMeal = new UserMeal(USER_MEAL_ID, Timestamp.valueOf("2015-12-21 23:59:59.610000").toLocalDateTime(), "Завтрак updated", 485);
        service.update(userMeal, USER_MEAL_USERID);
        Assert.assertEquals(userMeal.toString(), service.get(USER_MEAL_ID, USER_MEAL_USERID).toString());
    }

    @Test
    public void testSave() throws Exception {
        UserMeal userMeal = new UserMeal(Timestamp.valueOf("2015-12-31 23:59:59.610000").toLocalDateTime(), "НОВЫЙ ГОД!", 5000);
        UserMeal newUserMeal = service.save(userMeal, USER_MEAL_USERID);
        Assert.assertEquals(userMeal.toString(), newUserMeal.toString());
    }
}