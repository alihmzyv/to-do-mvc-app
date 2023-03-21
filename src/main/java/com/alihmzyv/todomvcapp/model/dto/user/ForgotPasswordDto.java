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
public class ForgotPasswordDto {
    @NotBlank(message = "{field.notblank}")
    @Email(message = "{email.valid}")
    String emailAddress;
}
