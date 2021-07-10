package com.ricardo.pmapp.exceptions;

public final class ExceptionMessages {

    private ExceptionMessages() {
        throw new IllegalStateException("Don't create an instance of this class, use its static constants");
    }

    public static final String LOGIN_EXCEPTION = "Login exception: %s";

    public static final String MISSING_MANDATORY_FIELDS_USER_CREATION = "User creation: please provide mandatory " +
            "fields username, email and/or password";

    public static final String EMAIL_ALREADY_EXISTS = "A user with the same email already exists: %s";

    public static final String USER_NOT_EXISTING = "The requested User doesn't exist in the system: %s";

    public static final String INTERNAL_SERVER_ERROR = "An error occurred in the system";

    public static final String INVALID_JWT_USER = "Wrong username from JWT: user not existing or different in db";
}
