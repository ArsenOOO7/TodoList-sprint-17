package com.softserve.itacademy.dto;

import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ToDoTransformer {
    public static ToDoDto convertToDto(ToDo toDo) {
        List<Long> taskIds = new ArrayList<>();
        if (toDo.getTasks() != null) {
            taskIds = toDo.getTasks().stream()
                    .map(Task::getId)
                    .collect(Collectors.toList());
        }
        List<Long> collaboratorsIds = new ArrayList<>();
        if (toDo.getTasks() != null) {
            collaboratorsIds = toDo.getCollaborators().stream()
                    .map(User::getId)
                    .collect(Collectors.toList());
        }

        return new ToDoDto(
                toDo.getId(),
                toDo.getTitle(),
                toDo.getCreatedAt(),
                toDo.getOwner().getId(),
                taskIds,
                collaboratorsIds
        );
    }

    public static ToDo convertToEntity(ToDoDto toDoDto, User owner) {
        ToDo toDo = new ToDo();
        toDo.setId(toDoDto.getId());
        toDo.setTitle(toDoDto.getTitle());
        if (toDo.getCreatedAt() == null) {
            toDo.setCreatedAt(LocalDateTime.now());
        } else {
            toDo.setCreatedAt(toDo.getCreatedAt());
        }
        toDo.setOwner(owner);
        return toDo;
    }
}
