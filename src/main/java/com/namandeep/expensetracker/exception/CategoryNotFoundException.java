package com.namandeep.expensetracker.exception;

/** Raised when a category is absent or not owned by the current user. */
public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(String message) {
        super(message);
    }
}
