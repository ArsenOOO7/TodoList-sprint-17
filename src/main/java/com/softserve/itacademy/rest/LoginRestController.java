package com.softserve.itacademy.rest;

import com.softserve.itacademy.config.jwt.WebJWTTokenGenerator;
import com.softserve.itacademy.dto.UserTransformer;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class LoginRestController {

    @Autowired
    private WebJWTTokenGenerator webJWTTokenGeneratorFilter;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;

    @RequestMapping("/api/login")
    public ResponseEntity<?> login(HttpServletRequest request, HttpServletResponse response){

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getParameter("username"), request.getParameter("password")));
        User user = (User) authentication.getPrincipal();

        SecurityContextHolder.getContext().setAuthentication(authentication);
        webJWTTokenGeneratorFilter.generateToken(response);
        return ResponseEntity.ok(UserTransformer.convertToDto(user));
    }

}
