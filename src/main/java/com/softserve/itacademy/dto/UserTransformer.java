package com.softserve.itacademy.dto;

import com.softserve.itacademy.model.Role;
import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserTransformer {
    public static UserDto convertToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole().getId()
        );
    }

    public static User convertToEntity(UserDto userDto, String password, Role role) {
        User user = new User();
        user.setId(userDto.getId());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(password);
        user.setRole(role);
        return user;
    }
}
