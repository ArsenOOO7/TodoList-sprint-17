package com.softserve.itacademy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softserve.itacademy.model.ToDo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private long id;

    @NotBlank(message = "The 'firstName' cannot be empty")
    @Pattern(regexp = "[A-Z][a-z]+", message = "Must start with a capital letter followed by one or more lowercase letters")
    private String firstName;

    @NotBlank(message = "The 'lastName' cannot be empty")
    @Pattern(regexp = "[A-Z][a-z]+", message = "Must start with a capital letter followed by one or more lowercase letters")
    private String lastName;

    @NotBlank(message = "The 'email' cannot be empty")
    @Email(regexp = "[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}")
    private String email;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private long roleId;

    public UserDto(long id, String firstName, String lastName, String email, long roleId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = null;
        this.roleId = roleId;
    }
}
