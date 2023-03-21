package com.alihmzyv.todomvcapp.security.util;

import jakarta.servlet.http.HttpServletResponse;

public interface ResponseHandler {
    void handle(HttpServletResponse response, Object responseBody);
}
