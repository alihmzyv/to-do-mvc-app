package com.alihmzyv.todomvcapp.model.dto.error;

import lombok.*;
import lombok.experimental.FieldDefaults;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
@Builder
public class ErrorResponse {
    private Integer code;
    private String field;
    private String message;

    public static ErrorResponseBuilder badRequestErrResp() {
        return builder()
                .code(BAD_REQUEST.value());
    }

    public static ErrorResponseBuilder authenticationErrResp() {
        return builder()
                .code(UNAUTHORIZED.value());
    }

    public static ErrorResponseBuilder fieldValidationErrResp(String field, String message) {
        return badRequestErrResp()
                .field(field)
                .message(message);
    }

    public static ErrorResponseBuilder fieldValidationErrResp(String field, String message, Object rejectedValue) {
        return fieldValidationErrResp(field, String.format("%s: %s", message, rejectedValue));
    }
}
