package com.softserve.itacademy.config.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.softserve.itacademy.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.time.Instant;

@Component
public class WebJWTTokenGenerator {
    public void generateToken(HttpServletResponse response){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null) {

            User user  = (User) authentication.getPrincipal();
            Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());

            String accessToken = JWT.create()
                    .withSubject(user.getEmail())
                    .withExpiresAt(Instant.now().plusSeconds(30 * 60))
                    .withIssuer("/api/login")
                    .withClaim("role", user.getRole().getName())
                    .sign(algorithm);

            response.setHeader("Authorization", accessToken);

        }
    }
}
