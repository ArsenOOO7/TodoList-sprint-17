package com.softserve.itacademy.rest;

import com.softserve.itacademy.model.RegisteredUser;
import com.softserve.itacademy.repository.RegisteredUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RestController
public class LoginRestController {

    @Autowired
    private RegisteredUserRepository registeredUserRepository;

    @RequestMapping("/api/login")
    public ResponseEntity<?> login(HttpServletRequest request, HttpServletResponse response) {
        Long userId = Long.parseLong(request.getParameter("user_id"));
        String token = request.getParameter("token");

        Optional<RegisteredUser> optional =  registeredUserRepository.findByUserId(userId);
        RegisteredUser registeredUser;
        if (optional.isPresent()) {
            registeredUser = optional.get();
            if (!registeredUser.getToken().equals(token)) {
                registeredUser.setToken(token);
                registeredUserRepository.save(registeredUser);
            }
        } else {
            registeredUser = new RegisteredUser();
            registeredUser.setUserId(userId);
            registeredUser.setToken(token);
            registeredUserRepository.save(registeredUser);
        }
        return ResponseEntity.ok(registeredUser);
    }

}
