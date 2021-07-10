package com.ricardo.pmapp.exceptions;

public final class ExceptionMessages {

    private ExceptionMessages() {
        throw new IllegalStateException("Don't create an instance of this class, use its static constants");
    }

    public static final String LOGIN_EXCEPTION = "Login exception: %s";

    public static final String MISSING_MANDATORY_FIELDS_USER_CREATION = "User creation: please provide mandatory " +
            "fields username, email and/or password";

    public static final String USER_ALREADY_EXISTS = "A user with the same username already exists: %s";

    public static final String USER_NOT_EXISTING = "The requested User doesn't exist in the system: %s";

    public static final String INTERNAL_SERVER_ERROR = "An error occurred in the system";

    public static final String INVALID_JWT_USER = "Wrong username from JWT: user not existing or different in db";

    public static final String INVALID_PASSWORD = "Invalid password format. The format must be min-length: " +
            "6 characters with at least 1 uppercase letter and 1 number";

    public static final String TASK_CODE_IN_CREATION = "You shouldn't provide a code on Task creation";

    public static final String ASSIGNEE_NOT_EXISTING = "Task creation failed. The provided Assignee username " +
            "doesn't exist: %s";

    public static final String TASK_NOT_EXISTING = "The requested Task doesn't exist in the system: %s";
}
