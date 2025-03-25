package project.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import project.user.model.Goal;
import project.user.model.Sex;

@Setter
@Getter
public class UserDto {
    private String name;
    private Integer age;
    private Float weight;
    private Float height;
    private Sex sex;
    @JsonProperty("calories_limit")
    private Integer limit;
    private Goal goal;
}
