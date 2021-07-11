package com.ricardo.pmapp.exceptions;

public final class ExceptionMessages {

    private ExceptionMessages() {
        throw new IllegalStateException("Don't create an instance of this class, use its static constants");
    }

    /*
     * Auth related Exceptions
     * */

    public static final String LOGIN_EXCEPTION = "Login exception: %s";

    public static final String INVALID_JWT_USER = "Wrong username from JWT: user not existing or different in db";


    /*
     * Project related Exceptions
     * */

    public static final String PROJECT_CODE_IN_CREATION = "You shouldn't provide a code on Project creation";

    public static final String MISSING_PROJECT_MANAGER = "Project creation/update failed. No project manager " +
            "specified.";

    public static final String PROJECT_MANAGER_NOT_EXISTING = "Project creation/update failed. The provided " +
            "Project Manager doesn't exist: %s";

    public static final String PROJECT_NOT_EXISTING = "The requested Project doesn't exist in the system: %s";

    public static final String FORBIDDEN_PROJECT = "Operation failed. You have no rights to modify " +
            "the Project '%s' and its Tasks, or to change Project Manager.";


    /*
     * Task related Exceptions
     * */
    public static final String TASK_CODE_IN_CREATION = "You shouldn't provide a code on Task creation";

    public static final String ASSIGNEE_NOT_EXISTING = "Task creation/update failed. The provided Assignee " +
            "doesn't exist: %s";

    public static final String TASK_NOT_EXISTING = "The requested Task doesn't exist in the system: %s";

    /*
     * User related Exceptions
     * */

    public static final String MISSING_MANDATORY_FIELDS_USER_CREATION = "User creation: please provide mandatory " +
            "fields username, email and/or password";

    public static final String USER_ALREADY_EXISTS = "A user with the same username already exists: %s";

    public static final String USER_NOT_EXISTING = "The requested User doesn't exist in the system: %s";

    public static final String INVALID_PASSWORD = "Invalid password format. The format must be min-length: " +
            "6 characters with at least 1 uppercase letter and 1 number";


    /*
     * Common Exceptions
     * */

    public static final String INTERNAL_SERVER_ERROR = "An error occurred in the system";
}
