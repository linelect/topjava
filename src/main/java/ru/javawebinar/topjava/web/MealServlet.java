package ru.javawebinar.topjava.web;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.LoggedUser;
import ru.javawebinar.topjava.LoggerWrapper;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;
import ru.javawebinar.topjava.repository.mock.InMemoryUserMealRepositoryImpl;
import ru.javawebinar.topjava.repository.UserMealRepository;
import ru.javawebinar.topjava.util.UserMealsUtil;
import ru.javawebinar.topjava.web.meal.UserMealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

/**
 * User: gkislin
 * Date: 19.08.2014
 */
public class MealServlet extends HttpServlet {
    private static final LoggerWrapper LOG = LoggerWrapper.get(MealServlet.class);

    private UserMealRestController userMealRestController;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        userMealRestController = appCtx.getBean(UserMealRestController.class);
        appCtx.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String fromDate = request.getParameter("fromDate");
        String toDate = request.getParameter("toDate");
        String fromTime = request.getParameter("fromTime");
        String toTime = request.getParameter("toTime");
        boolean filterByDate = fromDate != null & toDate !=null;
        boolean filterByTime = fromTime != null & toTime != null;
        if (filterByDate | filterByTime) {

            List<UserMeal> mealList;
            assert fromDate != null;
            if (filterByDate && (!fromDate.equals("") & !toDate.equals(""))) {
                mealList = UserMealsUtil.getFilteredByDate(userMealRestController.getAllByUserId(LoggedUser.id()), LocalDate.parse(fromDate), LocalDate.parse(toDate));
                request.setAttribute("fromDate", LocalDate.parse(fromDate));
                request.setAttribute("toDate", LocalDate.parse(toDate));
            } else {
                mealList = userMealRestController.getAllByUserId(LoggedUser.id());
            }

            List<UserMealWithExceed> mealWithExceedList;
            if (filterByTime && (!fromTime.equals("") & !toTime.equals(""))) {
                mealWithExceedList = UserMealsUtil.getFilteredWithExceeded(mealList,
                                LocalTime.parse(fromTime),
                                LocalTime.parse(toTime),
                                UserMealsUtil.DEFAULT_CALORIES_PER_DAY);
                request.setAttribute("fromTime", LocalTime.parse(fromTime));
                request.setAttribute("toTime", LocalTime.parse(toTime));
            }else {
                mealWithExceedList = UserMealsUtil.getWithExceeded(mealList, UserMealsUtil.DEFAULT_CALORIES_PER_DAY);
            }

            request.setAttribute("mealList", mealWithExceedList);
            request.getRequestDispatcher("/mealList.jsp").forward(request, response);

        } else {
            String id = request.getParameter("id");
            UserMeal userMeal = new UserMeal(id.isEmpty() ? null : Integer.valueOf(id),
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.valueOf(request.getParameter("calories")), LoggedUser.id());
            LOG.info(userMeal.isNew() ? "Create {}" : "Update {}", userMeal);
            userMealRestController.save(userMeal);
            response.sendRedirect("meals");
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            LOG.info("getAll");
            request.setAttribute("mealList",
                    UserMealsUtil.getWithExceeded(userMealRestController.getAllByUserId(LoggedUser.id()), UserMealsUtil.DEFAULT_CALORIES_PER_DAY));
            request.getRequestDispatcher("/mealList.jsp").forward(request, response);
        } else if (action.equals("delete")) {
            int id = getId(request);
            LOG.info("Delete {}", id);
            userMealRestController.delete(id);
            response.sendRedirect("meals");
        } else {
            final UserMeal meal = action.equals("create") ?
                    new UserMeal(LocalDateTime.now(), "", 1000, LoggedUser.id()) :
                    userMealRestController.get(getId(request));
            request.setAttribute("meal", meal);
            request.getRequestDispatcher("mealEdit.jsp").forward(request, response);
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.valueOf(paramId);
    }
}
