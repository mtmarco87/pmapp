package com.ricardo.pmapp.api.advices;

import com.ricardo.pmapp.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class TaskControllerAdvice {

    Logger logger = LoggerFactory.getLogger(TaskControllerAdvice.class);

    @ResponseBody
    @ExceptionHandler(TaskNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String taskNotFoundHandler(TaskNotFoundException ex) {
        logger.error(ex.getMessage(), ex);
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(TaskCreationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String taskCreationExceptionHandler(TaskCreationException ex) {
        logger.error(ex.getMessage(), ex);
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(TaskUpdateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String taskUpdateExceptionHandler(TaskUpdateException ex) {
        logger.error(ex.getMessage(), ex);
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(TaskDeletionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String taskDeletionExceptionHandler(TaskDeletionException ex) {
        logger.error(ex.getMessage(), ex);
        return ex.getMessage();
    }
}
