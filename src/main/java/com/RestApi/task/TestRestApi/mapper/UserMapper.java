package com.RestApi.task.TestRestApi.mapper;

import com.RestApi.task.TestRestApi.dto.UserDto;
import com.RestApi.task.TestRestApi.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;

    public User convertToUser(UserDto userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    public UserDto convertToDTO(User user) {
        return modelMapper.map(user, UserDto.class);
    }

}
