package com.snapengage.intergrations.custom.model.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ErrorResponse {
    @JsonProperty(required = true)
    private ErrorCode errorCode;
    private String message;
}
