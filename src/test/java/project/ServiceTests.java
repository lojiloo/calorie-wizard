package project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.dish.model.Dish;
import project.dish.repository.DishRepository;
import project.meal.converter.MealConverter;
import project.meal.dto.CreateMenuElementDto;
import project.meal.dto.CreateMenuRequest;
import project.meal.model.Meal;
import project.meal.model.MealDish;
import project.meal.repository.MealDishRepository;
import project.meal.repository.MealRepository;
import project.user.converter.UserConverter;
import project.user.dto.CreateUserRequest;
import project.user.dto.UserDto;
import project.user.model.User;
import project.user.repository.UserRepository;
import project.user.service.UserServiceImpl;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceTests {
    private final long USER_ID = 1L;
    private final long MEAL_ID = 1L;
    private final long DISH_ID = 1L;

    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    private DishRepository dishRepositoryMock;
    @Mock
    private MealRepository mealRepositoryMock;
    @Mock
    private MealDishRepository mealDishRepositoryMock;

    @InjectMocks
    private UserServiceImpl service;

    @BeforeEach
    public void setUp() {
        service = new UserServiceImpl(userRepositoryMock,
                dishRepositoryMock,
                mealRepositoryMock,
                mealDishRepositoryMock,
                new UserConverter(),
                new MealConverter()
        );
    }

    @Test
    @DisplayName("Корректное создание пользователя: расчёт нормы калорий и возраста")
    public void createUserTest() {
        User user = new User();
        user.setName("test");
        when(userRepositoryMock.save(any(User.class)))
                .thenReturn(user);

        CreateUserRequest userCreated = new CreateUserRequest();
        userCreated.setBirthday(LocalDate.of(2000, Month.JANUARY, 1));
        userCreated.setSex("FEMALE");
        userCreated.setWeight(100.5f);
        userCreated.setHeight(175f);

        UserDto userResult = service.createUser(userCreated);

        assertEquals(1807, userResult.getLimit()); //калории
        assertEquals(25, userResult.getAge()); //возраст
    }

    @ParameterizedTest
    @CsvSource({
            "MALE, 100, 200, 2246",
            "FEMALE, 100, 200, 1880"
    })
    @DisplayName("Проверка адекватности вычисляемой нормы калорий для мужчин и женщин")
    void testCalorieCalculation(String sex, float weight, float height, int expected) {
        CreateUserRequest request = new CreateUserRequest();
        request.setSex(sex);
        request.setWeight(weight);
        request.setHeight(height);
        request.setBirthday(LocalDate.of(2000, Month.JANUARY, 1));

        when(userRepositoryMock.save(any(User.class)))
                .thenReturn(new User());

        UserDto result = service.createUser(request);

        assertEquals(expected, result.getLimit());
    }

    @Test
    @DisplayName("Подсчёт калорийности в соответствии с весом добавленного продукта")
    public void calorieCalculatingOnTheFirstAdditionTest() {
        CreateMenuElementDto element = new CreateMenuElementDto(DISH_ID, 150.0f);
        CreateMenuRequest request = new CreateMenuRequest(List.of(element));

        Dish dish = new Dish();
        dish.setId(DISH_ID);
        dish.setCalories(200.0f);

        User user = new User();
        user.setId(USER_ID);

        Meal meal = new Meal();
        meal.setId(MEAL_ID);

        when(userRepositoryMock.findById(USER_ID))
                .thenReturn(Optional.of(user));
        when(mealRepositoryMock.findById(MEAL_ID))
                .thenReturn(Optional.of(meal));
        when(mealRepositoryMock.existsMealByUserIdAndId(USER_ID, MEAL_ID))
                .thenReturn(true);
        when(dishRepositoryMock.findAllByIdIn(any()))
                .thenReturn(List.of(dish));
        when(mealDishRepositoryMock.existsById(any()))
                .thenReturn(false);

        service.createMenu(request, USER_ID, MEAL_ID);

        verify(mealDishRepositoryMock).save(argThat(mealDish ->
                mealDish.getWeight() == 150.0f &&
                        mealDish.getCalories() == 300.0f
        ));
    }

    @Test
    @DisplayName("Пересчёт калорийности и веса при повторном добавлении продукта")
    public void calorieRecalculatingOnTheExtraAdditionTest() {
        CreateMenuElementDto element = new CreateMenuElementDto(DISH_ID, 150.0f);
        CreateMenuRequest request = new CreateMenuRequest(List.of(element));

        Dish dish = new Dish();
        dish.setId(DISH_ID);
        dish.setCalories(200.0f);

        MealDish existingMealDish = new MealDish();
        existingMealDish.setWeight(100.0f);
        existingMealDish.setCalories(200.0f);

        User user = new User();
        user.setId(USER_ID);

        Meal meal = new Meal();
        meal.setId(MEAL_ID);

        when(userRepositoryMock.findById(USER_ID))
                .thenReturn(Optional.of(user));
        when(mealRepositoryMock.findById(MEAL_ID))
                .thenReturn(Optional.of(meal));
        when(mealRepositoryMock.existsMealByUserIdAndId(USER_ID, MEAL_ID))
                .thenReturn(true);
        when(dishRepositoryMock.findAllByIdIn(any()))
                .thenReturn(List.of(dish));
        when(mealDishRepositoryMock.existsById(any()))
                .thenReturn(true);
        when(mealDishRepositoryMock.findById(any()))
                .thenReturn(Optional.of(existingMealDish));

        service.createMenu(request, USER_ID, MEAL_ID);

        verify(mealDishRepositoryMock).save(argThat(mealDish ->
                mealDish.getWeight() == 250.0f &&
                        mealDish.getCalories() == 500.0f
        ));
    }
}
