package com.vcet.studentanalysis.exception;

/**
 * Thrown when an underlying JDBC database query or transaction encounters a failure.
 */
public class DatabaseOperationException extends Exception {
    public DatabaseOperationException(String message) {
        super(message);
    }

    public DatabaseOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
