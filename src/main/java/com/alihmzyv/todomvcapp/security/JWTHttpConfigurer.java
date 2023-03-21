package com.alihmzyv.todomvcapp.security;

import com.alihmzyv.todomvcapp.filter.CustomAuthorizationFilter;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class JWTHttpConfigurer extends AbstractHttpConfigurer<JWTHttpConfigurer, HttpSecurity> {
    private final Algorithm algorithm;
    @Value("#{'${jwt.permit.all.paths.all}'.split(', ')}")
    private List<String> permitAllPaths;
    private final Environment env;

    @Override
    public void configure(HttpSecurity http) {
        CustomAuthorizationFilter customAuthorizationFilter =
                new CustomAuthorizationFilter(env.getProperty("cookie.name"), algorithm, permitAllPaths);
        http.addFilterBefore(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
