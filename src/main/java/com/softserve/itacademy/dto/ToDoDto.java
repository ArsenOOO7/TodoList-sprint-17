package com.softserve.itacademy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ToDoDto {
    private long id;

    @NotBlank(message = "The 'title' cannot be empty")
    private String title;

    private LocalDateTime createdAt;

    @NotNull
    @Min(1L)
    private long ownerId;

    private List<Long> tasks;

    private List<Long> collaborators;
}
