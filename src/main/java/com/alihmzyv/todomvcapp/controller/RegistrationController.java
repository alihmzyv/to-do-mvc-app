package com.alihmzyv.todomvcapp.controller;

import com.alihmzyv.todomvcapp.exception.LoginException;
import com.alihmzyv.todomvcapp.exception.RegistrationException;
import com.alihmzyv.todomvcapp.model.dto.base.BaseResponse;
import com.alihmzyv.todomvcapp.model.dto.error.ErrorResponse;
import com.alihmzyv.todomvcapp.model.dto.user.RegisterUserDto;
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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpMethod.POST;

@Slf4j
@RequiredArgsConstructor
@Controller
public class RegistrationController {
    private final RestTemplate restTemplate;

    @GetMapping("/signup")
    public ModelAndView showSignupForm(ModelAndView mav) {
        mav.addObject("registerUserDto", new RegisterUserDto());
        mav.setViewName("sign-up");
        return mav;
    }

    @PostMapping("/signup")
    public ModelAndView processSignupForm(
            @ModelAttribute @Valid RegisterUserDto registerUserDto,
            ModelAndView mav,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return mav;
        }

        //send request
        ParameterizedTypeReference<BaseResponse<Object>> responseType =
                new ParameterizedTypeReference<>() {};
        ResponseEntity<BaseResponse<Object>> responseEntity = restTemplate.exchange(
                "/users",
                POST,
                new HttpEntity<>(registerUserDto),
                responseType);

        log.info("Status: success");
        BaseResponse<Object> respBody = Optional.ofNullable(responseEntity.getBody())
                .orElseThrow(() -> new LoginException("An error happened. Please, try again"));
        log.info("Response: {}", respBody);

        boolean success = responseEntity.getStatusCode().is2xxSuccessful();

        if (success) {
            return new ModelAndView("redirect:/login");
        } else if (responseEntity.getStatusCode().value() == 400) {
            String errors = respBody.getErrors().stream()
                    .map(ErrorResponse::getMessage)
                    .collect(Collectors.joining());
            throw new RegistrationException(errors);
        } else {
            throw new RegistrationException("Something went wrong. Please, try again");
        }
    }
}
