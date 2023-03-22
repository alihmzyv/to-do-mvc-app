package com.alihmzyv.todomvcapp.config;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;


@RequiredArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@ConfigurationProperties(prefix = "api")
public class ApiProperties {
    String jwtSecret;
    String baseUrl;

    @Bean
    public Algorithm jwtAlgorithm() {
        return Algorithm.HMAC256(jwtSecret);
    }
}
