package com.namandeep.expensetracker.exception;

/** Raised when submitted login credentials cannot be authenticated. */
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("Invalid email or password");
    }
}
