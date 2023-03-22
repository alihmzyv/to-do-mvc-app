package com.alihmzyv.todomvcapp.security.config;

import com.alihmzyv.todomvcapp.config.ApiProperties;
import com.alihmzyv.todomvcapp.controller.GeneralExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JWTHttpConfigurer jwtHttpConfigurer;
    @Value("#{'${permit.all.paths.all}'.split(', ')}")
    private List<String> permitPathsAll;
    private final ApiProperties apiProperties;
    private final GeneralExceptionHandler generalExceptionHandler;

    @Bean
    public SecurityFilterChain http(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .cors().configurationSource(req -> {
                    CorsConfiguration corsConf = new CorsConfiguration();
                    corsConf.setAllowedOriginPatterns(Collections.singletonList("*"));
                    corsConf.setAllowedMethods(Collections.singletonList("*"));
                    corsConf.setAllowCredentials(true);
                    corsConf.setAllowedHeaders(List.of("*"));
                    corsConf.setExposedHeaders(List.of("*"));
                    return corsConf;
                }).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeHttpRequests()
                .requestMatchers(permitPathsAll.toArray(String[]::new)).permitAll().and()
                .apply(jwtHttpConfigurer).and()
                .build();
    }
}
