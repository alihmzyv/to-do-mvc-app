package com.alihmzyv.todomvcapp.controller;

import com.alihmzyv.todomvcapp.config.CookieProperties;
import com.alihmzyv.todomvcapp.exception.LoginException;
import com.alihmzyv.todomvcapp.model.dto.base.BaseResponse;
import com.alihmzyv.todomvcapp.model.dto.error.ErrorResponse;
import com.alihmzyv.todomvcapp.model.dto.security.TokenDto;
import com.alihmzyv.todomvcapp.model.dto.user.LoginFormDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
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
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpMethod.POST;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/")
public class LoginController {
    private final RestTemplate restTemplate;
    private final CookieProperties cookieProperties;

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
        log.info("Inside login controller");
        if (bindingResult.hasErrors()) {
            return mav;
        }

        //send request
        ParameterizedTypeReference<BaseResponse<List<TokenDto>>> responseType =
                new ParameterizedTypeReference<>() {};
        ResponseEntity<BaseResponse<List<TokenDto>>> responseEntity = restTemplate.exchange(
                "/login",
                POST,
                new HttpEntity<>(loginFormDto),
                responseType);

        log.info("Status: success");
        BaseResponse<List<TokenDto>> respBody = Optional.ofNullable(responseEntity.getBody())
                .orElseThrow(() -> new LoginException("An error happened. Please, try again"));
        log.info("Response: {}", respBody);

        boolean success = responseEntity.getStatusCode().is2xxSuccessful();

        if (success) {
            List<TokenDto> payload = respBody.getPayload();
            String accessToken = payload.stream()
                    .filter(tokenDto -> tokenDto.getName().equalsIgnoreCase("access-token"))
                    .findFirst()
                    .map(TokenDto::getBody)
                    .orElseThrow(() -> new LoginException("An error happened. Please, try again"));
            String refreshToken = payload.stream()
                    .filter(tokenDto -> tokenDto.getName().equalsIgnoreCase("refresh-token"))
                    .findFirst()
                    .map(TokenDto::getBody)
                    .orElseThrow(() -> new LoginException("An error happened. Please, try again"));
            Cookie accessCookie = new Cookie(cookieProperties.getAccessName(), accessToken);
            accessCookie.setHttpOnly(true);
            Boolean rememberMe = loginFormDto.getRememberMe();
            log.info("Remembered: {}", rememberMe);
            if (rememberMe) {
                accessCookie.setMaxAge(cookieProperties.getAgeSeconds());
                Cookie refreshCookie = new Cookie(cookieProperties.getRefreshName(), refreshToken);
                refreshCookie.setMaxAge(cookieProperties.getAgeSeconds());
                refreshCookie.setPath("/login/refresh");
                refreshCookie.setHttpOnly(true);
                response.addCookie(accessCookie);
                response.addCookie(refreshCookie);
            } else {
                accessCookie.setMaxAge(-1);
                response.addCookie(accessCookie);
            }
            return new ModelAndView("redirect:/landing");
        } else {
            String errors = respBody.getErrors().stream()
                    .map(ErrorResponse::getMessage)
                    .collect(Collectors.joining());
            throw new LoginException(errors);
        }
    }
}
