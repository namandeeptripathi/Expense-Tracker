package com.namandeep.expensetracker.exception;

/** Raised when an expense list query contains an unsupported sort field. */
public class InvalidExpenseQueryException extends RuntimeException {

    public InvalidExpenseQueryException(String message) {
        super(message);
    }
}
