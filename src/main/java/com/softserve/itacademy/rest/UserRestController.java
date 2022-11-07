package com.softserve.itacademy.rest;

import com.softserve.itacademy.dto.UserDto;
import com.softserve.itacademy.dto.UserTransformer;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.RoleService;
import com.softserve.itacademy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/people")
public class UserRestController {

//    @Autowired
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserRestController(BCryptPasswordEncoder passwordEncoder, UserService userService, RoleService roleService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostMapping({"/", ""})
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserDto> create(@Valid @RequestBody UserDto userDto) {
        User user = UserTransformer.convertToEntity(userDto,
                passwordEncoder.encode(userDto.getPassword()),
                roleService.readById(2)
        );
        user = userService.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserTransformer.convertToDto(user));
    }

    @GetMapping("/{userId}")
    @PreAuthorize("#userId == principal.id || hasAuthority('ADMIN')")
    public UserDto read(@PathVariable long userId) {
        User user = userService.readById(userId);
        return UserTransformer.convertToDto(user);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("#userId == principal.id || hasAuthority('ADMIN')")
    public UserDto update(@PathVariable long userId, @Valid @RequestBody UserDto userDto) {
        if (userId != userDto.getId()) {
            throw new IllegalArgumentException("id cannot be changed");
        }
        User user = userService.readById(userId);
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        // TODO: update password
        if (user.getRole().getName().equals("ADMIN")) {
            user.setRole(roleService.readById(userDto.getRoleId()));
        }
        user = userService.update(user);
        return UserTransformer.convertToDto(user);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public boolean delete(@PathVariable long userId) {
        userService.delete(userId);
        return true;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserDto> getAll() {
        return userService.getAll().stream()
                .map(UserTransformer::convertToDto)
                .collect(Collectors.toList());
    }
}
