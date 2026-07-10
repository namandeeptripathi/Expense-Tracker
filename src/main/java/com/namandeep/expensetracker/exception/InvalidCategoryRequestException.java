package com.namandeep.expensetracker.exception;

/** Raised when a category request is semantically invalid. */
public class InvalidCategoryRequestException extends RuntimeException {

    public InvalidCategoryRequestException(String message) {
        super(message);
    }
}
