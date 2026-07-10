package com.namandeep.expensetracker.exception;

/** Raised when income pagination or sorting input is unsupported. */
public class InvalidIncomeQueryException extends RuntimeException {

    public InvalidIncomeQueryException(String message) {
        super(message);
    }
}
