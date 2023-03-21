package com.alihmzyv.todomvcapp.model.dto.base;

import com.alihmzyv.todomvcapp.model.dto.error.ErrorResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.PagedModel;

import java.util.Set;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
@ToString
public class BaseResponse<A> {
    Boolean success;
    Integer status;
    String message;
    A payload;
    PagedModel.PageMetadata pageMetadata;
    Set<ErrorResponse> errors;
    Links links;
}
