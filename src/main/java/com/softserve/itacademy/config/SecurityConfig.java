package com.softserve.itacademy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;


import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final WebAuthenticationEntryPoint webAuthenticationEntryPoint;
    private final UserDetailsService userDetailsService;

    private final AccessDeniedHandler accessDeniedHandler;

    private final WebAccessSuccessHandler webAccessSuccessHandler;

    public SecurityConfig(WebAuthenticationEntryPoint webAuthenticationEntryPoint, UserDetailsService userDetailsService, AccessDeniedHandler accessDeniedHandler, WebAccessSuccessHandler webAccessSuccessHandler) {
        this.webAuthenticationEntryPoint = webAuthenticationEntryPoint;
        this.userDetailsService = userDetailsService;
        this.accessDeniedHandler = accessDeniedHandler;
        this.webAccessSuccessHandler = webAccessSuccessHandler;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .anyRequest()
//                .authenticated()
                .permitAll(); // TODO change to JWT

//                .and()
//                .formLogin()
//                .loginPage("/login-form")
//                .loginProcessingUrl("/login")
//                .failureUrl("/login-form?error=true")
//                .successHandler(webAccessSuccessHandler)
//                .permitAll()
//
//                .and()
//                .logout()
//                .logoutUrl("/perform-logout")
//                .logoutSuccessUrl("/login-form")
//                .deleteCookies("JSESSIONID")
//
//                .and()
//                .exceptionHandling()
//                .authenticationEntryPoint(webAuthenticationEntryPoint)
//
//                .and()
////                .exceptionHandling().accessDeniedHandler(accessDeniedHandler);
//                .httpBasic();
    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth, AuthenticationProvider provider) throws Exception {
        auth.authenticationProvider(provider);
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
}
