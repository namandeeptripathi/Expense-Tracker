package com.namandeep.expensetracker.exception;

/** Raised when an income record is absent or not owned by the current user. */
public class IncomeNotFoundException extends RuntimeException {

    public IncomeNotFoundException(String message) {
        super(message);
    }
}
