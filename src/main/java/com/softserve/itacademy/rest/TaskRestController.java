package com.softserve.itacademy.rest;

import com.softserve.itacademy.dto.TaskDto;
import com.softserve.itacademy.dto.TaskTransformer;
import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.service.StateService;
import com.softserve.itacademy.service.TaskService;
import com.softserve.itacademy.service.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/tasks")
public class TaskRestController {
    private final TaskService taskService;
    private final ToDoService todoService;
    private final StateService stateService;

    @Autowired
    public TaskRestController(TaskService taskService, ToDoService todoService, StateService stateService) {
        this.taskService = taskService;
        this.todoService = todoService;
        this.stateService = stateService;
    }

    @PostMapping({"/{todoId}"})
    @PreAuthorize("@toDoRestController.getToDoReadingLevel(#todoId) <= 1 || hasAuthority('ADMIN')")
    public TaskDto create(@PathVariable long todoId, @Valid @RequestBody TaskDto taskDto) {
        Task task = TaskTransformer.convertToEntity(
                taskDto,
                todoService.readById(todoId),
                stateService.getByName("New")
        );
        task = taskService.create(task);
        taskDto = TaskTransformer.convertToDto(task);
        return taskDto;
    }

    @GetMapping("/{taskId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public TaskDto read(@PathVariable long taskId) {
        Task task = taskService.readById(taskId);
        return TaskTransformer.convertToDto(task);
    }

    @PutMapping("/{todoId}/{taskId}")
    @PreAuthorize("@toDoRestController.getToDoReadingLevel(#todoId) <= 1 || hasAuthority('ADMIN')")
    public TaskDto update(@PathVariable long todoId, @PathVariable long taskId, @Valid @RequestBody TaskDto taskDto) {
        if (taskId != taskDto.getId()) {
            throw new IllegalArgumentException("id cannot be changed");
        }
        Task task = TaskTransformer.convertToEntity(
                taskDto,
                todoService.readById(taskDto.getTodoId()),
                stateService.readById(taskDto.getStateId())
        );
        task = taskService.update(task);
        taskDto = TaskTransformer.convertToDto(task);
        return taskDto;
    }

    @DeleteMapping("/{todoId}/{taskId}")
    @PreAuthorize("@toDoRestController.getToDoReadingLevel(#todoId) <= 1 || hasAuthority('ADMIN')")
    public void delete(@PathVariable long todoId, @PathVariable long taskId) {
        taskService.delete(taskId);
    }
}
