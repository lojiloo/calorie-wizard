package project.meal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.meal.model.MealDish;
import project.meal.model.MealDishId;
import project.meal.repository.projection.TotalCaloriesAndMealsProjection;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface MealDishRepository extends JpaRepository<MealDish, MealDishId> {

    @Query("""
            SELECT FUNCTION('DATE', md.meal.mealtime) as date, COALESCE(count(distinct md.meal.id), 0) as mealsTotal, COALESCE(sum(md.calories), 0) as caloriesTotal
            FROM MealDish md
            JOIN md.meal
            WHERE md.meal.mealtime BETWEEN :start AND :end
            GROUP BY FUNCTION('DATE', md.meal.mealtime)
            """)
    TotalCaloriesAndMealsProjection countCaloriesTotalDaily(LocalDateTime start, LocalDateTime end);

    @Query("""
            SELECT FUNCTION('DATE', md.meal.mealtime) as date, COALESCE(count(distinct md.meal.id), 0) as mealsTotal, COALESCE(sum(md.calories), 0) as caloriesTotal
            FROM MealDish md
            JOIN md.meal
            WHERE FUNCTION('DATE', md.meal.mealtime) BETWEEN :start AND :end
            GROUP BY FUNCTION('DATE', md.meal.mealtime)
            """)
    List<TotalCaloriesAndMealsProjection> countCaloriesTotalPerDay(LocalDate start, LocalDate end);

}
