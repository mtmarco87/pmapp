package com.ricardo.pmapp.exceptions;

public class TaskNotFoundException extends Exception {

    public TaskNotFoundException(String errorMessage){
        super(errorMessage);
    }

}
