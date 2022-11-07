package com.softserve.itacademy.dto;

import com.softserve.itacademy.model.ToDo;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class TodoPreviewDto {


    private long id;
    private String title;

    private LocalDateTime createdAt;

    private String ownerName;

    public TodoPreviewDto(ToDo toDo){

        id = toDo.getId();
        title = toDo.getTitle();
        createdAt = toDo.getCreatedAt();
        ownerName = toDo.getOwner().getFirstName() + " " + toDo.getOwner().getLastName();

    }

}
