package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.LoggerWrapper;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;
import ru.javawebinar.topjava.repository.MealsStorage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class MealServlet extends HttpServlet {
    private static final LoggerWrapper LOG = LoggerWrapper.get(UserServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String dateTimeParamValue = req.getParameter("dateTime");
        String descriptionParamValue = req.getParameter("description");
        String caloriesParamValue = req.getParameter("calories");
        String idParamValue = req.getParameter("id");
        if (!dateTimeParamValue.equals("") && descriptionParamValue != null && caloriesParamValue != null) {
            if (caloriesParamValue.matches("^\\d+$")) {
                LOG.debug("add new UserMeal");
                MealsStorage mealsStorage = MealsStorage.getInstance();
                UserMeal newObjectUserMeal = new UserMeal(LocalDateTime.parse(dateTimeParamValue), descriptionParamValue, Integer.valueOf(caloriesParamValue));
                if (idParamValue.matches("^\\d+$")) mealsStorage.edit(Integer.valueOf(idParamValue), newObjectUserMeal);
                else mealsStorage.add(newObjectUserMeal);
            }
        }
        showMainPage(req, resp);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MealsStorage mealsStorage = MealsStorage.getInstance();

        String doParamValue = request.getParameter("do");
        String idParamValue = request.getParameter("id");
        if (doParamValue != null) {
            if (idParamValue != null && idParamValue.matches("^\\d+$")) {
                if (doParamValue.equals("delete")) {
                    LOG.debug("remove UserMeal with id: " + idParamValue);
                    mealsStorage.remove(Integer.parseInt(idParamValue));
                }else if (doParamValue.equals("edit")) {
                    LOG.debug("show edit page UserMeal with id: " + idParamValue);
                    UserMeal um = mealsStorage.getById(Integer.valueOf(idParamValue));
                    request.setAttribute("dateTime", um.getDateTime());
                    request.setAttribute("description", um.getDescription());
                    request.setAttribute("calories", um.getCalories());
                    request.setAttribute("id", um.getId());
                }
            }
        }

        showMainPage(request, response);
    }

    private void showMainPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("get mealList");
        MealsStorage mealsStorage = MealsStorage.getInstance();
        List<UserMealWithExceed> list = mealsStorage.listWithExceed();
        request.setAttribute("meals", list);

        request.getRequestDispatcher("/mealList.jsp").forward(request, response);
    }
}
