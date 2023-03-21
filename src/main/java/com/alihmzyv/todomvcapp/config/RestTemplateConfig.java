package com.alihmzyv.todomvcapp.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class RestTemplateConfig {
    private final Environment env;

    @Bean
    public RestTemplate restTemplate(RestTemplate restTemplate) {
        String baseUrl = Objects.requireNonNull(env.getProperty("api.base.url"));
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(baseUrl));
        return restTemplate;
    }
}
