package project.meal.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.dish.model.Dish;
import project.user.model.User;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "dishes_on_meal")
public class MealDish {
    @Id
    @Embedded
    private MealDishId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("mealId")
    @JoinColumn(name = "meal_id")
    private Meal meal;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("dishId")
    @JoinColumn(name = "dish_id")
    private Dish dish;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "weight")
    private Float weight;

    @Column(name = "calories_total")
    private Float calories;
}
