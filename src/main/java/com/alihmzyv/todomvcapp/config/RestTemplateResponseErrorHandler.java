package com.alihmzyv.todomvcapp.config;

import com.alihmzyv.todomvcapp.exception.LoginException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Slf4j
@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return (response.getStatusCode().is4xxClientError());
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        HttpStatusCode statusCode = response.getStatusCode();
        log.info("Exception: {}", response.getStatusText());
        if (statusCode.value() == 401) {
            throw new LoginException("Wrong email address and/or password. Try again.");
        }
    }
}
