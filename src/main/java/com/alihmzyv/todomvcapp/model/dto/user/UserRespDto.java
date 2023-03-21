package com.alihmzyv.todomvcapp.model.dto.user;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRespDto implements Serializable {
    String firstName;
    String lastName;
    String emailAddress;
}