package project.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangeGoalRequest {
    @NotNull
    private String goal;
}
