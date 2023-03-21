package com.alihmzyv.todomvcapp.security.config;

import com.alihmzyv.todomvcapp.security.JWTHttpConfigurer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JWTHttpConfigurer jwtHttpConfigurer;
    @Value("#{'${jwt.permit.all.paths.all}'.split(', ')}")
    private List<String> permitPathsAll;

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
                .requestMatchers(permitPathsAll.toArray(String[]::new)).permitAll()
                .anyRequest().hasAnyAuthority("ROLE_USER").and()
                .apply(jwtHttpConfigurer).and()
                .build();
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
