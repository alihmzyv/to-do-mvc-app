package com.alihmzyv.todomvcapp.exception.security;

public class CustomAuthorizationException extends SecurityException {
    public CustomAuthorizationException() {
    }

    public CustomAuthorizationException(String s) {
        super(s);
    }

    public CustomAuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomAuthorizationException(Throwable cause) {
        super(cause);
    }

    public CustomAuthorizationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
