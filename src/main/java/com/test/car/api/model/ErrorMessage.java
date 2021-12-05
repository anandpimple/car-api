package com.test.car.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorMessage {
    private final String message;
    private final String field;
    private final Severity severity;
}