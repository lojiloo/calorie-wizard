package project.user.dto.reports;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"meals_total", "calories_total"})
public class CaloriesAndMealsDailyReport {
    @JsonProperty("calories_total")
    private Integer caloriesTotal;
    @JsonProperty("meals_total")
    private Integer mealsTotal;
}
