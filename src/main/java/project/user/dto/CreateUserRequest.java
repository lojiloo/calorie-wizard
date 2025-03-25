package project.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import project.user.model.Goal;
import project.user.model.Sex;
import project.validation.CheckEnum;

import java.time.LocalDate;

@Setter
@Getter
public class CreateUserRequest {
    @Size(min = 1, max = 300)
    @NotBlank
    private String name;
    @Email
    @NotBlank
    private String email;
    @NotNull
    @Past
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    @NotNull
    @Max(300)
    @Positive
    private Float weight;
    @NotNull
    @Max(300)
    @Positive
    private Float height;
    @NotNull
    @CheckEnum(enumClass = Sex.class)
    private String sex;
    @CheckEnum(enumClass = Goal.class)
    private String goal;
}
