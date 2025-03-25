package project.meal.converter;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import project.meal.dto.CreateMealRequest;
import project.meal.dto.MealDto;
import project.meal.model.Meal;

@Component
public class MealConverter {
    private final ModelMapper mapper;

    public MealConverter() {
        this.mapper = new ModelMapper();
    }

    public Meal toMeal(CreateMealRequest request) {
        return mapper.map(request, Meal.class);
    }

    public MealDto toMealDto(Meal meal) {
        return mapper.map(meal, MealDto.class);
    }
}
