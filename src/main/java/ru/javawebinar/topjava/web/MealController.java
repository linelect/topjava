package ru.javawebinar.topjava.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.LoggedUser;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.service.UserMealService;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.util.UserMealsUtil;

import java.time.LocalDateTime;

@Controller
public class MealController {

    @Autowired
    private UserMealService userMealService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/meals", method = RequestMethod.GET)
    public String mealList(Model model) {
        model.addAttribute("mealList", UserMealsUtil.getWithExceeded(userMealService.getAll(LoggedUser.id()), UserMealsUtil.DEFAULT_CALORIES_PER_DAY));
        return "mealList";
    }

    @RequestMapping(value = "/meals/add", method = RequestMethod.GET)
    public String getAddMeal(Model model) {
        UserMeal userMeal = new UserMeal(LocalDateTime.now(), "", 1000);
        model.addAttribute("meal", userMeal);
        return "mealEdit";
    }

    @RequestMapping(value = "/meals/add", method = RequestMethod.POST)
    public String addMeal(UserMeal userMeal, BindingResult result, ModelMap model) {
        userMeal.setDateTime(LocalDateTime.now());
        userMeal.setUser(userService.get(LoggedUser.id()));
        userMealService.save(userMeal, LoggedUser.id());
        return "redirect:/meals";
    }

    @RequestMapping(value = "/meals/delete", method = RequestMethod.GET)
    public String deleteMeal(@RequestParam(value = "id", required = true) int id, Model model) {
        userMealService.delete(id, LoggedUser.id());
        return "redirect:/meals";
    }

    @RequestMapping(value = "/meals/update", method = RequestMethod.GET)
    public String getUpdateMeal(@RequestParam(value = "id", required = true) int id, Model model) {
        UserMeal userMeal = userMealService.get(id, LoggedUser.id());
        model.addAttribute("meal", userMeal);
        return "mealEdit";
    }

    @RequestMapping(value = "/meals/update", method = RequestMethod.POST)
    public String updateMeal(UserMeal userMeal, BindingResult result, ModelMap model) {
        userMeal.setDateTime(LocalDateTime.now());
        userMeal.setUser(userService.get(LoggedUser.id()));
        userMealService.update(userMeal, LoggedUser.id());
        return "redirect:/meals";
    }
}
