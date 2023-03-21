package com.alihmzyv.todomvcapp.security.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class JwtProperties {
    @Value("${jwt.secret}")
    private String secret;
}
