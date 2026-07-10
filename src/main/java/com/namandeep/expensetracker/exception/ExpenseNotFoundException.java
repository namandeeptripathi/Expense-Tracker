package com.namandeep.expensetracker.exception;

/** Raised when an expense is absent or is not owned by the current user. */
public class ExpenseNotFoundException extends RuntimeException {

    public ExpenseNotFoundException(String message) {
        super(message);
    }
}
