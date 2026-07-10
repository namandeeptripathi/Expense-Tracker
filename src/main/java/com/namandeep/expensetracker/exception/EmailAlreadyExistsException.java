package com.namandeep.expensetracker.exception;

/** Raised when registration attempts to reuse an existing email address. */
public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String email) {
        super("An account already exists for email: " + email);
    }
}
