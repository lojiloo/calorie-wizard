package project.user.converter;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import project.user.dto.CreateUserRequest;
import project.user.dto.UserDto;
import project.user.model.User;

@Component
public class UserConverter {
    private final ModelMapper mapper;

    public UserConverter() {
        this.mapper = new ModelMapper();
    }

    public User toUser(CreateUserRequest request) {
        return mapper.map(request, User.class);
    }

    public UserDto toDto(User user) {
        return mapper.map(user, UserDto.class);
    }
}
