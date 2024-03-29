package com.ricardo.pmapp.api.advices;

import com.ricardo.pmapp.exceptions.UserCreationException;
import com.ricardo.pmapp.exceptions.UserDeletionException;
import com.ricardo.pmapp.exceptions.UserNotFoundException;
import com.ricardo.pmapp.exceptions.UserUpdateException;
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
public class UserControllerAdvice {

    Logger logger = LoggerFactory.getLogger(UserControllerAdvice.class);

    @ResponseBody
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String userNotFoundHandler(UserNotFoundException ex) {
        logger.error(ex.getMessage(), ex);
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(UserCreationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String userCreationExceptionHandler(UserCreationException ex) {
        logger.error(ex.getMessage(), ex);
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(UserUpdateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String userUpdateExceptionHandler(UserUpdateException ex) {
        logger.error(ex.getMessage(), ex);
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(UserDeletionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String userDeletionExceptionHandler(UserDeletionException ex) {
        logger.error(ex.getMessage(), ex);
        return ex.getMessage();
    }
}
