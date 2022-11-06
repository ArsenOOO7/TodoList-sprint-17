package com.softserve.itacademy.dto;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private long id;

    @NotBlank(message = "The 'name' cannot be empty")
    private String name;

    @NotNull
    @NotBlank
    private String priority;

    @NotNull
    @Min(1L)
    private long todoId;

    @NotNull
    @Min(1L)
    private long stateId;
}
