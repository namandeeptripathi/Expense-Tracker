package com.namandeep.expensetracker.exception;

/** Raised when a user attempts to reuse one of their category names. */
public class CategoryAlreadyExistsException extends RuntimeException {

    public CategoryAlreadyExistsException(String name) {
        super("A category named '" + name + "' already exists");
    }
}
