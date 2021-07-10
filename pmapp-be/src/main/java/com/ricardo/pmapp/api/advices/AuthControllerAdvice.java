package com.ricardo.pmapp.api.advices;

import com.ricardo.pmapp.exceptions.LoginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class AuthControllerAdvice {

    Logger logger = LoggerFactory.getLogger(AuthControllerAdvice.class);

    @ResponseBody
    @ExceptionHandler(LoginException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    String loginExceptionHandler(LoginException ex) {
        logger.error(ex.getMessage(), ex);
        return ex.getMessage();
    }
}
