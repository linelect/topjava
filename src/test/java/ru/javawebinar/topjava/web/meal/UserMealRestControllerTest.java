package ru.javawebinar.topjava.web.meal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.TestUtil;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.service.UserMealService;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.util.UserMealsUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static ru.javawebinar.topjava.MealTestData.USER_MEALS;
import static ru.javawebinar.topjava.MealTestData.MEAL1;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.MealTestData.MATCHER;
import static ru.javawebinar.topjava.UserTestData.*;


public class UserMealRestControllerTest extends AbstractControllerTest {

    public static final String REST_URL = UserMealRestController.REST_URL + '/';
//    public static final String REST_URL = "/rest/meal/";

    @Autowired
    protected UserMealService userMealService;

    @Test
    public void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + MEAL1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentMatcher(MEAL1));
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + MEAL1_ID).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        ArrayList<UserMeal> mealsAfterDelete = new ArrayList<>(USER_MEALS);
        mealsAfterDelete.remove(MEAL1);
        MATCHER.assertCollectionEquals(mealsAfterDelete, userMealService.getAll(USER_ID));
    }

    @Test
    public void testUpdate() throws Exception {
        UserMeal updated = MealTestData.getUpdated();
        updated.setDescription("UpdatedName");
        updated.setUser(MEAL1.getUser());
        mockMvc.perform(put(REST_URL + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isOk());

        MealTestData.MATCHER.assertEquals(updated, userMealService.get(MEAL1_ID, USER_ID));
    }

    @Test
    public void testCreate() throws Exception {
        UserMeal expected = MealTestData.getCreated();
        ResultActions action = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andExpect(status().isCreated());

        UserMeal returned = MealTestData.MATCHER.fromJsonAction(action);
        expected.setId(returned.getId());

        MealTestData.MATCHER.assertEquals(expected, returned);
        ArrayList<UserMeal> userMeals = new ArrayList<>();
        userMeals.add(expected);
        userMeals.addAll(USER_MEALS);
        MealTestData.MATCHER.assertCollectionEquals(userMeals, userMealService.getAll(USER_ID));
    }

    @Test
    public void testGetAll() throws Exception {
        TestUtil.print(mockMvc.perform(get(REST_URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MealTestData.MATCHER_WITH_EXCEED.contentListMatcher(UserMealsUtil.getWithExceeded(USER_MEALS, UserMealsUtil.DEFAULT_CALORIES_PER_DAY))));
    }

    @Test
    public void testGetBetween() throws Exception {
        String startDate = MEAL1.getDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String endDate = MEAL1.getDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        TestUtil.print(mockMvc.perform(get(REST_URL + "getBetween/"+startDate+"/"+endDate).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MealTestData.MATCHER_WITH_EXCEED.contentListMatcher(UserMealsUtil.getWithExceeded(Collections.singletonList(MEAL1), UserMealsUtil.DEFAULT_CALORIES_PER_DAY))));
    }
}