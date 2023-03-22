package com.alihmzyv.todomvcapp.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate(
            ApiProperties apiProperties,
            ResponseErrorHandler responseErrorHandler,
            RestTemplateBuilder restTemplateBuilder) {
        String baseUrl = apiProperties.getBaseUrl();
        DefaultUriBuilderFactory defaultUriBuilderFactory = new DefaultUriBuilderFactory(baseUrl);
        return restTemplateBuilder.uriTemplateHandler(defaultUriBuilderFactory)
                .errorHandler(responseErrorHandler)
                .build();
    }
}
