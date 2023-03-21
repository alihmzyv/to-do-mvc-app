package com.alihmzyv.todomvcapp.model.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;


@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterUserDto implements Serializable {
    @NotBlank(message = "{field.notblank}")
    String firstName;
    @NotBlank(message = "{field.notblank}")
    String lastName;
    @Email(message = "{email.valid}")
    @NotBlank(message = "{field.notblank}")
    String emailAddress;
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$", //TODO: read from properties file
            message = "{password.strong}")
    @NotBlank(message = "{field.notblank}")
    String password;
}