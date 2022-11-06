package com.softserve.itacademy;

import com.softserve.itacademy.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class ToDoListApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ToDoListApplication.class, args);

    }

}
