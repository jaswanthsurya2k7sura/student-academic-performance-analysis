package com.vcet.studentanalysis.exception;

/**
 * Thrown when a queried student register number does not exist.
 */
public class StudentNotFoundException extends Exception {
    public StudentNotFoundException(String message) {
        super(message);
    }

    public StudentNotFoundException(String registerNo, boolean formatMsg) {
        super("Student with Register Number '" + registerNo + "' was not found in the records.");
    }
}
