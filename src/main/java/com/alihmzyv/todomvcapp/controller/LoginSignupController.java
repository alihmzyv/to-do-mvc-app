package com.alihmzyv.todomvcapp.controller;

import com.alihmzyv.todomvcapp.model.dto.base.BaseResponse;
import com.alihmzyv.todomvcapp.model.dto.security.TokenDto;
import com.alihmzyv.todomvcapp.model.dto.user.LoginFormDto;
import com.alihmzyv.todomvcapp.model.dto.user.RegisterUserDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static org.springframework.http.HttpMethod.POST;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/")
public class LoginSignupController {
    private final RestTemplate restTemplate;
    private final Environment env;

    @GetMapping("/login")
    public ModelAndView showLoginForm(ModelAndView mav) {
        mav.addObject("loginFormDto", new LoginFormDto());
        mav.setViewName("login");
        return mav;
    }

    @PostMapping("/login")
    public ModelAndView processLoginForm(
            ModelAndView mav,
            @ModelAttribute @Valid LoginFormDto loginFormDto,
            BindingResult bindingResult,
            HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return mav;
        }
        log.info("Inside login controller");
        ParameterizedTypeReference<BaseResponse<List<TokenDto>>> responseType =
                new ParameterizedTypeReference<>() {
                };
        ResponseEntity<BaseResponse<List<TokenDto>>> responseEntity = restTemplate.exchange(
                "/login",
                POST,
                new HttpEntity<>(loginFormDto),
                responseType);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            log.info("Status: success");
            BaseResponse<List<TokenDto>> body = responseEntity.getBody();
            log.info("Response: {}", body);
            String accessToken = body.getPayload().stream()
                    .filter(tokenDto -> tokenDto.getName().equalsIgnoreCase("access-token"))
                    .findFirst()
                    .map(TokenDto::getBody)
                    .orElseThrow(() -> new RuntimeException("not implemented"));
            String refreshToken = responseEntity.getBody().getPayload().stream()
                    .filter(tokenDto -> tokenDto.getName().equalsIgnoreCase("refresh-token"))
                    .findFirst()
                    .map(TokenDto::getBody)
                    .orElseThrow(() -> new RuntimeException("not implemented"));
            Cookie accessCookie = new Cookie(env.getProperty("access.cookie.name"), accessToken);
            accessCookie.setHttpOnly(true);
            if (loginFormDto.getRememberMe()) {
                accessCookie.setMaxAge(env.getProperty("cookie.age.seconds", Integer.class));
                Cookie refreshCookie = new Cookie(env.getProperty("refresh.cookie.name"), refreshToken);
                refreshCookie.setMaxAge(env.getProperty("cookie.age.seconds", Integer.class));
                refreshCookie.setPath("/login/refresh");
                refreshCookie.setHttpOnly(true);
                response.addCookie(accessCookie);
                response.addCookie(refreshCookie);
            } else {
                accessCookie.setMaxAge(-1);
                response.addCookie(accessCookie);
            }
        } else {
            throw new RuntimeException();
        }
        return new ModelAndView("redirect:/landing");
    }

    @GetMapping("/signup")
    public ModelAndView signup(ModelAndView mav) {
        mav.addObject("registerUserDto", new RegisterUserDto());
        mav.setViewName("sign-up");
        return mav;
    }
}
