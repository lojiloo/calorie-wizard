package project.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.dish.model.Dish;
import project.dish.repository.DishRepository;
import project.errors.exception.ConditionViolatedException;
import project.errors.exception.NoSuchEnumValueException;
import project.errors.exception.NotFoundException;
import project.meal.converter.MealConverter;
import project.meal.dto.CreateMealRequest;
import project.meal.dto.CreateMenuElementDto;
import project.meal.dto.CreateMenuRequest;
import project.meal.dto.MealDto;
import project.meal.model.Meal;
import project.meal.model.MealDish;
import project.meal.model.MealDishId;
import project.meal.repository.MealDishRepository;
import project.meal.repository.MealRepository;
import project.meal.repository.projection.TotalCaloriesAndMealsProjection;
import project.user.converter.UserConverter;
import project.user.dto.ChangeGoalRequest;
import project.user.dto.CreateUserRequest;
import project.user.dto.UserDto;
import project.user.dto.reports.CaloriesAndMealsDailyReport;
import project.user.dto.reports.LimitStatusForCurrentDayReport;
import project.user.model.Goal;
import project.user.model.Sex;
import project.user.model.User;
import project.user.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final DishRepository dishRepository;
    private final MealRepository mealRepository;
    private final MealDishRepository mealDishRepository;

    private final UserConverter userConverter;
    private final MealConverter mealConverter;

    private final float PORTION_WEIGHT = 100;

    @Override
    public UserDto createUser(CreateUserRequest request) {
        int age = calculateAge(request.getBirthday());
        int limit = calculateCalories(Sex.from(request.getSex()).get(), request.getWeight(), request.getHeight(), age);

        User user = userConverter.toUser(request);
        user.setLimit(limit);
        userRepository.save(user);

        UserDto dto = userConverter.toDto(user);
        dto.setAge(age);

        return dto;
    }

    @Override
    public MealDto createMeal(CreateMealRequest request, long userId) {
        User user = findUserById(userId);

        Meal meal = mealConverter.toMeal(request);
        meal.setUser(user);
        mealRepository.save(meal);

        return mealConverter.toMealDto(meal);
    }

    @Override
    public MealDto createMenu(CreateMenuRequest request, long userId, long mealId) {
        User user = findUserById(userId);
        Meal meal = findMealById(mealId);

        if (!mealRepository.existsMealByUserIdAndId(userId, mealId)) {
            throw new ConditionViolatedException("Право на редактирование ограничено");
        }

        Map<Long, CreateMenuElementDto> menu = request.getMenu()
                .stream()
                .collect(Collectors.toMap(
                        CreateMenuElementDto::getDishId,
                        Function.identity()
                ));

        List<Long> dishesIds = request.getMenu().stream().map(CreateMenuElementDto::getDishId).toList();
        Map<Long, Dish> dishes = dishRepository.findAllByIdIn(dishesIds)
                .stream()
                .collect(Collectors.toMap(
                        Dish::getId,
                        Function.identity()
                ));

        for (Long dishId : menu.keySet()) {
            MealDishId mealDishId = new MealDishId(mealId, dishId);
            if (mealDishRepository.existsById(mealDishId)) {
                MealDish mealDish = mealDishRepository.findById(mealDishId).get();

                float extraWeight = menu.get(dishId).getWeight();
                mealDish.setWeight(mealDish.getWeight() + extraWeight);
                mealDish.setCalories(mealDish.getWeight() / PORTION_WEIGHT * dishes.get(dishId).getCalories()
                );

                mealDishRepository.save(mealDish);
            } else {
                MealDish mealDish = new MealDish();
                mealDish.setId(new MealDishId(mealId, dishId));

                float calories = menu.get(dishId).getWeight() / PORTION_WEIGHT * dishes.get(dishId).getCalories();
                mealDish.setCalories(calories);
                mealDish.setWeight(menu.get(dishId).getWeight());

                mealDish.setUser(user);
                mealDish.setDish(dishes.get(dishId));
                mealDish.setMeal(meal);

                mealDishRepository.save(mealDish);
            }
        }

        return mealConverter.toMealDto(meal);
    }

    @Override
    public UserDto changeGoal(ChangeGoalRequest request, long userId) {
        Goal newGoal = Goal.from(request.getGoal()).orElseThrow(
                () -> new NoSuchEnumValueException(String.format("Значение %s неизвестно", request.getGoal()))
        );

        User user = findUserById(userId);
        user.setGoal(newGoal);
        user.setAge(calculateAge(user.getBirthday()));

        userRepository.save(user);

        return userConverter.toDto(user);
    }

    @Override
    public CaloriesAndMealsDailyReport getCaloriesAndMealsDailyReport(long userId) {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.atTime(23, 59, 59);

        TotalCaloriesAndMealsProjection result = mealDishRepository.countCaloriesTotalDaily(start, end);

        return new CaloriesAndMealsDailyReport(result.getCaloriesTotal(), result.getMealsTotal());
    }

    @Override
    public LimitStatusForCurrentDayReport getLimitStatusReport(long userId) {
        int limit = findUserById(userId).getLimit();
        int actualAmount = getCaloriesAndMealsDailyReport(userId).getCaloriesTotal();

        int diff = limit - actualAmount;
        if (diff >= 0) {
            return new LimitStatusForCurrentDayReport(false, 0);
        } else {
            return new LimitStatusForCurrentDayReport(true, Math.abs(diff));
        }
    }

    @Override
    public Map<LocalDate, CaloriesAndMealsDailyReport> getCaloriesAndMealsReportForFewDays(LocalDate dateStart, LocalDate dateEnd) {
        List<TotalCaloriesAndMealsProjection> results = mealDishRepository.countCaloriesTotalPerDay(dateStart, dateEnd);

        return results.stream()
                .filter(element -> element.getDate() != null)
                .collect(Collectors.toMap(
                        TotalCaloriesAndMealsProjection::getDate,
                        value -> new CaloriesAndMealsDailyReport(value.getCaloriesTotal(), value.getMealsTotal())
                ));
    }

    private int calculateCalories(Sex sex, float weight, float height, int age) {
        switch (sex) {
            case MALE:
                return (int) Math.round(88.36 + (13.4 * weight) + (4.8 * height) - (5.7 * age));
            case FEMALE:
                return (int) Math.round(447.6 + (9.2 * weight) + (3.1 * height) - (4.3 * age));
            default:
                return 0;
        }
    }

    private int calculateAge(LocalDate birthday) {
        return Period.between(birthday, LocalDate.now()).getYears();
    }

    private Meal findMealById(long mealId) {
        return mealRepository.findById(mealId).orElseThrow(
                () -> new NotFoundException(String.format("Приём пищи с id=%d не найден", mealId))
        );
    }

    private User findUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с id=%d не найден", userId))
        );
    }

}
