package com.vcet.studentanalysis.exception;

/**
 * Thrown when internal or external marks exceed allowed limits or are negative.
 */
public class InvalidMarkException extends Exception {
    public InvalidMarkException(String message) {
        super(message);
    }
}
