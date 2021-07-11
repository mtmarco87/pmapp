package com.ricardo.pmapp.api.advices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.ricardo.pmapp.exceptions.ExceptionMessages.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class CommonControllerAdvice {

    Logger logger = LoggerFactory.getLogger(CommonControllerAdvice.class);

    @ResponseBody
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    String accessDeniedExceptionHandler(AccessDeniedException ex) {
        logger.error(ex.getMessage(), ex);
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String runtimeExceptionHandler(RuntimeException ex) {
        logger.error(ex.getMessage(), ex);
        return INTERNAL_SERVER_ERROR;
    }
}
