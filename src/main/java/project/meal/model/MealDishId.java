package project.meal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class MealDishId {
    @Column(name = "meal_id")
    private Long mealId;

    @Column(name = "dish_id")
    private Long dishId;
}
