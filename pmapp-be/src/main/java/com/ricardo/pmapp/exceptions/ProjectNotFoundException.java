package com.ricardo.pmapp.exceptions;

public class ProjectNotFoundException extends Exception {

    public ProjectNotFoundException(String errorMessage){
        super(errorMessage);
    }

}
