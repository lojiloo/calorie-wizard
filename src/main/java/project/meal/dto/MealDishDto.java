package project.meal.dto;

import lombok.Getter;
import lombok.Setter;
import project.dish.dto.DishDto;

@Setter
@Getter
public class MealDishDto {
    private DishDto dish;
    private Float weight;
    private Float calories;
}
