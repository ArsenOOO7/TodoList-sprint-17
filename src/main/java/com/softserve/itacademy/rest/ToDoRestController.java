package com.softserve.itacademy.rest;

import com.softserve.itacademy.dto.ToDoDto;
import com.softserve.itacademy.dto.ToDoTransformer;
import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.RoleService;
import com.softserve.itacademy.service.TaskService;
import com.softserve.itacademy.service.ToDoService;
import com.softserve.itacademy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/todo")
public class ToDoRestController {

    private final ToDoService todoService;
    private final TaskService taskService;
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public ToDoRestController(ToDoService todoService, TaskService taskService, UserService userService, RoleService roleService) {
        this.todoService = todoService;
        this.taskService = taskService;
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostMapping({"/", ""})
//    @PreAuthorize("#ownerId == principal.id || hasAuthority('ADMIN')")
    public ToDoDto create(@Valid @RequestBody ToDoDto toDoDto) {
        toDoDto.setCreatedAt(LocalDateTime.now());
        ToDo toDo = ToDoTransformer.convertToEntity(
                toDoDto,
                userService.readById(toDoDto.getOwnerId()) // TODO: ownerId from principal.id
        );
        toDo = todoService.create(toDo);
        toDoDto = ToDoTransformer.convertToDto(toDo);
        return toDoDto;
    }

    @GetMapping("/{todoId}")
//    @PreAuthorize("@toDoController.getToDoReadingLevel(#todoId) <= 1 || hasAuthority('ADMIN')")
    public ToDoDto read(@PathVariable long todoId) {
        ToDo toDo = todoService.readById(todoId);
        return ToDoTransformer.convertToDto(toDo);
    }

    @PutMapping("/{todoId}")
//    @PreAuthorize("@toDoController.getToDoReadingLevel(#todoId) == 0 || hasAuthority('ADMIN')")
    public ToDoDto update(@PathVariable long todoId, @Valid @RequestBody ToDoDto toDoDto) {
        if (todoId != toDoDto.getId()) {
            throw new IllegalArgumentException("id cannot be changed");
        }
        ToDo toDo = todoService.readById(todoId);
        toDo.setTitle(toDoDto.getTitle());
        // TODO: change owner
        toDo = todoService.update(toDo);
        return ToDoTransformer.convertToDto(toDo);
    }

    @DeleteMapping("/{todoId}")
//    @PreAuthorize("@toDoController.getToDoReadingLevel(#todoId) == 0 || hasAuthority('ADMIN')")
    public boolean delete(@PathVariable long todoId) {
        todoService.delete(todoId);
        return true;
    }

    @GetMapping("/all/user/{userId}")
//    @PreAuthorize("#userId == principal.id || hasAuthority('ADMIN')")
    public List<ToDoDto> getAll(@PathVariable long userId) {
        return todoService.getByUserId(userId).stream()
                .map(ToDoTransformer::convertToDto)
                .collect(Collectors.toList());
    }

    @PutMapping("/{todoId}/add-collaborator")
//    @PreAuthorize("@toDoController.getToDoReadingLevel(#todoId) == 0 || hasAuthority('ADMIN')")
    public boolean addCollaborator(@PathVariable long todoId, @RequestBody long collaboratorId) {
        ToDo toDo = todoService.readById(todoId);
        List<User> collaborators = toDo.getCollaborators();
        User collaborator = userService.readById(collaboratorId);
        if (!collaborators.contains(collaborator) && collaboratorId != toDo.getOwner().getId()) {
            collaborators.add(collaborator);
            toDo.setCollaborators(collaborators);
            todoService.update(toDo);
            return true;
        }
        return false;
    }

    @PutMapping("/{todoId}/remove-collaborator")
//    @PreAuthorize("@toDoController.getToDoReadingLevel(#id) == 0 || hasAuthority('ADMIN')")
    public boolean removeCollaborator(@PathVariable long todoId, @RequestBody long collaboratorId) {
        ToDo toDo = todoService.readById(todoId);
        List<User> collaborators = toDo.getCollaborators();
        User collaborator = userService.readById(collaboratorId);
        boolean result = collaborators.remove(collaborator);
        toDo.setCollaborators(collaborators);
        todoService.update(toDo);
        return result;
    }

    /**
     * Check if current user is owner or collaborator
     *
     * @param todoId of ToDo
     * @return 0 if user is owner, 1 if user is collaborator, otherwise 2
     */
    public int getToDoReadingLevel(long todoId) {
        User todosUserDetail = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ToDo toDo = todoService.readById(todoId);

        if (toDo.getOwner().getId() == todosUserDetail.getId()) {
            return 0;
        }
        for (User user : toDo.getCollaborators()) {
            if (user.getId() == todosUserDetail.getId()) {
                return 1;
            }
        }
        return 2;
    }
}
