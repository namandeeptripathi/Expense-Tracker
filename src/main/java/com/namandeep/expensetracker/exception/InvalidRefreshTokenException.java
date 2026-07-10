package com.namandeep.expensetracker.exception;

/** Raised when a refresh token is unknown, expired, or revoked. */
public class InvalidRefreshTokenException extends RuntimeException {

    public InvalidRefreshTokenException() {
        super("Refresh token is invalid or expired");
    }
}
