package com.alihmzyv.todomvcapp.model.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginFormDto {
    @Email(message = "{email.valid}")
    @NotBlank(message = "{field.notblank}")
    String emailAddress;
    @NotBlank(message = "{field.notblank}")
    String password;
    Boolean rememberMe = false;
}
