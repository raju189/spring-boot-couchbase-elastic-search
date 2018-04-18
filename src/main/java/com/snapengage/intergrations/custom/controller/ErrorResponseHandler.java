package com.snapengage.intergrations.custom.controller;

import com.snapengage.intergrations.custom.model.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

import static com.snapengage.intergrations.custom.model.exception.ErrorCode.UNKNOWN_ERROR;

@Slf4j
@ControllerAdvice
public class ErrorResponseHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public ErrorResponse handleServerException(HttpServletRequest req, Throwable throwable) {
        log.error("Technical error has been occurred", throwable);
        return ErrorResponse.builder()
                .message(throwable.getMessage())
                .errorCode(UNKNOWN_ERROR)
                .build();
    }
}
