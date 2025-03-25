package project.meal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@JsonPropertyOrder({"user_id", "mealtime", "menu"})
public class MealDto {
    @JsonProperty("user_id")
    private Long userId;
    private List<MealDishDto> menu;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime mealtime;
}
