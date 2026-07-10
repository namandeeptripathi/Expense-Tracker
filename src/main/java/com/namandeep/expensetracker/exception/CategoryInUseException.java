package com.namandeep.expensetracker.exception;

/** Raised when deleting a category that still has expense references. */
public class CategoryInUseException extends RuntimeException {

    public CategoryInUseException() {
        super("Category is referenced by expenses; provide reassignToCategoryId before deleting it");
    }
}
