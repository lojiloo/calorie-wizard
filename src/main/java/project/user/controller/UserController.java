package project.user.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import project.meal.dto.CreateMealRequest;
import project.meal.dto.CreateMenuRequest;
import project.meal.dto.MealDto;
import project.user.dto.ChangeGoalRequest;
import project.user.dto.CreateUserRequest;
import project.user.dto.UserDto;
import project.user.dto.reports.CaloriesAndMealsDailyReport;
import project.user.dto.reports.LimitStatusForCurrentDayReport;
import project.user.service.UserService;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody @Valid CreateUserRequest request) {
        log.info("пришёл запрос на создание нового пользователя: {}", request.getEmail());
        return userService.createUser(request);
    }

    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public MealDto createMeal(@RequestBody @Valid CreateMealRequest request,
                              @PathVariable long userId) {
        log.info("пришёл запрос на добавление нового приёма пищи от пользователя с id={}", userId);
        return userService.createMeal(request, userId);
    }

    @PostMapping("/{userId}/{mealId}")
    @ResponseStatus(HttpStatus.CREATED)
    public MealDto createMealDish(@RequestBody @Valid CreateMenuRequest request,
                                  @PathVariable long userId,
                                  @PathVariable long mealId) {
        log.info("пришёл запрос на добавление блюда к приёму пищи с id={} от пользователя с id={}", mealId, userId);
        return userService.createMenu(request, userId, mealId);
    }

    @PatchMapping("/{userId}/goal")
    @ResponseStatus(HttpStatus.OK)
    public UserDto changeGoal(@RequestBody @Valid ChangeGoalRequest request,
                              @PathVariable long userId) {
        log.info("пришёл запрос на изменение цели от пользователя с id={}", userId);
        return userService.changeGoal(request, userId);
    }

    @GetMapping("/{userId}/reports/current")
    @ResponseStatus(HttpStatus.OK)
    public CaloriesAndMealsDailyReport getDailyCaloriesReport(@PathVariable long userId) {
        log.info("пришёл запрос на получение информации об общем количестве калорий и приёмов пищи за {} от пользователя с id={}",
                LocalDate.now(), userId);
        return userService.getCaloriesAndMealsDailyReport(userId);
    }

    @GetMapping("/{userId}/reports/limit")
    @ResponseStatus(HttpStatus.OK)
    public LimitStatusForCurrentDayReport getLimitStatusForCurrentDayReport(@PathVariable long userId) {
        log.info("пришёл запрос на получение информации об отношении установленного лимита и употреблённых калорий от пользователя с id={}",
                userId);
        return userService.getLimitStatusReport(userId);
    }

    @GetMapping("/{userId}/reports/total")
    @ResponseStatus(HttpStatus.OK)
    Map<LocalDate, CaloriesAndMealsDailyReport> getCaloriesAndMealsReportForFewDays(@PathVariable long userId,
                                                                                    @RequestParam("start") @JsonFormat(pattern = "yyyy-MM-dd") LocalDate dateStart,
                                                                                    @RequestParam("end") @JsonFormat(pattern = "yyyy-MM-dd") LocalDate dateEnd) {
        log.info("пришёл запрос на получение информации об общем количестве калорий и приёмов пищи от пользователя с id={} за период с {} по {}",
                userId, dateStart, dateEnd);
        return userService.getCaloriesAndMealsReportForFewDays(dateStart, dateEnd);
    }
}
