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
public class ProjectControllerAdvice {

    Logger logger = LoggerFactory.getLogger(ProjectControllerAdvice.class);

    @ResponseBody
    @ExceptionHandler(ProjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String projectNotFoundHandler(ProjectNotFoundException ex) {
        logger.error(ex.getMessage(), ex);
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(ProjectCreationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String projectCreationExceptionHandler(ProjectCreationException ex) {
        logger.error(ex.getMessage(), ex);
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(ProjectUpdateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String projectUpdateExceptionHandler(ProjectUpdateException ex) {
        logger.error(ex.getMessage(), ex);
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(ProjectDeletionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String projectDeletionExceptionHandler(ProjectDeletionException ex) {
        logger.error(ex.getMessage(), ex);
        return ex.getMessage();
    }
}
