package com.alihmzyv.todomvcapp.controller;

import com.alihmzyv.todomvcapp.model.dto.user.LoginFormDto;
import com.alihmzyv.todomvcapp.model.dto.user.RegisterUserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/")
public class LoginSignupController {
    private final RestTemplate restTemplate;

    @GetMapping("/login")
    public ModelAndView showLoginForm(ModelAndView mav) {
        mav.addObject("loginFormDto", new LoginFormDto());
        mav.setViewName("login");
        return mav;
    }

    @GetMapping("/signup")
    public ModelAndView signup(ModelAndView mav) {
        mav.addObject("registerUserDto", new RegisterUserDto());
        mav.setViewName("sign-up");
        return mav;
    }
}
