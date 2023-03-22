package com.alihmzyv.todomvcapp.controller;

import com.alihmzyv.todomvcapp.exception.LoginException;
import com.alihmzyv.todomvcapp.model.dto.user.LoginFormDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@ControllerAdvice
public class GeneralExceptionHandler {
    @ExceptionHandler(LoginException.class)
    public ModelAndView handleError(HttpServletRequest req, LoginException ex) {
        log.error("Request: " + req.getRequestURL() + " raised " + ex);
        ModelAndView mav = new ModelAndView("/login");
        mav.addObject("ex", ex);
        mav.addObject("loginFormDto", new LoginFormDto());
        return mav;
    }
}
