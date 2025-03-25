package project.dish.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DishDto {
    private String name;
    private Float calories;
    private Float proteins;
    private Float fats;
    private Float carbohydrates;
}
