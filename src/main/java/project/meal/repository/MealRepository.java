package project.meal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.meal.model.Meal;

public interface MealRepository extends JpaRepository<Meal, Long> {

    boolean existsMealByUserIdAndId(long userId, long mealId);

}
