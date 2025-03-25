package project.meal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateMenuElementDto {
    @NotNull
    @Positive
    @JsonProperty("dish_id")
    private Long dishId;
    @NotNull
    @Positive
    @Max(5000)
    private Float weight;
}
