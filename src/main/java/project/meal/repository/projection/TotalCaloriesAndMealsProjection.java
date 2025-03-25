package project.meal.repository.projection;

import java.time.LocalDate;

public interface TotalCaloriesAndMealsProjection {
    LocalDate getDate();

    Integer getCaloriesTotal();

    Integer getMealsTotal();
}
