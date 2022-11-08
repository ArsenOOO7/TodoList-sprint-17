package com.softserve.itacademy.rest;

import com.softserve.itacademy.config.jwt.WebJWTTokenGenerator;
import com.softserve.itacademy.dto.UserTransformer;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

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
        final String url = "https://localhost:9099/api_login?username={username}&password={password}";
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        RestTemplate restTemplate = new RestTemplate();
        String temp = restTemplate.getForObject(url, String.class, params);

        return ResponseEntity.ok(temp);

// TODO

//        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getParameter("username"), request.getParameter("password")));
//        User user = (User) authentication.getPrincipal();
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        webJWTTokenGeneratorFilter.generateToken(response);
//        return ResponseEntity.ok(UserTransformer.convertToDto(user));
    }

}
