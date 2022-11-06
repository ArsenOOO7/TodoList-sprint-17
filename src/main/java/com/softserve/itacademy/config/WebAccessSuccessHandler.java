package com.softserve.itacademy.config;

import com.softserve.itacademy.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class WebAccessSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        User userDetails = (User) authentication.getPrincipal();
        String redirectUrl = "/";

        if (userDetails.getRole().getName().equalsIgnoreCase("user")) {
            redirectUrl = "/todos/all/users/" + userDetails.getId();
        }

        response.sendRedirect(redirectUrl);
    }
}
