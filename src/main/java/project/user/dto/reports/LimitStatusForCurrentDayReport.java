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
@JsonPropertyOrder({"is_limit_exceeded", "calories_over_limit"})
public class LimitStatusForCurrentDayReport {
    @JsonProperty("is_limit_exceeded")
    private Boolean isLimitExceeded;
    @JsonProperty("calories_over_limit")
    private Integer caloriesOverLimit;
}
