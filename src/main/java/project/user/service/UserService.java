package project.user.service;

import project.meal.dto.CreateMealRequest;
import project.meal.dto.CreateMenuRequest;
import project.meal.dto.MealDto;
import project.user.dto.ChangeGoalRequest;
import project.user.dto.CreateUserRequest;
import project.user.dto.UserDto;
import project.user.dto.reports.CaloriesAndMealsDailyReport;
import project.user.dto.reports.LimitStatusForCurrentDayReport;

import java.time.LocalDate;
import java.util.Map;

public interface UserService {
    //добавить юзера
    UserDto createUser(CreateUserRequest request);

    //добавить приём пищи
    MealDto createMeal(CreateMealRequest request, long userId);

    //добавить список блюд в приём пищи
    MealDto createMenu(CreateMenuRequest request, long userId, long mealId);

    //изменить цель
    UserDto changeGoal(ChangeGoalRequest request, long userId);

    //получить отчёт : калории за день
    CaloriesAndMealsDailyReport getCaloriesAndMealsDailyReport(long userId);

    //получить отчёт : превышен ли лимит калорий
    LimitStatusForCurrentDayReport getLimitStatusReport(long userId);

    //получить отчёт : история питания по дням
    Map<LocalDate, CaloriesAndMealsDailyReport> getCaloriesAndMealsReportForFewDays(LocalDate dateStart, LocalDate dateEnd);
}
